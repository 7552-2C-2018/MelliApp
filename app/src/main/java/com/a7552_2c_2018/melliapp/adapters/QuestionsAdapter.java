package com.a7552_2c_2018.melliapp.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.model.BuyItem;
import com.a7552_2c_2018.melliapp.model.Question;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder>{

    private final List<Question> values;

    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView tvQstMsg;
        final RelativeLayout rlResponse;
        final TextView tvRespMsg;
        final View layout;

        ViewHolder(View v) {
            super(v);
            layout = v;
            tvQstMsg = v.findViewById(R.id.qstTvMsq);
            rlResponse = v.findViewById(R.id.qstRlResp);
            tvRespMsg = v.findViewById(R.id.qstTvResp);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public QuestionsAdapter(List<Question> myDataset) {
        values = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public QuestionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                          int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.question, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final long date = values.get(position).getDate();
        final String qst = values.get(position).getQuestion();
        holder.tvQstMsg.setText(convertTime(date) + ": " + qst);
        //holder.tvTitle.setText(title.substring(0,Math.min(title.length(),40)));
        if (values.get(position).getHasResponse()) {
            holder.rlResponse.setVisibility(View.VISIBLE);
            final long respDate = values.get(position).getRespDate();
            final String resp = values.get(position).getResponse();
            holder.tvRespMsg.setText(convertTime(respDate) + ": " + resp);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }

    public Question getQstItem(int position) {
        return values.get(position);
    }

    public String convertTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(date);
    }

}
