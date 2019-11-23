package com.example.pollutionctrl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;

public class ProfexActivity extends AppCompatActivity {

    TextView t1,t2,t3;
    FirebaseUser user;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profex);

        t1 = findViewById(R.id.profex_headname);
        t2 = findViewById(R.id.profex_ue);
        t3 = findViewById(R.id.profex_status);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        t1.setText("Welcome, " + user.getDisplayName());

        db.collection("cf-data").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot !=null && documentSnapshot.exists()){
                        double x = (Double) documentSnapshot.get("cfValue");
                        double d = x*365/1000;
                        t2.setText("Is equivalent to "+String.format("%.2f",d) + " Tonnes of CO2 per Year");
                        if(x < 5.1){
                            t3.setText("Safe Emissions");
                            t3.setTextColor(Color.parseColor("#039603"));
                        }else if(x < 5.5){
                            t3.setText("Control Your Emissions");
                            t3.setTextColor(Color.parseColor("#E4E400"));
                        }else{
                            t3.setText("Unsafe Emissions");
                            t3.setTextColor(Color.RED);
                        }
                    }else{
                        t2.setText("Please Answer the questions");
                        t3.setText("None");
                        t3.setTextColor(Color.RED);
                    }
                }
            }
        });
    }
}
