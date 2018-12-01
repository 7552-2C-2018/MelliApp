package com.a7552_2c_2018.melliapp.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.a7552_2c_2018.melliapp.R;
import com.a7552_2c_2018.melliapp.model.ChatItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

@SuppressWarnings("SpellCheckingInspection")
public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder>{

    private final List<ChatItem> values;

    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView tvTitle;
        final CircleImageView ivPhoto;
        final View layout;

        ViewHolder(View v) {
            super(v);
            layout = v;
            tvTitle = v.findViewById(R.id.chatTvTitle);
            ivPhoto = v.findViewById(R.id.chatPhoto);
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ChatsAdapter(List<ChatItem> myDataset) {
        values = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ChatsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                      int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.chat_element, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final String title = values.get(position).getTitle();
        holder.tvTitle.setText(title);
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

    public ChatItem getChatItem(int position) {
        return values.get(position);
    }

}
