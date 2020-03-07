package com.example.bee;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private ImageView logo, ivSignIn;
    private AutoCompleteTextView email, password;
    private TextView forgotPass, signUp;
    private Button btnSignInRider, btnSignInDriver;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeGUI();

        /*user = firebaseAuth.getCurrentUser();

        if(user != null) {
            finish();
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }*/

        btnSignInRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inEmail = email.getText().toString();
                String inPassword = password.getText().toString();

                if(validateInput(inEmail, inPassword)){
                    signUser(inEmail, inPassword, "rider");
                }

            }
        });

        btnSignInDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inEmail = email.getText().toString();
                String inPassword = password.getText().toString();

                if(validateInput(inEmail, inPassword)){
                    signUser(inEmail, inPassword, "driver");
                }

            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,PWresetActivity.class));
            }
        });



    }



    public void signUser(String email, String password, String role){

        progressDialog.setMessage("Verifying...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                    if (role.equals("rider")) {
                        startActivity(new Intent(LoginActivity.this, EnterAddressMap.class));
                    } else {
                        // startActivity(new Intent(LoginActivity.this, Driverblahblah.class));
                    }
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,"Invalid email or password",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void initializeGUI(){

        logo = findViewById(R.id.ivLogLogo);
        email = findViewById(R.id.atvEmailLog);
        password = findViewById(R.id.atvPasswordLog);
        forgotPass = findViewById(R.id.tvForgotPass);
        signUp = findViewById(R.id.tvSignIn);
        btnSignInRider = findViewById(R.id.btnSignInRider);
        btnSignInDriver = findViewById(R.id.btnSignInDriver);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

    }


    public boolean validateInput(String inemail, String inpassword){

        if(inemail.isEmpty()){
            email.setError("Email field is empty.");
            return false;
        }
        if(inpassword.isEmpty()){
            password.setError("Password is empty.");
            return false;
        }

        return true;
    }

}
