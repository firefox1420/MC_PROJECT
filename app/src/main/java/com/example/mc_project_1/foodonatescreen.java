package com.example.mc_project_1;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.util.Arrays;


public class foodonatescreen extends Fragment {

    ImageView foodphotos , cameraViewForImage, galleryViewForImage;
    private FileReaderWriter rw;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view = inflater.inflate(R.layout.fragment_foodonatescreen, container, false);
        ImageView profilePhoto , cameraViewForImage, galleryViewForImage;
        foodphotos = view.findViewById(R.id.profieScreenimageView4);
        rw = new FileReaderWriter(getActivity().getApplicationContext());
        foodphotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectProfileImageOperation(/*bmv*/);
            }
        });
        return view;
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

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImageUri = data.getData();
                    foodphotos.setImageURI(selectedImageUri);
                    setImageInMenu();
                }

                break;
            case 2:
                if(resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
                    Bitmap bitmapImage = (Bitmap) bundle.get("data");
                    foodphotos.setImageBitmap(bitmapImage);
                    setImageInMenu();
                }
                break;
        }
    }

    private void setImageInMenu(){
        Drawable profilePicDrawable = foodphotos.getDrawable();
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




}