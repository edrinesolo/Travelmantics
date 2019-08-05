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

public class SignUpActivity extends AppCompatActivity {
	private EditText nameInput;
    private EditText emailfields;
    private EditText passs;
    private TextView login;
    private ProgressDialog mProgress;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        mProgress=new ProgressDialog(this);
        nameInput=(EditText)findViewById(R.id.input_name);
        emailfields=(EditText)findViewById(R.id.emaill);
        passs=(EditText)findViewById(R.id.input_password1);

        Button regbutton=(Button)findViewById(R.id.btn_signup);
        regbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegistering();
            }
        });

        login=(TextView)findViewById(R.id.link_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTologin();
            }
        });

    }
    public void goTologin() {
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));

    }

    private void startRegistering() {

        final String name=nameInput.getText().toString().trim();
        String email=emailfields.getText().toString().trim();
        String password=passs.getText().toString().trim();


        if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){
            mProgress.setMessage("Registering...");
            mProgress.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
			    @Override
			    public void onComplete(@NonNull Task<AuthResult> task) {
			        if (task.isComplete()){
			            if(task.isSuccessful()){

			                String user_id=mAuth.getCurrentUser().getUid();
			                DatabaseReference currentUserDb= mDatabase.child(user_id);
			                currentUserDb.child("UserName").setValue(name);
			                currentUserDb.child("user_image").setValue("default");

			                mProgress.dismiss();

			                Intent mainIntent=new Intent(SignUpActivity.this,MainActivity.class);
			                mainIntent.addFlags(mainIntent.FLAG_ACTIVITY_CLEAR_TOP);
			                startActivity(mainIntent);

			            }else {
			                Toast.makeText(SignUpActivity.this, "Registration failed try again later", Toast.LENGTH_LONG).show();
			                mProgress.dismiss();
			            }
			        }

			    }
			});

        }

    }
}
