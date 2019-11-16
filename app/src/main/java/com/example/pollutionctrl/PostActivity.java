package com.example.pollutionctrl;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pollutionctrl.extradata.PostData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.LocalDate;
import java.time.LocalTime;

public class PostActivity extends AppCompatActivity {

    FirebaseAuth auth;
    static int RC_PHOTO_PICKER = 11211;
    FirebaseFirestore db;
    FirebaseStorage file;
    Spinner spinner;
    FloatingActionButton imageButton;
    EditText editText,editText2;
    StorageReference storageReference;
    FirebaseUser uName;
    Uri imageUri;
    String name;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        file = FirebaseStorage.getInstance();
        storageReference = file.getReference().child("post_pics");
        editText2 = findViewById(R.id.post_article_title);

        if(auth.getCurrentUser() != null){
            uName = auth.getCurrentUser();
        }

        imageButton = findViewById(R.id.fab);
        editText = findViewById(R.id.post_edit);
        progressBar = findViewById(R.id.my_progress);
        spinner = findViewById(R.id.post_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.spinArray,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                name = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(intent,"Complete action using"),RC_PHOTO_PICKER);
            }
        });

        TextView textView = findViewById(R.id.post_txt);
        String s = "Welcome, " + uName.getDisplayName();
        textView.setText(s);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_post,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.post_post){
            progressBar.setVisibility(View.VISIBLE);
            StorageReference photoRef = storageReference.child(imageUri.getLastPathSegment());
            photoRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Toast.makeText(PostActivity.this,"Uploaded the Post we will contact you once we accept it",Toast.LENGTH_SHORT)
                                    .show();
                            String s = LocalDate.now().toString() +" "+ LocalTime.now().toString() +" " + "UTC+5:30";
                            PostData postData = new PostData(editText.getText().toString(),uName.getUid(),uri.toString(),uName.getDisplayName(),editText2.getText()
                            .toString(),s);
                            db.collection(name).document(). set(postData, SetOptions.merge())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(PostActivity.this,"Done",Toast.LENGTH_SHORT).show();
                                            imageUri = null;
                                            editText.setText("");
                                            editText2.setText("");
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });
                        }
                    });
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }


}
