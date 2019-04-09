package com.example.admin.trackme.views;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.trackme.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TrackingActivity extends AppCompatActivity implements
        OnMapReadyCallback ,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener{

    private Polyline gpsTrack;
    private GoogleApiClient googleApiClient;
    private LatLng lastKnownLatLng;

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;

    ImageView btnPause,btnStop,btnReplay;
    TextView tvTime,tvDistance,tvSpeed;

    private Date startTime;
    private Date endTime;
    private Handler myHandler = new Handler();
    double distance =0;
    List<Float> listSpeed = new ArrayList<Float>();
    float speed;
    static long sum_time;

    boolean isRunning = true;
    boolean isRecording = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFrag.getMapAsync(this);

        btnReplay = (ImageView) findViewById(R.id.btnReplay);
        btnStop = (ImageView) findViewById(R.id.btnStop);
        btnPause = (ImageView) findViewById(R.id.btnPause);

        tvTime = (TextView) findViewById(R.id.tvTime);
        tvSpeed = (TextView) findViewById(R.id.tvSpeed);
        tvDistance = (TextView) findViewById(R.id.tvDistance);

        if(startTime == null)
        {
            startTime = new Date();
            this.runTimer();
        }
        //Count Time
        //startTime = SystemClock.uptimeMillis();
        //myHandler.postDelayed(updateTimerMethod, 0);
        // Button


        btnReplay.setVisibility(View.GONE);
        btnStop.setVisibility(View.GONE);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnReplay.setVisibility(View.VISIBLE);
                btnStop.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.GONE);
//                timeSwap += timeInMillies;
                myHandler.removeCallbacks(updateTimerMethod);
                endTime = new Date();
                isRecording = false;
                isRunning = false;

            }
        });
        btnReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnReplay.setVisibility(View.GONE);
                btnStop.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                //myHandler.post(replayTimerMethod);
                myHandler.post(updateTimerMethod);
                isRunning = true;
            }
        });

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }
    public static void setDate(TextView receiver,Date date,Date now) {
        if (date != null) {
            try {
                long time_usage = now.getTime() - date.getTime();
                sum_time = time_usage;
                long seconds = time_usage / (long)1000 % (long)60;
                long minutes = time_usage / (long)'\uea60' % (long)60;
                long hours = time_usage / (long)3600000 % (long)23;
                String format_time = "%02d:%02d:%02d";
                Object[] objTime = new Object[]{hours, minutes, seconds};
                String time = String.format(format_time, Arrays.copyOf(objTime, objTime.length));
                receiver.setText((CharSequence)time);
            } catch (Throwable e) {
            }

        }
    }
    public static void setDateRange(TextView receiver, Date start,Date end) {
        if (start != null && end != null) {
            try {
                long time_usage = end.getTime() - start.getTime();
                long seconds = time_usage / (long)1000 % (long)60;
                long minutes = time_usage / (long)'\uea60' % (long)60;
                long hours = time_usage / (long)3600000 % (long)23;
                String format_time = "%02d:%02d:%02d";
                Object[] objTime = new Object[]{hours, minutes, seconds};
                String time = String.format(format_time, Arrays.copyOf(objTime, objTime.length));
                receiver.setText((CharSequence)time);
            } catch (Throwable e) {
            }

        }
    }
    private Runnable updateTimerMethod = new Runnable() {

        public void run() {
            TextView tvTime = (TextView) findViewById(R.id.tvTime);
            if (isRecording) {
                Date now = new Date();
                TrackingActivity.setDate(tvTime, TrackingActivity.this.startTime,now);
                myHandler.postDelayed(updateTimerMethod, 1000L);
            }
            else
            {
                Date now = endTime;
                long temp = -(now.getTime()-endTime.getTime());
                TrackingActivity.setDate(tvTime, TrackingActivity.this.startTime,now);
                myHandler.postDelayed(updateTimerMethod, 0);
//                TrackingActivity.setDateRange(tvTime, TrackingActivity.this.startTime,TrackingActivity.this.endTime);
//                myHandler.postDelayed(updateTimerMethod, 1000L);
//                TrackingActivity.setDate(tvTime, TrackingActivity.this.startTime);
//                Date now = new Date();
//                long temp = -(now.getTime() - TrackingActivity.this.endTime.getTime());
//                myHandler.postDelayed(updateTimerMethod, temp);
            }
        }

    };
    private Runnable replayTimerMethod = new Runnable() {

        public void run() {
            TextView tvTime = (TextView) findViewById(R.id.tvTime);
            TrackingActivity.setDateRange(tvTime, TrackingActivity.this.startTime,TrackingActivity.this.endTime);
//            if (isRunning) {
            myHandler.post(replayTimerMethod);
        }

    };
    private void runTimer() {
        //myHandler.post((Runnable)this.timerRunnable);
        myHandler.post(updateTimerMethod);

    }
    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LatLng calymayor = new LatLng(19.345822, -99.152274);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(calymayor));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(calymayor, 15));

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.CYAN);
        polylineOptions.width(4);
        gpsTrack = mGoogleMap.addPolyline(polylineOptions);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback,
                        Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback,
                    Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (googleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lastKnownLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        updateTrack();

        Location temp = mLastLocation;     //save the old location
        mLastLocation = location;          //get the new location
        distance = (mLastLocation.distanceTo(temp)/ 1000.00);   //find the distance

        //calculating the speed with getSpeed method it returns speed in m/s so we are converting it into kmph
        speed = location.getSpeed()*18/5;
        listSpeed.add(speed);
        updateUI();
    }
    //The live feed of Distance and Speed are being set in the method below .
    private void updateUI() {
            String stringDistance = String.format ("%.3f", distance);
            tvDistance.setText(stringDistance);
//            avgSpeed = AverageSpeed(arrayListSpeed);
//            speed = (float) (distance/(sum_time));
            String stringAvgSpeed = String.format ("%.2f", speed);
            tvSpeed.setText(stringAvgSpeed);
//

    }
    private Float AverageSpeed(ArrayList<Float> arrayListSpeed){
        float sum = 0;
        for(float speed:arrayListSpeed)sum+=speed;
        return (sum/arrayListSpeed.size());
    }


//    @Override
//    public boolean onUnbind(Intent intent) {
//        stopLocationUpdates();
//        if (mGoogleApiClient.isConnected())
//            mGoogleApiClient.disconnect();
//        lStart = null;
//        lEnd = null;
//        distance = 0;
//        return super.onUnbind(intent);
//    }

    protected void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }


    private void updateTrack() {
        List<LatLng> points = gpsTrack.getPoints();
        points.add(lastKnownLatLng);
        gpsTrack.setPoints(points);
    }

    //

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                //move map camera
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latLng.latitude, latLng.longitude)).zoom(16).build();
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }
    };

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(TrackingActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {
                    // if not allow a permission, the application will exit
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    System.exit(0);
                }
            }
        }
    }

}
