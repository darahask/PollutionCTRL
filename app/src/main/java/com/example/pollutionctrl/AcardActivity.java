package com.example.pollutionctrl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pollutionctrl.askclasses.AskMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.LocalDate;

public class AcardActivity extends AppCompatActivity {

    ImageView img,img_reply;
    FloatingActionButton fab;
    TextView txt;
    EditText eTxt;
    CardView cardView;
    Uri replyImageUri;
    Intent data;
    ProgressBar pgbar;
    private static int RC_PHOTO_PICKER = 123;
    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;
    FirebaseDatabase firebaseDatabase;
    StorageReference sRef;
    DatabaseReference dRef;
    FirebaseUser user;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acard);

        img = findViewById(R.id.acard_img);
        img_reply = findViewById(R.id.acard_img_reply);
        txt = findViewById(R.id.acard_text);
        fab = findViewById(R.id.acard_fab);
        eTxt = findViewById(R.id.acard_text_reply);
        cardView = findViewById(R.id.acard_card);
        cardView.setVisibility(View.GONE);
        pgbar = findViewById(R.id.acard_progressbar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        data = getIntent();

        sRef = firebaseStorage.getReference().child("ask_pics");
        dRef = firebaseDatabase.getReference().child("askMessages");


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(intent,"Complete action using"),RC_PHOTO_PICKER);
            }
        });

        if(data.getExtras() != null){
            cardView.setVisibility(View.VISIBLE);
            String img,txt,img_reply,txt_reply,key,name;
            img = data.getStringExtra("img");
            img_reply = data.getStringExtra("img_reply");
            txt = data.getStringExtra("txt");
            txt_reply = data.getStringExtra("txt_reply");
            name = data.getStringExtra("id");
            key = data.getStringExtra("key");

            if(!img.isEmpty())
                Glide.with(this).load(img).into(this.img);
            else
                this.img.setVisibility(View.GONE);
            this.txt.setText(txt);

            this.img_reply.setVisibility(View.VISIBLE);
            if(!(img_reply == null)) {
                if(!img_reply.isEmpty()){
                    this.img_reply.setVisibility(View.VISIBLE);
                    Glide.with(this).load(img_reply).into(this.img_reply);
                }else{
                    this.img_reply.setVisibility(View.GONE);
                }
            }else {
                this.img_reply.setVisibility(View.GONE);
            }
            getSupportActionBar().setTitle(name);
            this.key = key;

            if(txt_reply == null)
                this.eTxt.setHint(R.string.dummy);
            else {
                this.eTxt.setText(txt_reply);
                this.eTxt.setTextSize(20);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_acard,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        menu.findItem(R.id.acard_reply).setTitle("Ask");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        pgbar.setVisibility(View.VISIBLE);
        int id = item.getItemId();
        if( id == R.id.acard_reply && !data.hasExtra("txt")){
            if(firebaseAuth.getCurrentUser()!=null){
                if(replyImageUri != null){
                    sRef.child(replyImageUri.getLastPathSegment()).putFile(replyImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                            task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Toast.makeText(AcardActivity.this,"Image Uploaded",Toast.LENGTH_SHORT).show();
                                    String s;
                                    if(user != null){
                                        s = user.getDisplayName();
                                    }else{
                                        s = "Anonymous";
                                    }
                                    DatabaseReference d = dRef.push();
                                    AskMessage msg = new AskMessage(s, LocalDate.now().toString(),uri.toString(),eTxt.getText().toString(),
                                            null,null,d.getKey(),"Not Answered");
                                    d.setValue(msg).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            pgbar.setVisibility(View.GONE);
                                            Toast.makeText(AcardActivity.this,"Done",Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                                }
                            });
                        }
                    });
                }else{
                    String s;
                    if(user != null){
                        s = user.getDisplayName();
                    }else{
                        s = "Anonymous";
                    }
                    DatabaseReference d = dRef.push();
                    AskMessage msg = new AskMessage(s, LocalDate.now().toString(),"",eTxt.getText().toString(),
                            null,null,d.getKey(),"Not Answered");
                    d.setValue(msg).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pgbar.setVisibility(View.GONE);
                            Toast.makeText(AcardActivity.this,"Done",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }
        }else if(id == R.id.acard_reply && key!=null && firebaseAuth.getCurrentUser()!=null && replyImageUri != null){
            sRef.child(replyImageUri.getLastPathSegment()).putFile(replyImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Toast.makeText(AcardActivity.this,"Image Uploaded",Toast.LENGTH_SHORT).show();
                            DatabaseReference d = dRef.child(key);
                            AskMessage msg = new AskMessage(data.getStringExtra("id"), LocalDate.now().toString(),
                                    data.getStringExtra("img"),data.getStringExtra("txt"),
                                    uri.toString(),eTxt.getText().toString(),key,"Answered");
                            d.setValue(msg).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(AcardActivity.this,"Upload done",Toast.LENGTH_SHORT).show();
                                    pgbar.setVisibility(View.GONE);
                                    finish();
                                }
                            });
                        }
                    });
                }
            });
        }else if(id == R.id.acard_reply && key!=null && firebaseAuth.getCurrentUser()!=null && replyImageUri == null){
            DatabaseReference d = dRef.child(key);
            AskMessage msg = new AskMessage(data.getStringExtra("id"), LocalDate.now().toString(),
                    data.getStringExtra("img"),data.getStringExtra("txt"),
                    "",eTxt.getText().toString(),key,"Answered");
            d.setValue(msg).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(AcardActivity.this,"Upload done",Toast.LENGTH_SHORT).show();
                    pgbar.setVisibility(View.GONE);
                    finish();
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK && data!=null){
            replyImageUri = data.getData();
        }
    }


}
