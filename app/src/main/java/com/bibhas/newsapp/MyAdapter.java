package com.bibhas.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private List<HeadlineModel> headlineModelList;

    public MyAdapter(Context context, List<HeadlineModel> headlineModelList) {
        this.context = context;
        this.headlineModelList = headlineModelList;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.news_item_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyAdapter.MyViewHolder holder, int position) {
        final HeadlineModel headlineModel=headlineModelList.get(position);
        holder.newTitle.setText(headlineModel.getTitle());
        holder.newsDescription.setText(headlineModel.getDescription());
        holder.newsDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri=Uri.parse(headlineModel.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
                //Log.v("SSSSSS",headlineModel.getUrl());
            }
        });
        holder.newsName.setText(headlineModel.getName());
        holder.newsTime.setText(headlineModel.getPublishedAt());
        Glide.with(context).load(headlineModel.getUrlToImage())
                .thumbnail(0.5f)
                .into(holder.newsImage);
    }

    @Override
    public int getItemCount() {
        return headlineModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView newTitle;
        TextView newsDescription;
        TextView newsName;
        TextView newsTime;
        ImageView newsImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            newTitle=itemView.findViewById(R.id.tv_news_title);
            newsDescription=itemView.findViewById(R.id.tv_news_desc);
            newsName=itemView.findViewById(R.id.tv_name);
            newsTime=itemView.findViewById(R.id.tv_news_date);
            newsImage=itemView.findViewById(R.id.im_news_image);
        }
    }
}
