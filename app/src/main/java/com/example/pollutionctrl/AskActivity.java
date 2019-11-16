package com.example.pollutionctrl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.pollutionctrl.askclasses.AskAdapter;
import com.example.pollutionctrl.askclasses.AskMessage;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.LocalDate;
import java.util.ArrayList;

public class AskActivity extends AppCompatActivity implements AskAdapter.OnClickFucker{

    FirebaseUser user;
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    DatabaseReference dbRef;
    StorageReference sRef;
    AskAdapter adapter;
    ChildEventListener listener;
    ArrayList<AskMessage> messageArrayList;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        messageArrayList = new ArrayList<>();

        dbRef = firebaseDatabase.getReference().child("askMessages");
        sRef = firebaseStorage.getReference().child("ask_pics");
        user = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = findViewById(R.id.ask_recycler);
        floatingActionButton = findViewById(R.id.ask_fab);

        listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                AskMessage obj = dataSnapshot.getValue(AskMessage.class);
                adapter.addData(obj);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        dbRef.addChildEventListener(listener);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AskActivity.this,AcardActivity.class));
            }
        });

        adapter = new AskAdapter(this,messageArrayList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRestart(){
        super.onRestart();
        Intent i = new Intent(AskActivity.this, AskActivity.class);
        startActivity(i);
        this.finish();
    }

    @Override
    public void onClick(int position) {
        Intent i = new Intent(AskActivity.this,AcardActivity.class);
        i.putExtra("img",messageArrayList.get(position).getImageUri());
        i.putExtra("txt",messageArrayList.get(position).getMessage());
        i.putExtra("img_reply",messageArrayList.get(position).getReplyImageUri());
        i.putExtra("txt_reply",messageArrayList.get(position).getReplyMessage());
        i.putExtra("key",messageArrayList.get(position).getKey());
        i.putExtra("id",messageArrayList.get(position).getId());
        startActivity(i);

    }

    public ArrayList<AskMessage> getMessageArrayList() {
        return messageArrayList;
    }
}
