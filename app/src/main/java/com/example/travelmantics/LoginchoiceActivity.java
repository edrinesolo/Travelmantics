package com.example.travelmantics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;  
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;  
import com.google.android.gms.auth.api.signin.GoogleSignInResult;  
import com.google.android.gms.common.ConnectionResult;  
import com.google.android.gms.common.SignInButton;  
import com.google.android.gms.common.api.GoogleApiClient;

public class LoginchoiceActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
	SignInButton signInButton;  
    private GoogleApiClient googleApiClient; 
    private Button emailbut;
    private static final int RC_SIGN_IN = 1; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginchoice);

        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)  
                .requestEmail()  
                .build();  
        googleApiClient=new GoogleApiClient.Builder(this)  
                .enableAutoManage(this,this)  
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)  
                .build();  
  
  
  
        signInButton=(SignInButton)findViewById(R.id.google_login);  
        signInButton.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View view) {  
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);  
                startActivityForResult(intent,RC_SIGN_IN);  
            }  
        });  

        emailbut=(Button)findViewById(R.id.emaillog);
        emailbut.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View view) {
                Intent intent = new Intent (LoginchoiceActivity.this, LoginActivity.class);
                startActivity(intent); 
            }  
        });  
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(LoginchoiceActivity.this, "connection failed", Toast.LENGTH_LONG).show();
    }
}
