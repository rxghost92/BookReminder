package com.example.bookreminder.ui.search;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookreminder.R;
import com.squareup.picasso.Picasso;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    View mView;
    private ViewHolder.ClickListener mClickListener;


    public ViewHolder(View itemView) {
        super(itemView);
        mView = itemView;


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                mClickListener.onItemLongClick(view, getAdapterPosition());
                return true;
            }
        });

    }

    //set details to recycler view row
    public void setDetails(Context ctx, String title, String description, String description_cardview, String image, String autore, String type, String numberpages) {
        //Views
        TextView mTitleTv = mView.findViewById(R.id.rTitleTv);
        TextView mAutore = mView.findViewById(R.id.rAutore);
        TextView mDetailTv = mView.findViewById(R.id.rDescriptionTv);
        TextView mTypeTv = mView.findViewById(R.id.rTypeTv);
        ImageView mImageIv = mView.findViewById(R.id.rImageView);

        //set data to views
        mTitleTv.setText(title);
        mAutore.setText(autore);
        mDetailTv.setText(description_cardview);
        mTypeTv.setText("Genere:" + " " + type);
        Picasso.get().load(image).into(mImageIv);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;

    }


    public interface ClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }


}
