package com.example.pollutionctrl.myfragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pollutionctrl.HcardActivity;
import com.example.pollutionctrl.R;
import com.example.pollutionctrl.extradata.PostData;
import com.example.pollutionctrl.homedata.HomeAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class AirPollution extends Fragment implements HomeAdapter.OnHomeCardCLick {

    FirebaseFirestore firestore;
    ArrayList<PostData> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_air_pollution, container, false);
        list = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.air_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        final HomeAdapter homeAdapter = new HomeAdapter(list,this);

        firestore = FirebaseFirestore.getInstance();

        firestore.collection("air-pollution").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot document : task.getResult().getDocuments()){
                        Timestamp t = document.getTimestamp("Date");
                        homeAdapter.addData(new PostData(t,document.getString("_id"),
                                document.getString("article_name"),
                                document.getString("desp"),
                                document.getString("img"),
                                document.getString("name")));
                    }
                }
            }
        });

        recyclerView.setAdapter(homeAdapter);

        return view;
    }

    @Override
    public void onSelect(int position) {
        PostData data = list.get(position);
        HashMap<String,String> map = new HashMap<>();
        map.put("date",data.getDate());
        map.put("_id",data.getUserUID());
        map.put("article_name",data.getArticle_name());
        map.put("desp",data.getData());
        map.put("img",data.getImageUri());
        map.put("name",data.getName());

        Intent  i = new Intent(getActivity(), HcardActivity.class);
        i.putExtra("map",map);
        startActivity(i);
    }
}