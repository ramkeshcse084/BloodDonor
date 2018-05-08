package com.nestedmango.blooddonor;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateProfileActivity extends AppCompatActivity {
    private EditText name, mobile;
    private TextView userEmail;
    private Button update;
    Spinner age, bloodgroup, city;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    String userName, userCity,userBloodgroup,userAge,userMobileNo,userId,userEmailId,userCondition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        getLayoutId();
        databaseReference = FirebaseDatabase.getInstance().getReference("profile");
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser CurrentUserEmail=firebaseAuth.getCurrentUser();
        userEmail.setText(CurrentUserEmail.getEmail());
        userEmailId = CurrentUserEmail.getEmail();
        setOnClickListener();
    }

    public void getLayoutId() {
        name = (EditText) findViewById(R.id.nameET);
        mobile = (EditText) findViewById(R.id.mobileNoET);
        update = (Button) findViewById(R.id.updateBT);
        age = (Spinner) findViewById(R.id.ageSpinner);
        bloodgroup = (Spinner) findViewById(R.id.bloodgroupSpinner);
        city = (Spinner) findViewById(R.id.citySpinner);
        userEmail = (TextView) findViewById(R.id.userEmailTV);
    }

    private boolean validate() {
        if (name.getText().toString().trim().equals("")) {
            name.setError("Enter Name");
            name.requestFocus();
            return false;
        } else if (mobile.getText().toString().equals("")) {
            mobile.setError("Enter Mobile No");
            mobile.requestFocus();
            return false;
        } else if (mobile.getText().toString().length() == 9) {
            mobile.setError("Enter 10 digit Mobile No");
            mobile.requestFocus();
            return false;
        } else if (city.getSelectedItem().toString().trim().equals("Select District")) {
            Toast.makeText(getApplicationContext(), "Select your district", Toast.LENGTH_SHORT).show();
            return false;
        } else if (age.getSelectedItem().toString().trim().equals("Select your Age")) {
            Toast.makeText(getApplicationContext(), "Select your age", Toast.LENGTH_SHORT).show();
            return false;
        } else if (bloodgroup.getSelectedItem().toString().trim().equals("Select your blood group")) {
            Toast.makeText(getApplicationContext(), "Select your blood group", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void update() {
        userName = name.getText().toString().trim();
        userAge = age.getSelectedItem().toString().trim();
        userBloodgroup = bloodgroup.getSelectedItem().toString().trim();
        userCity = city.getSelectedItem().toString().trim();
        userMobileNo = mobile.getText().toString().trim();
        userId = databaseReference.push().getKey();
        userCondition = userBloodgroup.concat(userCity);
        UserProfile userProfile = new UserProfile(userAge,userBloodgroup,userCity,userId,userMobileNo,userName,userEmailId,userCondition);
        databaseReference.child(userId).setValue(userProfile);
       Toast.makeText(getApplicationContext(),"User Profile Update Sucessfully",Toast.LENGTH_SHORT).show();
       Intent intent = new Intent(UpdateProfileActivity.this,HomeActivity.class);
       startActivity(intent);

    }

    private void setOnClickListener() {
        android.view.View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id)
                {
                    case R.id.updateBT:
                        Log.d("UpdateProfileActivity","updateBT clicked enter()");
                        if(validate() == true){
                            if(isNetworkAvailable())
                            {
                                update();

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Network not available",Toast.LENGTH_LONG).show();
                            }
                        }
                }
            }
        };
        update.setOnClickListener(onClickListener);

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        Log.d("LoginActivity","isNetworkAvailable");
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
