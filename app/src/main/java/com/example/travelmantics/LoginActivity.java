package com.example.travelmantics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
	private EditText emailfield;
    private EditText password;
    
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabasePlace_users;
    private TextView regestertext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int orientation = getResources().getConfiguration().orientation;
        setContentView(R.layout.activity_login);

        if (mDatabasePlace_users != null) {
            mDatabasePlace_users.keepSynced(true);
        }

        mAuth = FirebaseAuth.getInstance();
        mDatabasePlace_users = FirebaseDatabase.getInstance().getReference().child("Users");
        // Set up the login form.
        progressDialog = new ProgressDialog(this);
        emailfield = (EditText) findViewById(R.id.login_email);
        password = (EditText) findViewById(R.id.login_password);
        Button email_sign_in_button = (Button) findViewById(R.id.btn_login);

        regestertext = (TextView) findViewById(R.id.link_signup);
        regestertext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToegister();
            }
        });

        email_sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSigningIn();
            }
        });
    }

    private void startSigningIn() {
        String email = emailfield.getText().toString().trim();
        String pass = password.getText().toString().trim();


        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {
            progressDialog.setMessage("Checking sign in....");
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isComplete()) {
                        if (task.isSuccessful()) {
                        	progressDialog.dismiss();
                        	Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    		startActivity(i);
                    		
                            
                        } else {
                            Toast.makeText(LoginActivity.this, "Sign In Error", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }


                }
            });
        } else {
            Toast.makeText(LoginActivity.this, "fill in fields", Toast.LENGTH_LONG).show();
        }
    }

    public void goToegister() {
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));

    }


}
