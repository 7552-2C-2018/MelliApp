package com.a7552_2c_2018.melliapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.model.Question;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder>{

    private final List<Question> values;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView tvQstMsg;
        final RelativeLayout rlResponse;
        final TextView tvRespMsg;
        final TextView tvLink;
        final View layout;

        ViewHolder(View v) {
            super(v);
            layout = v;
            tvQstMsg = v.findViewById(R.id.qstTvMsq);
            rlResponse = v.findViewById(R.id.qstRlResp);
            tvRespMsg = v.findViewById(R.id.qstTvResp);
            tvLink = v.findViewById(R.id.qstTvLink);
            context = v.getContext();
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
        holder.tvQstMsg.setText(String.format(context.getString(R.string.date_holder), convertTime(date), qst));


        if (values.get(position).getHasResponse()) {
            holder.rlResponse.setVisibility(View.VISIBLE);
            holder.tvLink.setVisibility(View.GONE);
            final long respDate = values.get(position).getRespDate();
            final String resp = values.get(position).getResponse();
            holder.tvRespMsg.setText(String.format(context.getString(R.string.date_holder), convertTime(respDate), resp));

        } else {
            if (values.get(position).getCanAnswer()) {
                holder.tvLink.setVisibility(View.VISIBLE);
            }
            holder.rlResponse.setVisibility(View.GONE);
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

    private String convertTime(long time){
        Date date = new Date(time * 1000);
        Format format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(date);
    }

}
