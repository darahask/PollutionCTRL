package com.example.pollutionctrl.recycleview;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pollutionctrl.R;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<MyData> dataList;

    public MyAdapter(Context context, ArrayList<MyData> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cards_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {

        MyData myData = dataList.get(position);
        holder.t.setText(myData.getTitle());
        ImageView imageView = holder.imageView;

        Glide.with(context).load(myData.getImgUri()).into(imageView);

    }

    public void addData(MyData data){
        dataList.add(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView t;
        ImageView imageView;
        View itemView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            t = itemView.findViewById(R.id.news_text);
            imageView = itemView.findViewById(R.id.news_image);
            this.itemView = itemView;

        }
    }
}

