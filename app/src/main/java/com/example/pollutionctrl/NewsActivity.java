package com.example.pollutionctrl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pollutionctrl.askclasses.AskAdapter;
import com.example.pollutionctrl.newsdata.AirData;
import com.example.pollutionctrl.newsdata.MyLoader;
import com.example.pollutionctrl.recycleview.MyAdapter;
import com.example.pollutionctrl.recycleview.MyData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<AirData>, LocationListener, AskAdapter.OnClickFucker {

    private FirebaseDatabase db;
    private DatabaseReference ref;
    private ChildEventListener listener;
    private TextView t1,t2,t3,t4,t5,t6,t7,t8;
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private ConnectivityManager cm;
    private LocationManager locationManager;
    private double lon,lat;
    private ArrayList<MyData> list;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        t1 = findViewById(R.id.dTextView1);
        t2 = findViewById(R.id.dTextView2);
        t3 = findViewById(R.id.dTextView3);
        t4 = findViewById(R.id.dTextView4);
        t5 = findViewById(R.id.dTextView5);
        t6 = findViewById(R.id.dTextView6);
        t7 = findViewById(R.id.dTextView7);
        t8 = findViewById(R.id.dTextView8);
        recyclerView = findViewById(R.id.my_recycler);
        list = new ArrayList<>();

        cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        Toolbar toolbar = findViewById(R.id.collapse_toolbar);
        setSupportActionBar(toolbar);

        db = FirebaseDatabase.getInstance();
        ref = db.getReference().child("news_data");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final MyAdapter adapter = new MyAdapter(this,list,this);
        recyclerView.setAdapter(adapter);

        listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                MyData data = dataSnapshot.getValue(MyData.class);
                adapter.addData(data);
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
        ref.addChildEventListener(listener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationManager();
    }

    @NonNull
    @Override
    public Loader<AirData> onCreateLoader(int id, @Nullable Bundle args) {

        final String url = "https://api.airvisual.com/v2/nearest_city";
        final String API_KEY = "0b68b925-1d0e-4b6b-b91f-2a4919e5ef3e";
        String s = url + "?lat=" + lat +"&"+ "lon=" + lon +"&"+ "key=" + API_KEY;

        return new MyLoader(this,s);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<AirData> loader, AirData data) {
        updateUI(data);
        try {
            if(locationManager != null){
            locationManager.removeUpdates(this);
            locationManager=null;}
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<AirData> loader) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected){
            getSupportLoaderManager().initLoader(EARTHQUAKE_LOADER_ID,null,this);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(NewsActivity.this,"Please enable gps and internet",Toast.LENGTH_SHORT).show();
    }

    public void startLocationManager() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    private void updateUI(AirData airData){
        t1.setText(airData.getTp() + " C");
        t2.setText(airData.getName());
        t3.setText("AirQuality: " + airData.getAqi());
        t4.setText("Humidity: " + airData.getHm());
        t5.setText("Pressure: " + airData.getPr());
        t6.setText("WindSpeed: " + airData.getWs());
        t7.setText("WindDir: " + airData.getWd());
        t8.setText("Pollutant: " + airData.getMain());
    }

    @Override
    public void onClick(int position) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(position).getLink()));
        startActivity(i);
    }
}
