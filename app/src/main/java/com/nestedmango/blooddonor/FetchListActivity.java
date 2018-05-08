package com.nestedmango.blooddonor;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FetchListActivity extends AppCompatActivity {

    private Spinner fetchBloodgroup,fetchCity;
    private Button searchBT;
    private ListView searchLV;
    private static  final  String TAG = "FetchListActivity";
    private FirebaseListAdapter listAdapter;
    private String getBG, getCity, getCondition;
    private ProgressDialog progressDialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_list);
        getLayoutId();
        setOnClickListner();
    }
    private void getLayoutId()
    {
        fetchBloodgroup = (Spinner)findViewById(R.id.fetchBloodgroup);
        fetchCity = (Spinner)findViewById(R.id.fetchCity);
        searchBT =(Button)findViewById(R.id.searchBT);
        searchLV = (ListView)findViewById(R.id.searchLV);
    }
    private void getSearchList()
    {
        Log.d(TAG,"getSearchList enter()");
        /*progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Preparing Data");
        progressDialog.show();*/
        Log.d(TAG,"progressDialog show()");
        getBG = fetchBloodgroup.getSelectedItem().toString();
        getCity= fetchCity.getSelectedItem().toString();
        getCondition = getBG.concat(getCity);
        Query query = FirebaseDatabase.getInstance().getReference().child("profile").orderByChild("userCondition").equalTo(getCondition);
        FirebaseListOptions<UserProfile> options = new FirebaseListOptions.Builder<UserProfile>().setLayout(R.layout.customlistview)
                .setQuery(query,UserProfile.class).build();


        listAdapter = new FirebaseListAdapter(options) {
            @Override
            protected void populateView(View v, Object model, int position) {
                        UserProfile userProfile = (UserProfile) model;
                        TextView name = v.findViewById(R.id.customName);
                        TextView mobile = v.findViewById(R.id.customMobile);
                        TextView BG = v.findViewById(R.id.customBg);
                        TextView email = v.findViewById(R.id.customEmail);
                        TextView city = v.findViewById(R.id.customCity);

                        name.setText("Name:"+userProfile.getUserName().toString());
                        mobile.setText("Mobile No:"+userProfile.getUserMobileNo().toString());
                        BG.setText("BloodGroup:"+userProfile.getUserBloodgroup().toString());
                        email.setText("Email Id:"+userProfile.getUserEmailId().toString());
                        city.setText("Location:"+userProfile.getUserCity().toString());

            }
        };

            searchLV.setAdapter(listAdapter);
            listAdapter.startListening();

            Log.d(TAG,"getSearchList exit()");
    }

    private void setOnClickListner()
    {
        android.view.View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.searchBT:
                        if(validate() == true){
                            if(isNetworkAvailable())
                            {
                                getSearchList();

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Network not available",Toast.LENGTH_LONG).show();
                            }
                        }
                }
            }
                };
        searchBT.setOnClickListener(onClickListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        listAdapter.stopListening();
    }
    private boolean  validate ()
    {
        if (fetchBloodgroup.getSelectedItem().toString().trim().equals("Select your blood group")) {
            Toast.makeText(getApplicationContext(), "Select your blood group", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (fetchCity.getSelectedItem().toString().trim().equals("Select District")) {
            Toast.makeText(getApplicationContext(), "Select your city", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        Log.d("LoginActivity","isNetworkAvailable");
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
