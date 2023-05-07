package com.example.mc_project_1;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class foodonatescreen extends Fragment {

    public static  final String SERVER_KEY = "AAAAKtZbCWQ:APA91bFX9a_3YBdQ9oUj1GcKdKYF2soRD0j47LQrZXsmbKoHrPrP5dTbDWDX4B3TYchXSOzdOMwZC1v-3XFObhfES1_-_B5D4FPgOm0FxsphDSUPAKQjwYLO-kEjhCU7LUv3To0cMjNX";
    private static final String FCM_API_URL = "https://fcm.googleapis.com/fcm/send";

    TextInputLayout regName, regUsername, regEmail, regPhoneNo, regPassword;
    Button regBtn, regToLoginBtn;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    private final int CAMERA_REQ_CODE = 100;
    ImageView img;

    private LinearLayout dropdownOptionsLayout, dropdownOptionsLayout2, dropdownOptionsLayout4, dropdownOptionsLayout5, dropdownOptionsLayout6;
    private ImageView dropdownArrow, dropdownArrow2, dropdownArrow4, dropdownArrow5, dropdownArrow6;
    private TextView mainHeading, mainHeading2, mainHeading4, mainHeading5, mainHeading6;


    private LinearLayout mainLayout3;
    private TextView mainHeading3;
    private ImageView dropdownArrow3;
    private LinearLayout dropdownOptions3;
    String food_donate_image_url = null;

    StorageReference storageReference;
    ProgressDialog progressDialog;

    private LinearLayout option4;
    private EditText locationInput;
    private Button addLocationButton;

    TextInputEditText t1,t2;
    String  Devicetoken = null;

    EditText t3;

    String loc;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view = inflater.inflate(R.layout.fragment_foodonatescreen, container, false);
        regUsername = view.findViewById(R.id.reg_username);
        regEmail = view.findViewById(R.id.reg_email);
        regPhoneNo = view.findViewById(R.id.reg_phoneNo);
        regBtn = view.findViewById(R.id.reg_btn);
        t1=view.findViewById(R.id.username1);
        t2=view.findViewById(R.id.email);
        String location;
        SharedPreferences sharedPref = getActivity().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        String emailid = sharedPref.getString("emailid", null);
        t2.setText(""+emailid);
        t3=view.findViewById(R.id.location_input);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("User");
        String targetEmail = emailid;
        Toast.makeText(getActivity(), ""+targetEmail, Toast.LENGTH_SHORT).show();

        Query query = usersRef.orderByChild("emailid").equalTo(targetEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String name = userSnapshot.child("name").getValue(String.class);
                    t1.setText(""+name);
                    String email = userSnapshot.child("emailid").getValue(String.class);
                    String location = userSnapshot.child("address").getValue(String.class);
                     Devicetoken = userSnapshot.child("devicetoken").getValue(String.class);
                    t3.setText(location);
                    Log.d("Detials", "Name: " + name + ", Email: " + email + ", Location: " + location);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", "Error fetching data: " + databaseError.getMessage());
            }
        });

        img = view.findViewById(R.id.imageView);
        Button btn = view.findViewById(R.id.cameraButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(iCamera, CAMERA_REQ_CODE);
            }
        });


        // Get references to layout elements
        dropdownOptionsLayout = view.findViewById(R.id.dropdown_options);
        dropdownArrow = view.findViewById(R.id.dropdown_arrow);
        mainHeading = view.findViewById(R.id.main_heading);

        dropdownOptionsLayout2 = view.findViewById(R.id.dropdown_options2);
        dropdownArrow2 = view.findViewById(R.id.dropdown_arrow2);
        mainHeading2 = view.findViewById(R.id.main_heading2);

        dropdownOptionsLayout4 = view.findViewById(R.id.dropdown_options_type);
        dropdownArrow4 = view.findViewById(R.id.dropdown_arrow_type);
        mainHeading4 = view.findViewById(R.id.main_heading_type);

        dropdownOptionsLayout5 = view.findViewById(R.id.dropdown_options_bb);
        dropdownArrow5 = view.findViewById(R.id.dropdown_arrow_bb);
        mainHeading5 = view.findViewById(R.id.main_heading_bb);

        dropdownOptionsLayout6 = view.findViewById(R.id.dropdown_options_type_veg);      // done-2
        dropdownArrow6 = view.findViewById(R.id.dropdown_arrow_type_veg);
        mainHeading6 = view.findViewById(R.id.main_heading_type_veg);



        // Initializing views
        mainLayout3 = view.findViewById(R.id.main_layout3);
        mainHeading3= view.findViewById(R.id.main_heading3);
        dropdownArrow3 = view.findViewById(R.id.dropdown_arrow3);
        dropdownOptions3 = view.findViewById(R.id.dropdown_options3);
        option4 = view.findViewById(R.id.option_4);
        locationInput = view.findViewById(R.id.location_input);
        addLocationButton = view.findViewById(R.id.add_location_button);







        // Set click listener for main heading layout
        view.findViewById(R.id.main_layout_type_veg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dropdownOptionsLayout6.getVisibility() == View.GONE) {
                    // Show dropdown options with animation
                    dropdownOptionsLayout6.setVisibility(View.VISIBLE);
                    dropdownOptionsLayout6.animate()
                            .alpha(1.0f)
                            .translationY(0)
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .setDuration(500);
                    rotateArrow6(180);
                } else {
                    // Hide dropdown options with animation
                    dropdownOptionsLayout6.animate()
                            .alpha(0.0f)
                            .translationY(-dropdownOptionsLayout6.getHeight())
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .setDuration(500)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    dropdownOptionsLayout6.setVisibility(View.GONE);
                                }
                            });
                    rotateArrow6(0);
                }
            }
        });

        // Set click listeners for dropdown options
        view.findViewById(R.id.option_1_veg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainHeading6.setText("Type: VEG");
                hideDropdownOptions6();
            }
        });

        view.findViewById(R.id.option_2_veg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainHeading6.setText("Type: NON-VEG");
                hideDropdownOptions6();
            }
        });







        // Set click listener for main heading layout
        view.findViewById(R.id.main_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dropdownOptionsLayout.getVisibility() == View.GONE) {
                    // Show dropdown options with animation
                    dropdownOptionsLayout.setVisibility(View.VISIBLE);
                    dropdownOptionsLayout.animate()
                            .alpha(1.0f)
                            .translationY(0)
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .setDuration(500);
                    rotateArrow(180);
                } else {
                    // Hide dropdown options with animation
                    dropdownOptionsLayout.animate()
                            .alpha(0.0f)
                            .translationY(-dropdownOptionsLayout.getHeight())
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .setDuration(500)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    dropdownOptionsLayout.setVisibility(View.GONE);
                                }
                            });
                    rotateArrow(0);
                }
            }
        });

        // Set click listeners for dropdown options
        view.findViewById(R.id.option_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainHeading.setText("Serves: 1 < 5");
                hideDropdownOptions();
            }
        });

        view.findViewById(R.id.option_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainHeading.setText("Serves: 5  < 20");
                hideDropdownOptions();
            }
        });

        view.findViewById(R.id.option_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainHeading.setText("Serves: 20 < 50");
                hideDropdownOptions();
            }
        });

        view.findViewById(R.id.option_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainHeading.setText("Serves: 50  < 100");
                hideDropdownOptions();
            }
        });

        view.findViewById(R.id.option_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainHeading.setText("Serves: 100 < 500");
                hideDropdownOptions();
            }
        });





        // Set click listener for main heading layout
        view.findViewById(R.id.main_layout2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dropdownOptionsLayout2.getVisibility() == View.GONE) {
                    // Show dropdown options with animation
                    dropdownOptionsLayout2.setVisibility(View.VISIBLE);
                    dropdownOptionsLayout2.animate()
                            .alpha(1.0f)
                            .translationY(0)
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .setDuration(500);
                    rotateArrow2(180);
                } else {
                    // Hide dropdown options with animation
                    dropdownOptionsLayout2.animate()
                            .alpha(0.0f)
                            .translationY(-dropdownOptionsLayout2.getHeight())
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .setDuration(500)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    dropdownOptionsLayout2.setVisibility(View.GONE);
                                }
                            });
                    rotateArrow2(0);
                }
            }
        });

        // Set click listeners for dropdown options
        view.findViewById(R.id.option_1_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainHeading2.setText("Time: 11am to 3pm");
                hideDropdownOptions2();
            }
        });

        view.findViewById(R.id.option_2_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainHeading2.setText("Time: 12pm to 8pm");
                hideDropdownOptions2();
            }
        });




        // Set click listener for main heading layout
        view.findViewById(R.id.main_layout_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dropdownOptionsLayout4.getVisibility() == View.GONE) {
                    // Show dropdown options with animation
                    dropdownOptionsLayout4.setVisibility(View.VISIBLE);
                    dropdownOptionsLayout4.animate()
                            .alpha(1.0f)
                            .translationY(0)
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .setDuration(500);
                    rotateArrow4(180);
                } else {
                    // Hide dropdown options with animation
                    dropdownOptionsLayout4.animate()
                            .alpha(0.0f)
                            .translationY(-dropdownOptionsLayout4.getHeight())
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .setDuration(500)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    dropdownOptionsLayout4.setVisibility(View.GONE);
                                }
                            });
                    rotateArrow4(0);
                }
            }
        });

        // Set click listeners for dropdown options
        view.findViewById(R.id.option_1_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainHeading4.setText("Type: Perishable");
                hideDropdownOptions4();
            }
        });

        view.findViewById(R.id.option_2_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainHeading4.setText("Type: Non-Perishable");
                hideDropdownOptions4();
            }
        });

        view.findViewById(R.id.option_3_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainHeading4.setText("Type: Canned");
                hideDropdownOptions4();
            }
        });

        view.findViewById(R.id.option_4_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainHeading4.setText("Type: Fresh");
                hideDropdownOptions4();
            }
        });





        // Set click listener for main heading layout
        view.findViewById(R.id.main_layout_bb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dropdownOptionsLayout5.getVisibility() == View.GONE) {
                    // Show dropdown options with animation
                    dropdownOptionsLayout5.setVisibility(View.VISIBLE);
                    dropdownOptionsLayout5.animate()
                            .alpha(1.0f)
                            .translationY(0)
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .setDuration(500);
                    rotateArrow5(180);
                } else {
                    // Hide dropdown options with animation
                    dropdownOptionsLayout5.animate()
                            .alpha(0.0f)
                            .translationY(-dropdownOptionsLayout5.getHeight())
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .setDuration(500)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    dropdownOptionsLayout5.setVisibility(View.GONE);
                                }
                            });
                    rotateArrow5(0);
                }
            }
        });

        // Set click listeners for dropdown options
        view.findViewById(R.id.option_1_bb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainHeading5.setText("Best before: 3 months");
                hideDropdownOptions5();
            }
        });

        view.findViewById(R.id.option_2_bb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainHeading5.setText("Best before: 6 months");
                hideDropdownOptions5();
            }
        });

        view.findViewById(R.id.option_3_bb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainHeading5.setText("Best before: 12 months");
                hideDropdownOptions5();
            }
        });

        view.findViewById(R.id.option_4_bb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainHeading5.setText("Best before: >12 months");
                hideDropdownOptions5();
            }
        });








        //Save data in FireBase on button click
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootNode = FirebaseDatabase.getInstance();
                String email1 = emailid.substring(0, emailid.indexOf("@"));// will get https://testdbapp-6ad34-default-rtdb.firebaseio.com/
                reference = rootNode.getReference("User").child(email1);
