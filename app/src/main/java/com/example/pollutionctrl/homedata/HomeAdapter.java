package com.example.pollutionctrl.homedata;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pollutionctrl.R;
import com.example.pollutionctrl.extradata.PostData;
import com.example.pollutionctrl.recycleview.MyAdapter;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {

    private ArrayList<PostData> list;
    private OnHomeCardCLick onHomeCardCLick;

    public HomeAdapter(ArrayList<PostData> list, OnHomeCardCLick onHomeCardCLick) {
        this.list = list;
        this.onHomeCardCLick = onHomeCardCLick;
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homecard_layout, parent, false);
        return new HomeHolder(view,onHomeCardCLick);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, int position) {
        PostData data = list.get(position);
        holder.tm.setText(data.getArticle_name());

        ImageView img = holder.imageView;
        if(!data.getImageUri().isEmpty())
            Glide.with(img.getContext()).load(data.getImageUri()).into(img);
        else
            Glide.with(img.getContext()).load(R.drawable.ic_man_user).into(img);

        if(data.getImageUri() == null){
            holder.ts.setText(data.getData() + "d");
        }else {
            holder.ts.setText(data.getName());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addData(PostData data){
        list.add(data);
        notifyDataSetChanged();
    }

    class HomeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tm,ts;
        ImageView imageView;
        OnHomeCardCLick onHomeCardCLick;

        public HomeHolder(@NonNull View itemView, OnHomeCardCLick onHomeCardCLick) {
            super(itemView);
            tm = itemView.findViewById(R.id.home_text_card);
            ts = itemView.findViewById(R.id.home_txt1_card);
            imageView = itemView.findViewById(R.id.home_img_card);
            this.onHomeCardCLick = onHomeCardCLick;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onHomeCardCLick.onSelect(getAdapterPosition());
        }
    }

    public interface OnHomeCardCLick{
        public void onSelect(int position);
    }
}
