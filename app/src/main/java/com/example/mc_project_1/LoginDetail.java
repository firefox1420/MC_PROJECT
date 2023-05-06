package com.example.mc_project_1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginDetail extends AppCompatActivity {


    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    Button googleBtn;

    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    FirebaseUser mUser;

    FirebaseDatabase db;
    DatabaseReference reference;

    String pincode;
    String address;
    double latitude;
    double longitude;
    private CustomLocationHandler locationHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_detail);

        Intent intent = getIntent();
        pincode = intent.getStringExtra("pincode");
        address = intent.getStringExtra("address");
         latitude=intent.getDoubleExtra("Lat",0.0);
       longitude =intent.getDoubleExtra("Longi",0.0);

        CustomLocationHandler locationHandler = new CustomLocationHandler(this);


        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);


        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=mGoogleSignInClient.getSignInIntent();
                startActivityForResult(i,1000);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
                String username = account.getDisplayName();
                String emailid=account.getEmail();
                SharedPreferences sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("emailid", emailid);
                editor.apply();
                navigateToSecondActivity();
                storeindatabase(emailid,username);
            } catch (ApiException e) {
                Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }


    void navigateToSecondActivity(){
        finish();
        Intent intent = new Intent(LoginDetail.this, HomeActivity.class);
        startActivity(intent);
//        signOut();
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAGsigninsuccess", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginDetail.this, "Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginDetail.this, "Fail", Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user.
                            Log.w("TAGsoigninfail", "signInWithCredential:failure", task.getException());
                        }

                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(LoginDetail.this, HomeActivity.class);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (currentUser!=null && account!=null ){
            Intent intent = new Intent(LoginDetail.this,HomeActivity.class);
            SharedPreferences sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
            String emailid = sharedPref.getString("emailid", null);
            intent.putExtra("emailid", emailid);
            startActivity(intent);
            finish();
        }
    }

    void storeindatabase(String emailid, String username)
    {
        String email = emailid.substring(0, emailid.indexOf("@"));
        FoodDonateDatabse foodDonateDatabse= new FoodDonateDatabse(username, email, "", "", "", "", "", "");
        FoodManageDatabase foodManageDatabase=new FoodManageDatabase(username, email, "", "", "", "", "", "", "");
        ComplaintDatabase complaintDatabase=new ComplaintDatabase( username, email, "", "");
        String devicetoken = "";
        AnnaDatabase annaDatabase=new AnnaDatabase(devicetoken,emailid,username,"",address,pincode,latitude,longitude,foodManageDatabase,foodDonateDatabse,complaintDatabase,false);

        db=FirebaseDatabase.getInstance();
        reference=db.getReference("User");

        reference.child(email).setValue(annaDatabase).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(LoginDetail.this, "Success In Database", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        // Perform your desired action here, such as going back to the previous activity
//        super.onBackPressed();
//        finishAffinity(); // Close the entire application
//    }
}