//                reference.setValue("First data stored");
                //Get all the values
                String username = regUsername.getEditText().getText().toString();
                String email = regEmail.getEditText().getText().toString();
                String phoneNo = regPhoneNo.getEditText().getText().toString();
                String serves = mainHeading.getText().toString();
                String time = mainHeading2.getText().toString();
                String type = mainHeading4.getText().toString();
                String best_bef = mainHeading5.getText().toString();
                String typeVeg = mainHeading6.getText().toString();
                FoodDonateDatabse helperClass = new FoodDonateDatabse(username, email, phoneNo, serves, time, type, best_bef, typeVeg,food_donate_image_url);
                Toast.makeText(getActivity(), ""+String.valueOf(helperClass), Toast.LENGTH_SHORT).show();
                reference.child("foodDonateDatabse").setValue(helperClass);



                //when a user request food donate to admin
                // Send notification message-> "food donate request"
                String message = "food donate request";
                String title = "user";
                sendNotification(Devicetoken, title, message);



//                Query query = usersRef.orderByChild("emailid").equalTo(targetEmail);
//                query.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
//                            String name = userSnapshot.child("name").getValue(String.class);
//                            t1.setText(""+name);
//                            String email = userSnapshot.child("emailid").getValue(String.class);
//                            String location = userSnapshot.child("address").getValue(String.class);
//                            t3.setText(location);
//                            Log.d("Detials", "Name: " + name + ", Email: " + email + ", Location: " + location);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Log.e("TAG", "Error fetching data: " + databaseError.getMessage());
//                    }
//                });
            }
        });//Register Button method end




        // Adding click listener to main layout
        mainLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dropdownOptions3.getVisibility() == View.VISIBLE) {
                    dropdownOptions3.setVisibility(View.GONE);
                    dropdownArrow3.setImageResource(R.drawable.down);
                } else {
                    dropdownOptions3.setVisibility(View.VISIBLE);
                    dropdownArrow3.setImageResource(R.drawable.up);
                }
            }
        });

        // Adding click listener to add location button
        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = locationInput.getText().toString().trim();
                if (!location.isEmpty()) {

                }
                locationInput.setText("");
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == CAMERA_REQ_CODE){

//                Bitmap image = (Bitmap)(data.getExtras().get("data"));

                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG,90,bytes);
                byte bb[] = bytes.toByteArray();
                uploadImage(bb);
                //if user has requested then only it should display food donate image
                //                img.setImageBitmap(image);

            }
        }
    }
    private void uploadImage(byte[] image_data) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading File....");
        progressDialog.show();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now = new Date();
        String fileName = formatter.format(now);
        storageReference = FirebaseStorage.getInstance().getReference("images/"+fileName);

        storageReference.putBytes(image_data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();

                        // Get the download URL
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
//                                 Set the image view with the downloaded URL
                                Glide.with(getContext())
                                        .load(uri)
                                        .into(img);
                                 food_donate_image_url = String.valueOf(uri);
//                                Log.e("ggggg", String.valueOf(uri));
                                Toast.makeText(getContext(),"Successfully Uploaded",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        Toast.makeText(getContext(),"Failed to Upload",Toast.LENGTH_SHORT).show();
                    }
                });
    }





    // Helper method to hide dropdown options with animation
    private void hideDropdownOptions() {
        dropdownOptionsLayout.animate()
                .alpha(0.0f)
                .translationY(-dropdownOptionsLayout.getHeight())
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(500)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        dropdownOptionsLayout.setVisibility(View.GONE);
                    }
                });
        rotateArrow(0);
    }

    // Helper method to rotate arrow image
    private void rotateArrow(float angle) {
        RotateAnimation animation = new RotateAnimation(
                0, angle, dropdownArrow.getWidth()/2, dropdownArrow.getHeight()/2);
        animation.setDuration(500);
        animation.setFillAfter(true);
        dropdownArrow.startAnimation(animation);
    }

    // Helper method to hide dropdown options with animation
    private void hideDropdownOptions2() {
        dropdownOptionsLayout2.animate()
                .alpha(0.0f)
                .translationY(-dropdownOptionsLayout2.getHeight())
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(500)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        dropdownOptionsLayout2.setVisibility(View.GONE);
                    }
                });
        rotateArrow2(0);
    }

    // Helper method to rotate arrow image
    private void rotateArrow2(float angle) {
        RotateAnimation animation = new RotateAnimation(
                0, angle, dropdownArrow2.getWidth()/2, dropdownArrow2.getHeight()/2);
        animation.setDuration(500);
        animation.setFillAfter(true);
        dropdownArrow2.startAnimation(animation);
    }






    // Helper method to hide dropdown options with animation
    private void hideDropdownOptions4() {
        dropdownOptionsLayout4.animate()
                .alpha(0.0f)
                .translationY(-dropdownOptionsLayout4.getHeight())
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(500)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        dropdownOptionsLayout4.setVisibility(View.GONE);
                    }
                });
        rotateArrow4(0);
    }

    // Helper method to rotate arrow image
    private void rotateArrow4(float angle) {
        RotateAnimation animation = new RotateAnimation(
                0, angle, dropdownArrow4.getWidth()/2, dropdownArrow4.getHeight()/2);
        animation.setDuration(500);
        animation.setFillAfter(true);
        dropdownArrow4.startAnimation(animation);
    }






    // Helper method to hide dropdown options with animation
    private void hideDropdownOptions5() {
        dropdownOptionsLayout5.animate()
                .alpha(0.0f)
                .translationY(-dropdownOptionsLayout5.getHeight())
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(500)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        dropdownOptionsLayout5.setVisibility(View.GONE);
                    }
                });
        rotateArrow5(0);
    }

    // Helper method to rotate arrow image
    private void rotateArrow5(float angle) {
        RotateAnimation animation = new RotateAnimation(
                0, angle, dropdownArrow5.getWidth()/2, dropdownArrow5.getHeight()/2);
        animation.setDuration(500);
        animation.setFillAfter(true);
        dropdownArrow5.startAnimation(animation);
    }






    // Helper method to hide dropdown options with animation
    private void hideDropdownOptions6() {
        dropdownOptionsLayout6.animate()
                .alpha(0.0f)
                .translationY(-dropdownOptionsLayout6.getHeight())
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(500)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        dropdownOptionsLayout6.setVisibility(View.GONE);
                    }
                });
        rotateArrow6(0);
    }

    // Helper method to rotate arrow image
    private void rotateArrow6(float angle) {
        RotateAnimation animation = new RotateAnimation(
                0, angle, dropdownArrow6.getWidth()/2, dropdownArrow6.getHeight()/2);
        animation.setDuration(500);
        animation.setFillAfter(true);
        dropdownArrow6.startAnimation(animation);
    }


    private void sendNotification(String token, String title, String message) {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        // Construct JSON body for POST request
        JSONObject notificationBody = new JSONObject();
        try {
            notificationBody.put("to", token);

            JSONObject data = new JSONObject();
            data.put("title", title);
            data.put("message", message);

            notificationBody.put("data", data);
        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON body", e);
        }

        // Construct POST request
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                FCM_API_URL,
                notificationBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "Notification sent successfully: " + response);
                        Toast.makeText(getContext(), "Notification sent successfully", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorResponse = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        Log.e(TAG, "Error sending notification: " + errorResponse, error);
                        Toast.makeText(getContext(), "Error sending notification: " + errorResponse, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "key=" + SERVER_KEY);
                return headers;
            }
        };

        // Add POST request to Volley queue
        queue.add(request);
    }

}