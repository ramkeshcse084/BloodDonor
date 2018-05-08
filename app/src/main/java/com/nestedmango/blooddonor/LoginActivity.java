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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText loginEmailET = null;
    private EditText loginPassword = null;
    private Button loginBtn = null;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    String email ,passsword = null;
    private TextView registerLinkTV =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() !=null)
        {
            finish();
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
        }
        getLayout();
        loginBtn.setOnClickListener(this);
        registerLinkTV.setOnClickListener(this);
        }

    private void getLayout()
    {
        loginEmailET =(EditText)findViewById(R.id.loginMailET);
        loginPassword =(EditText)findViewById(R.id.loginPasswordET);
        loginBtn = (Button)findViewById(R.id.loginBT);
        registerLinkTV =(TextView)findViewById(R.id.registerLinkTV);
        progressDialog = new ProgressDialog(this);


    }
    public void onClick(View v)
    {
     switch(v.getId())
        {
            case R.id.loginBT:
                if(validate() == true) {
                    if (isNetworkAvailable()) {
                        login();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Network not available",Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.registerLinkTV:
                Intent intent = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(intent);
                break;


        }
    }
    private boolean validate()
    {
        if (loginEmailET.getText().toString().equals("")) {
            loginEmailET.setError("Enter mail");
            loginEmailET.requestFocus();
            return false;
        } else if (loginPassword.getText().toString().equals("")) {
            loginPassword.setError("Enter password");
            loginPassword.requestFocus();
            return false;
        }
        return true;
    }
    private void login()
    {
        Log.d("LoginActivity","login enter()");
        email = loginEmailET.getText().toString().trim();
        passsword = loginPassword.getText().toString().trim();
        progressDialog.setMessage("Checking Credentials...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,passsword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful())
                {
                    finish();
                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"Wrong Credentials",Toast.LENGTH_SHORT).show();
                    loginEmailET.setText("");
                    loginPassword.setText("");
                    loginEmailET.requestFocus();
                }

            }
        });
        Log.d("LoginActivity","login exit()");

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        Log.d("LoginActivity","isNetworkAvailable");
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
