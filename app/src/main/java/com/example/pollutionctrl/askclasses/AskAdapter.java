package com.example.pollutionctrl.askclasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pollutionctrl.R;

import java.util.ArrayList;

public class AskAdapter extends RecyclerView.Adapter<AskAdapter.AskViewHolder> {

    private Context context;
    private ArrayList<AskMessage> list;

    public AskAdapter(Context context, ArrayList<AskMessage> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AskAdapter.AskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AskViewHolder(LayoutInflater.from(context).inflate(R.layout.ask_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AskAdapter.AskViewHolder holder, int position) {
        AskMessage message = list.get(position);

        ImageView imageView = holder.imageView;

        Glide.with(context).load(message.getImageUri()).into(imageView);


        holder.tm.setText(message.getMessage());
        holder.tl.setText(message.getId());
        holder.tr.setText(message.getDate());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addData(AskMessage message){
        list.add(message);
        notifyDataSetChanged();
    }

    class AskViewHolder extends RecyclerView.ViewHolder{

        View itemView;
        ImageView imageView;
        TextView tm,tl,tr;

        public AskViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            imageView = itemView.findViewById(R.id.ask_card_img);
            tm = itemView.findViewById(R.id.ask_message);
            tl = itemView.findViewById(R.id.ask_id);
            tr = itemView.findViewById(R.id.ask_date);
        }
    }
}
