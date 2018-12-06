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
import com.a7552_2c_2018.melliapp.model.PostItem;

import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{

    private final List<PostItem> values;

    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView tvPrice;
        final TextView tvDesc;
        final ImageView ivPhoto;
        final View layout;

        ViewHolder(View v) {
            super(v);
            layout = v;
            tvPrice = v.findViewById(R.id.eTvPrice);
            tvDesc = v.findViewById(R.id.eTvDesc);
            ivPhoto = v.findViewById(R.id.miniPhoto);
        }

    }

// --Commented out by Inspection START (01/10/2018 23:20):
//    public void add(int position, PostItem item) {
//        values.add(position, item);
//        notifyItemInserted(position);
//    }
// --Commented out by Inspection STOP (01/10/2018 23:20)
    /*
    public void remove(int position) {
        values.remove(position);
        notifyItemRemoved(position);
    }
    */

    // Provide a suitable constructor (depends on the kind of dataset)
    public ItemAdapter(List<PostItem> myDataset) {
        values = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                     int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.element, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final String price = "$ " + values.get(position).getPrice();
        final String desc = values.get(position).getDesc();
        holder.tvPrice.setText(price);
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

    public PostItem getPostItem(int position) {
        return values.get(position);
    }

}
