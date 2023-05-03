package com.example.mc_project_1;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.view.View.GONE;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.provider.MediaStore;
import android.service.autofill.UserData;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.File;


public class ProfileScreen extends Fragment {

    ImageView profilePhoto , cameraViewForImage, galleryViewForImage;
    //BottomNavigationView bmv;
    MeowBottomNavigation menuTwo;
    String currentUserImageName;
    private String mParam1;
    private String mParam2;
    String userID = "123";

    private int requestCode;
    private String[] permissions;
    private int[] grantResults;
    private SharedPreferences sharedPreferences;
    private CustomLocationHandler locationHandler;
    private LocationRequest locationRequest;
    private ProgressBar progressBar;
    private String uid;
    private FileReaderWriter rw;
    TextView profileScreenUserName, profileScreenContactNumber, profileScreenBloodGroup, profileScreenAddress,profileScreenPinCode,profileScreenMainName;

    private DatabaseReference mDatabase;

    Button editDetailsButton,signOutButton;
    String username="",address="",pincode="",contactno = "";

    LocationManager locationManager;
    LocationListener locationListener;
    public ProfileScreen() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v =  inflater.inflate(R.layout.fragment_profile_screen, container, false);

        //setting profile photo
        profilePhoto = v.findViewById(R.id.profieScreenimageView3);
        rw = new FileReaderWriter(getActivity().getApplicationContext());
        profileScreenUserName = v.findViewById(R.id.profileScreenEditText1);
        profileScreenContactNumber =v.findViewById(R.id.profileScreenEditText2);
        profileScreenAddress = v.findViewById(R.id.profileScreenEditText4);
        profileScreenPinCode= v.findViewById(R.id.profileScreenEditText5);
        editDetailsButton=v.findViewById(R.id.profileScreenEditDetailButton);

        username  = profileScreenUserName.getText().toString();
        address = profileScreenAddress.getText().toString();
        contactno = profileScreenContactNumber.getText().toString();
        pincode = profileScreenPinCode.getText().toString();


        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectProfileImageOperation(/*bmv*/);
            }
        });

        if(editDetailsButton.getText().toString().equals("save")){
            activeDeactiveDetails(true);

        }
        else{
            activeDeactiveDetails(false);

        }
        fillProfile();

        editDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editDetailsButton.getText().toString().equals("save")){
                    writeUserData(username,address,contactno,pincode);
                    activeDeactiveDetails(false);
                    editDetailsButton.setText("Edit Details");
                }
                else{
                    activeDeactiveDetails(true);
                    editDetailsButton.setText("save");
                    //saveDetails();
                }
            }
        });


        return v;
    }

    private void fillProfile() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get UserData object and use the values to update the TextViews
                UserDetails userData = dataSnapshot.getValue(UserDetails.class);
//                profileScreenMainName.setText(userData.UserName);
//                profileScreenContactNumber.setText(userData.ContactNo);
//                profileScreenAddress.setText(userData.Address);
//                profileScreenPinCode.setText(userData.pincode);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting UserData failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.child("users").child(userID).addValueEventListener(postListener);
    }


    private void activeDeactiveDetails(Boolean action){
        profileScreenAddress.setEnabled(action);
        profileScreenUserName.setEnabled(action);
        profileScreenContactNumber.setEnabled(action);
        profileScreenPinCode.setEnabled(action);
    }
    private void selectProfileImageOperation(/*BottomNavigationView bmv*/){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_profile_picture, null);
        builder.setCancelable(true);
        builder.setView(dialogView);

//        cameraViewForImage = dialogView.findViewById(R.id.imageViewADPPCamera);
        galleryViewForImage = dialogView.findViewById(R.id.imageViewADPPGallery);

        final AlertDialog alertDialogProfilePicture = builder.create();

        alertDialogProfilePicture.show();

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 20);

//        cameraViewForImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(checkAndRequestPermissions()) {
//                    takePictureFromCamera();
//                    alertDialogProfilePicture.dismiss();
//                }
//            }
//        });

        galleryViewForImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePictureFromGallery();
                alertDialogProfilePicture.dismiss();
            }
        });
    }



    private void takePictureFromGallery(){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
    }

    private void takePictureFromCamera(){
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePicture.resolveActivity(getActivity().getPackageManager()) != null){
            startActivityForResult(takePicture, 2);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImageUri = data.getData();
                    profilePhoto.setImageURI(selectedImageUri);
                    setImageInMenu();
                }

                break;
            case 2:
                if(resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
                    Bitmap bitmapImage = (Bitmap) bundle.get("data");
                    profilePhoto.setImageBitmap(bitmapImage);
                    setImageInMenu();
                }
                break;
        }
    }

    private boolean checkAndRequestPermissions(){
        int cameraPermission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if(cameraPermission == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 20);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 20 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            takePictureFromCamera();
        }
        else {
            Toast.makeText(getContext(), "Camera Permission not Granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void setImageInMenu(){
        Drawable profilePicDrawable = profilePhoto.getDrawable();
        Bitmap bitmap = ((BitmapDrawable)profilePicDrawable).getBitmap();
        rw.saveToInternalStorage(bitmap);
        File image = rw.loadImageFromStorage();
        Glide.with(this)
                .asBitmap()
                .load(image).apply(RequestOptions.circleCropTransform())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Drawable d = new BitmapDrawable(getResources(), resource);
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder){
                    }});
    }

    private void writeUserData(String field1, String field2, String field3,String field4) {
        String userId = mDatabase.push().getKey();
        UserDetails userData = new UserDetails(field1, field2, field3,field4);
        assert userId != null;
        mDatabase.child("users").child(userId).setValue(userData);

    }

}