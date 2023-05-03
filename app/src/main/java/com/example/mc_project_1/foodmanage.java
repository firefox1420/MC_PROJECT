package com.example.mc_project_1;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link foodmanage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class foodmanage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public foodmanage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment foodmanage.
     */
    // TODO: Rename and change types and number of parameters
    public static foodmanage newInstance(String param1, String param2) {
        foodmanage fragment = new foodmanage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    ImageView dustphotos , cameraViewForImage, galleryViewForImage;
    private FileReaderWriter rw;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_foodmanage, container, false);
        ImageView profilePhoto , cameraViewForImage, galleryViewForImage;
        dustphotos = view.findViewById(R.id.profieScreenimageView4);
        rw = new FileReaderWriter(getActivity().getApplicationContext());
        dustphotos.setOnClickListener(new View.OnClickListener() {
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
                    dustphotos.setImageURI(selectedImageUri);
                    setImageInMenu();
                }

                break;
            case 2:
                if(resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
                    Bitmap bitmapImage = (Bitmap) bundle.get("data");
                    dustphotos.setImageBitmap(bitmapImage);
                    setImageInMenu();
                }
                break;
        }
    }

    private void setImageInMenu(){
        Drawable profilePicDrawable = dustphotos.getDrawable();
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