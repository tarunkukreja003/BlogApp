package com.example.tarunkukreja.blogapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by tarunkukreja on 10/04/17.
 */

public class BlogAdapter extends RecyclerView.ViewHolder{
    View mView ;
    Context mContext ;



    public BlogAdapter(View itemView) {
        super(itemView);
        mView = itemView ;
    }

    public void bindBlog(Blog blog){
        TextView title = (TextView) mView.findViewById(R.id.post_title) ;
        TextView desc = (TextView) mView.findViewById(R.id.post_desc) ;

        title.setText(blog.getTitle());
        desc.setText(blog.getDecription());
    }
}
