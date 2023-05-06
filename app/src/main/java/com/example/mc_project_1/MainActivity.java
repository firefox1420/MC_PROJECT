package com.example.mc_project_1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

//public class MainActivity extends AppCompatActivity {
//
//
//    private static final int REQUEST_CODE = 101;
//    private FusedLocationProviderClient fusedLocationProviderClient;
//    private LocationRequest locationRequest;
//    Animation topAnim, leftAnim;
//    TextView appName;
//    ImageView logo_image;
//
//    private CustomLocationHandler locationHandler;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//
//        // Initialize the FusedLocationProviderClient
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//
//        // Create a LocationRequest object
//        locationRequest = LocationRequest.create();
//        locationRequest.setInterval(3000);
//        locationRequest.setFastestInterval(1000);
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
////
//        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
//        leftAnim = AnimationUtils.loadAnimation(this, R.anim.left_animation);
//        CustomLocationHandler locationHandler = new CustomLocationHandler(this);
//
//        appName = findViewById(R.id.startScreenTxt);
//        logo_image = (ImageView) findViewById(R.id.logo_image);
//        logo_image.setAnimation(topAnim);
//        appName.setAnimation(leftAnim);
//
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
//        locationHandler = new CustomLocationHandler(this);
//        // Check if GPS is enabled
//        {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    //Do something
//                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
//                }
//            }, 5000);
//
//        }
//
//    }
//
//    private final LocationCallback locationCallback = new LocationCallback() {
//        @Override
//        public void onLocationResult(@NonNull LocationResult locationResult) {
//
//        }
//    };
//
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted, call the callMainActivity() method
//                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
//                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                if (location != null) {
//                    // Get latitude and longitude
//                    double latitude = location.getLatitude();
//                    double longitude = location.getLongitude();
//
//                    // Use latitude and longitude to fetch pincode and address
//                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
//                    try {
//                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
//                        if (addresses.size() > 0) {
//                            String pincode = addresses.get(0).getPostalCode();
//                            String address = addresses.get(0).getAddressLine(0);
//                            // Do something with pincode and address
//                            Intent intent = new Intent(MainActivity.this, LoginDetail.class);
//                            startActivity(intent);
//                            intent.putExtra("Lat", latitude);
//                            intent.putExtra("Longi", longitude);
//                            intent.putExtra("pincode", pincode);
//                            intent.putExtra("address", address);
//                            startActivity(intent);
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    // Check if the app has permission to access the user's location
//                    Toast.makeText(this, "Location permission denied, please grant the permission to continue", Toast.LENGTH_SHORT).show();
//                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
//                }
//            } else {
//                // Permission denied, show a toast message and ask for permission again
//                Toast.makeText(this, "Location permission denied, please grant the permission to continue", Toast.LENGTH_SHORT).show();
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
//            }
//        }
//    }
////    @Override
////    public void onBackPressed() {
////        // Perform your desired action here, such as going back to the previous activity
////        super.onBackPressed();
////        finishAffinity(); // Close the entire application
////    }
//}

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 100;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private Animation topAnim, leftAnim;
    private ImageView logo_image;
    private TextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Create a LocationRequest object
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(3000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        leftAnim = AnimationUtils.loadAnimation(this, R.anim.left_animation);

        appName = findViewById(R.id.startScreenTxt);
        logo_image = findViewById(R.id.logo_image);
        logo_image.setAnimation(topAnim);
        appName.setAnimation(leftAnim);

        // Request location permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            // TODO: Implement code to handle location result
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get the current location
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
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Get latitude and longitude
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                            // Use latitude and longitude to fetch pincode and address
                            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                if (addresses.size() > 0) {
                                    String pincode = addresses.get(0).getPostalCode();
                                    String address = addresses.get(0).getAddressLine(0);
                                    // Do something with pincode and address
                                    Intent intent = new Intent(MainActivity.this, LoginDetail.class);
                                    intent.putExtra("Lat", latitude);
                                    intent.putExtra("Longi", longitude);
                                    intent.putExtra("pincode", pincode);
                                    intent.putExtra("address", address);
                                    startActivity(intent);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            // Location is null, show a toast message and ask for permission again
                            Toast.makeText(MainActivity.this, "Unable to get location, please try again", Toast.LENGTH_SHORT).show();
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                        }
                    }
                });
            } else {
                // Permission denied, show a toast message and ask for permission again
                Toast.makeText(MainActivity.this, "Location permission denied, please grant the permission to continue", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            }
        }
    }


}







