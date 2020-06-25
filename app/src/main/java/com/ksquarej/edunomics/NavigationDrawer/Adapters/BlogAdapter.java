package com.ksquarej.edunomics.NavigationDrawer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ksquarej.edunomics.NavigationDrawer.BlogUploadClass;
import com.ksquarej.edunomics.R;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.MyViewHolder> {
    private ArrayList<BlogUploadClass> list1;
    Context context;

    public BlogAdapter(Context context, ArrayList<BlogUploadClass> list1) {
        this.context = context;
        this.list1 = list1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView personemail,blogdata;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            personemail=itemView.findViewById(R.id.cardView_commodityemail);
            blogdata=itemView.findViewById(R.id.blog_content);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.designofblog, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag((list1.get(i)));
        BlogUploadClass blg = list1.get(i);
        viewHolder.blogdata.setText(blg.getBlogdata());
        viewHolder.personemail.setText(blg.getEmail());
    }

    @Override
    public int getItemCount() {
        return list1.size();
    }

}



