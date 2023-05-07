package com.example.mc_project_1;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class complaintbox extends Fragment {

    TextInputLayout regName, regUsername, regEmail, regPhoneNo;
    TextInputEditText regComplaint;
    Button regBtn, regToLoginBtn;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    private final int CAMERA_REQ_CODE = 100;
    ImageView img;

    private LinearLayout dropdownOptionsLayout;
    private ImageView dropdownArrow;
    private TextView mainHeading;


    private LinearLayout mainLayout3;
    private TextView mainHeading3;
    private ImageView dropdownArrow3;
    private LinearLayout dropdownOptions3;
    private LinearLayout option4;
    private EditText locationInput;
    private Button addLocationButton;

    TextInputEditText t1,t2;

    EditText t3;

    String loc;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_complaintbox, container, false);
        regUsername = view.findViewById(R.id.reg_username);
        regEmail = view.findViewById(R.id.reg_email);
        regPhoneNo = view.findViewById(R.id.reg_phoneNo);
        regBtn = view.findViewById(R.id.reg_btn);
        regComplaint = view.findViewById(R.id.reg_complaint);
        t1=view.findViewById(R.id.username3);
        t2=view.findViewById(R.id.email3);
        t3=view.findViewById(R.id.location_input3);
        String location;
        SharedPreferences sharedPref = getActivity().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        String emailid = sharedPref.getString("emailid", null);
        t2.setText(""+emailid);
        t3=view.findViewById(R.id.location_input3);
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


        // Initializing views
        mainLayout3 = view.findViewById(R.id.main_layout3);
        mainHeading3= view.findViewById(R.id.main_heading3);
        dropdownArrow3 = view.findViewById(R.id.dropdown_arrow3);
        dropdownOptions3 = view.findViewById(R.id.dropdown_options3);
        option4 = view.findViewById(R.id.option_4);
        locationInput = view.findViewById(R.id.location_input3);
        addLocationButton = view.findViewById(R.id.add_location_button);



        //Save data in FireBase on button click
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootNode = FirebaseDatabase.getInstance();
                String email1 = emailid.substring(0, emailid.indexOf("@"));// will get https://testdbapp-6ad34-default-rtdb.firebaseio.com/
                reference = rootNode.getReference("User").child(email1);

                String username = regUsername.getEditText().getText().toString();
                String email = regEmail.getEditText().getText().toString();
                String phoneNo = regPhoneNo.getEditText().getText().toString();
                String complaint = regComplaint.getText().toString();
                ComplaintDatabase helperClass = new ComplaintDatabase(username, email, phoneNo, complaint);
                reference.child("complaintDatabase").setValue(helperClass);
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
                    // Do something with the entered location
                    Toast.makeText(getActivity(), "Location added: " + location, Toast.LENGTH_SHORT).show();
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
                Bitmap image = (Bitmap)(data.getExtras().get("data"));
                img.setImageBitmap(image);
            }
        }
    }


}