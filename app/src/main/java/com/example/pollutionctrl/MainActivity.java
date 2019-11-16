package com.example.pollutionctrl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Menu menu;
    private TextView textView;
    private FirebaseAuth mAuth;
    private static int REQUEST_CODE = 111;
    final private List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_black_24dp);

        drawerLayout = findViewById(R.id.drawer_layout1);
        NavigationView navigationView = findViewById(R.id.nav_view1);
        textView = findViewById(R.id.textView3);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == R.id.nav_news) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(MainActivity.this, NewsActivity.class));
                }else if(id == R.id.nav_post){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    if(mAuth.getCurrentUser()==null){
                        register();
                    }else {
                        startActivity(new Intent(MainActivity.this,PostActivity.class));
                    }
                }else if(id == R.id.nav_ask){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    if(mAuth.getCurrentUser()==null){
                        register();
                    }else {
                        startActivity(new Intent(MainActivity.this,AskActivity.class));
                    }
                }else if(id == R.id.nav_home){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else if(id == R.id.nav_profile){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Uri link = Uri.parse("https://obscure-castle-24161.herokuapp.com/userdetails");
                    Intent i = new Intent(Intent.ACTION_VIEW,link);
                    startActivity(i);
                }else if(id == R.id.nav_share){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{"daruroxx@gmail.com"});
                    i.putExtra(Intent.EXTRA_SUBJECT, "FeedBack");
                    i.putExtra(Intent.EXTRA_TEXT, "Provide your FeedBack.");
                    startActivity(Intent.createChooser(i, "Send Email"));
                }
                return true;
            }
        });

        checkPermission();
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(mAuth.getCurrentUser() != null){
            textView.setText(mAuth.getCurrentUser().getDisplayName());
        }else {
            textView.setText("Not logged in");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.memu_main, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(mAuth.getCurrentUser() != null) {
            menu.findItem(R.id.authenticate).setTitle("Sign Out");
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        }else if(item.getItemId() == R.id.authenticate){
            register();
        }else if(item.getItemId() == R.id.authenticate && item.getTitle() == "Sign Out"){
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    finish();
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ){

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    123);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 111 && resultCode == RESULT_OK){
            MenuItem item = menu.findItem(R.id.authenticate);
            item.setTitle("Sign Out");
        }

    }

    private void register(){
        startActivityForResult( AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setTheme(R.style.CustomTheme)
                .setAvailableProviders(providers)
                .build(),REQUEST_CODE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
