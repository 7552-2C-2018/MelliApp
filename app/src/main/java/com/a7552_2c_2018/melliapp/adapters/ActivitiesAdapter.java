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
import android.widget.TextView;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.model.BuyItem;

import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ViewHolder>{

    private final List<String> values;

    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView tvTitle;
        final View layout;

        ViewHolder(View v) {
            super(v);
            layout = v;
            tvTitle = v.findViewById(R.id.aciDesc);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ActivitiesAdapter(List<String> myDataset) {
        values = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ActivitiesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                           int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.activities_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final String desc = values.get(position);
        holder.tvTitle.setText(desc);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }

    public String getBuyItem(int position) {
        return values.get(position);
    }

}
