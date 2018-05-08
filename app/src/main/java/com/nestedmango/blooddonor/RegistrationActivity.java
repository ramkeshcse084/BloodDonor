package com.nestedmango.blooddonor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText registerMailET, registerPasswordET, regConfermPasswordET;
    private Button registerBT;
    private String TAG = "RegistrationActivity";
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    String email, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getLayoutId();
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
        }
        registerBT.setOnClickListener(this);
    }

    private void getLayoutId() {
        registerMailET = (EditText) findViewById(R.id.registerMailET);
        registerPasswordET = (EditText) findViewById(R.id.registerPasswordET);
        regConfermPasswordET = (EditText) findViewById(R.id.regConfermPasswordET);
        registerBT = (Button) findViewById(R.id.registerBT);
        progressDialog = new ProgressDialog(this);

    }

    private void register() {
        Log.d(TAG, "register enter()");
        email = registerMailET.getText().toString().trim();
        password = registerPasswordET.getText().toString().trim();
        progressDialog.setMessage("Registering User...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    //finish();
                    startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
                } else {
                    Log.e(TAG, "Sign-in Failed: " + task.getException().getMessage());
                    Toast.makeText(RegistrationActivity.this, "Registration Fails", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.d(TAG, "register exit()");
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerBT:
                if (validate() == true){
                    if(isNetworkAvailable()) {
                        register();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Network not available",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    private boolean validate() {
        String password = registerPasswordET.getText().toString();
        String confermpassword = regConfermPasswordET.getText().toString();
        if (registerMailET.getText().toString().equals("")) {
            registerMailET.setError("Enter mail");
            registerMailET.requestFocus();
            return false;
        } else if (registerPasswordET.getText().toString().equals("")){
            registerPasswordET.setError("Enter password");
            registerPasswordET.requestFocus();
            return false;
        }
          else if (password.length() < 6) {
                Toast.makeText(getApplicationContext(), "password should be maximum 6 character", Toast.LENGTH_LONG).show();
                registerMailET.requestFocus();
                return false;
            }
            else if (regConfermPasswordET.getText().toString().equals("")) {
            regConfermPasswordET.setError("Enter password");
            regConfermPasswordET.requestFocus();
            return false;
        }
        else if(!confermpassword.equals(password))
        {
            Toast.makeText(getApplicationContext(), "password doesn't match", Toast.LENGTH_LONG).show();
            regConfermPasswordET.requestFocus();
            return false;
        }
       // Toast.makeText(getApplicationContext(),"Enter correct mail",Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

