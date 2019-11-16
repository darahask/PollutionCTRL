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
import com.example.pollutionctrl.askclasses.AskAdapter;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<MyData> dataList;
    private AskAdapter.OnClickFucker onClickFucker;

    public MyAdapter(Context context, ArrayList<MyData> dataList, AskAdapter.OnClickFucker onClickFucker) {
        this.context = context;
        this.dataList = dataList;
        this.onClickFucker = onClickFucker;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cards_layout,parent,false);
        return new MyViewHolder(view,onClickFucker);
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

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView t;
        ImageView imageView;
        View itemView;
        AskAdapter.OnClickFucker onClickFucker;

        public MyViewHolder(@NonNull View itemView, AskAdapter.OnClickFucker onClickFucker) {
            super(itemView);

            t = itemView.findViewById(R.id.news_text);
            imageView = itemView.findViewById(R.id.news_image);
            this.itemView = itemView;
            this.onClickFucker = onClickFucker;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickFucker.onClick(getAdapterPosition());
        }
    }
}

