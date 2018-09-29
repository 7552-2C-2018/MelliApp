package com.a7552_2c_2018.melliapp.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.activity.ItemActivity;
import com.a7552_2c_2018.melliapp.model.PostItem;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{

    private List<PostItem> values;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvPrice;
        public TextView tvDesc;
        public ImageView ivPhoto;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            tvPrice = v.findViewById(R.id.eTvPrice);
            tvDesc = v.findViewById(R.id.eTvDesc);
            ivPhoto = v.findViewById(R.id.eIvPhoto);
        }
    }

    public void add(int position, PostItem item) {
        values.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        values.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ItemAdapter(List<PostItem> myDataset) {
        values = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.element, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String price = "$ " + values.get(position).getPrice();
        final String desc = values.get(position).getDesc();
        holder.tvPrice.setText(price);
        /*
        holder.tvPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(position);
            }
        });
        */
        Log.d("ItemAdapter", desc.substring(0,Math.max(desc.length(),40)));
        holder.tvDesc.setText(desc.substring(0,Math.min(desc.length(),40)));
        String base64Image = values.get(position).getImage();
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.ivPhoto.setImageBitmap(decodedByte);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }

}
