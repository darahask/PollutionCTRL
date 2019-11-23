package com.example.pollutionctrl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pollutionctrl.qdata.QData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseDatabase rDb;
    FirebaseUser user;
    ExtendedFloatingActionButton fab;
    DatabaseReference databaseReference;
    ProgressBar progressBar;
    EditText e1, e2;
    TextView t1, t2;
    Button b,b2;
    ArrayList<QData> data;
    double count = 0;
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        db = FirebaseFirestore.getInstance();
        rDb = FirebaseDatabase.getInstance();
        databaseReference = rDb.getReference().child("cf_ques");
        data = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        getSupportActionBar().setTitle("Welcome, " + user.getDisplayName());
        fab = findViewById(R.id.profile_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,ProfexActivity.class));
                finish();
            }
        });

        t1 = findViewById(R.id.prof_mq);
        e1 = findViewById(R.id.prof_ma);
        t2 = findViewById(R.id.prof_sq);
        e2 = findViewById(R.id.prof_sa);
        b  = findViewById(R.id.prof_next);
        b2 = findViewById(R.id.prof_save);
        progressBar = findViewById(R.id.prof_prog);

        Toast.makeText(this,"Enter the values in kms and hrs only",Toast.LENGTH_LONG).show();

        b2.setVisibility(View.GONE);
        b.setText("Go!");
        b.setVisibility(View.GONE);
        t1.setText("Please Answer the Questions");
        e1.setVisibility(View.GONE);
        e2.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                b.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                QData d = dataSnapshot.getValue(QData.class);
                if(d == null){
                    data.add(new QData("Null","NUll","0.00"));
                }else {
                    data.add(d);
                }
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
        });

    }

    public void saveQues(View view){
        if(index<= data.size()){
            String s1,s2;
            s1 = e1.getText().toString();
            s2 = e2.getText().toString();
            if(!s2.isEmpty())
                count = count + ( Double.valueOf(s1)/ (Integer.valueOf(s2) + 1) )*Double.valueOf(data.get(index-1).getEf());
            else if(!s1.isEmpty())
                count = count + (Double.valueOf(s1))*Double.valueOf(data.get(index-1).getEf());
            else
                count += 0;
            e1.setText("");
            e2.setText("");
            Toast.makeText(ProfileActivity.this,"Saved",Toast.LENGTH_SHORT).show();
        }else{
            HashMap<String,Object> map = new HashMap<>();
            map.put("cfValue",count);
            map.put("Date",new Timestamp(new Date()));
            db.collection("cf-data").document(user.getUid()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ProfileActivity.this,"Saved",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ProfileActivity.this,ProfexActivity.class));
                    finish();
                }
            });
        }

    }

    public void nextQues(View view){
        e1.setText("");
        e2.setText("");
       if(index < data.size()){
           b2.setVisibility(View.VISIBLE);
           e1.setVisibility(View.VISIBLE);
           e2.setVisibility(View.VISIBLE);
           b.setText("next");
           String s1,s2;
           s1 = data.get(index).getMainQ();
           s2 = data.get(index).getSubQ();

           t1.setText(s1);
           t2.setText(s2);
           if(s2.isEmpty()){
               e2.setVisibility(View.GONE);
           }
       }else if(index == data.size()){
           e1.setVisibility(View.GONE);
           e2.setVisibility(View.GONE);
           t1.setText("All Answered");
           b.setVisibility(View.GONE);
       }
        index++;
    }
}