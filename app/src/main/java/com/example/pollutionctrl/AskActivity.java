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

public class AskActivity extends AppCompatActivity {

    FirebaseUser user;
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    DatabaseReference dbRef;
    StorageReference sRef;
    AskAdapter adapter;
    ChildEventListener listener;
    ArrayList<AskMessage> messageArrayList;
    ImageButton imageButton;
    Button button;
    EditText editText;
    Uri imageUri;
    static int RC_PHOTO_PICKER = 11211;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        messageArrayList = new ArrayList<>();

        dbRef = firebaseDatabase.getReference().child("askmessages");
        sRef = firebaseStorage.getReference().child("ask_pics");
        user = FirebaseAuth.getInstance().getCurrentUser();

        imageButton = findViewById(R.id.ask_img_button);
        button = findViewById(R.id.ask_button);
        editText = findViewById(R.id.ask_edit_text);
        recyclerView = findViewById(R.id.ask_recycler);

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

//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.toString().trim().length() > 0) {
//                    button.setEnabled(true);
//                } else {
//                    button.setEnabled(false);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });
//        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1000)});
//
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/jpeg");
//                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
//                startActivityForResult(Intent.createChooser(intent,"Complete action using"),RC_PHOTO_PICKER);
//            }
//        });
//
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(imageUri != null){
//                    sRef.child(imageUri.getLastPathSegment()).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
//                            task.addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    Toast.makeText(AskActivity.this,"Image Uploaded",Toast.LENGTH_SHORT).show();
//                                    String s;
//                                    if(user != null){
//                                        s = user.getDisplayName();
//                                    }else{
//                                        s = "Anonymous";
//                                    }
//
//                                    AskMessage message = new AskMessage(s, LocalDate.now().toString(),uri.toString(),editText.getText().toString());
//                                    dbRef.push().setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            Toast.makeText(AskActivity.this,"Upload done",Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                                }
//                            });
//                        }
//                    });
//                }
//            }
//        });

        adapter = new AskAdapter(this,messageArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK){
            if(data!=null){
                imageUri = data.getData();
            }
        }
    }
}
