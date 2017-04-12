package com.example.tarunkukreja.blogapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by tarunkukreja on 10/04/17.
 */

public class BlogAdapter extends RecyclerView.ViewHolder{
    View mView ;
    Context mContext ;



    public BlogAdapter(View itemView) {
        super(itemView);
        mContext = itemView.getContext() ;
        mView = itemView ;

    }

    public void bindBlog(Blog blog){
        TextView title = (TextView) mView.findViewById(R.id.post_title) ;
        TextView desc = (TextView) mView.findViewById(R.id.post_desc) ;
        ImageView image = (ImageView)mView.findViewById(R.id.post_image) ;

        title.setText(blog.getTitle());
        desc.setText(blog.getDecription());

        Picasso.with(mContext).load(blog.getImage()).into(image);
    }
}
