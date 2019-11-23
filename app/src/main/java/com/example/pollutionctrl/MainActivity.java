package com.example.pollutionctrl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.pollutionctrl.myfragments.MyPagerAdapter;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Menu menu;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private static String CHANNEL_ID = "3823";
    FloatingActionButton fab;

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
        getSupportActionBar().setTitle("Articles");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_black_24dp);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            createNotificationChannel();
            Intent intent = new Intent(this, ProfexActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            db.collection("cf-data").document(mAuth.getCurrentUser().getUid()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Double d = (Double) task.getResult().getData().get("cfValue");
                    String s;
                    if(d > 5.5){
                        s = "Your CF is " + d;
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
                                .setSmallIcon(R.drawable.notification_icon)
                                .setContentTitle("UNSAFE EMISSIONS")
                                .setContentText(s)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                        notificationManager.notify(3823,builder.build());
                    }
                }
            });

        }

        drawerLayout = findViewById(R.id.drawer_layout1);
        NavigationView navigationView = findViewById(R.id.nav_view1);
        fab = findViewById(R.id.home_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AskActivity.class));
            }
        });

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
                    startActivity(new Intent(MainActivity.this,LauncherActivity.class));
                    finish();
                }else if(id == R.id.nav_profile){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    if(mAuth.getCurrentUser()==null){
                        register();
                    }else {
                        startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                    }
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


        ViewPager viewPager = findViewById(R.id.home_viewpager);
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(myPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.sliding_tab);
        tabLayout.setupWithViewPager(viewPager);

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
            AuthUI.getInstance().signOut(this);
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

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
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

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
