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
/**
 *  This class logs a user in if they already registered in the database
 *  connects to the RegistrationActivity
 */
public class LoginActivity extends AppCompatActivity {

    private ImageView logo, ivSignIn;
    private AutoCompleteTextView name, password;
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

        user = firebaseAuth.getCurrentUser();



        btnSignInRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inEmail = name.getText().toString()+"@gmailbiugvuebgiuv.com";
                String inPassword = password.getText().toString();

                if(validateInput(inEmail, inPassword)){
                    signUser(inEmail, inPassword,"rider");
                }

            }
        });

        btnSignInDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inEmail = name.getText().toString()+"@gmailbiugvuebgiuv.com";
                String inPassword = password.getText().toString();

                if(validateInput(inEmail, inPassword)){
                    signUser(inEmail, inPassword,"driver");
                }

            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
            }
        });

//        forgotPass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(LoginActivity.this,PWresetActivity.class));
//            }
//        });



    }


/**
 *  This method builds the firebase authentication
 */
    public void signUser(String email, String password,final String role){

        progressDialog.setMessage("Verifying...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                    if( role.equals("rider")) {
                        startActivity(new Intent(LoginActivity.this,EnterAddressMap.class));
                    } else{
                        Intent driver = new Intent(LoginActivity.this, DriverMain.class);
                        startActivity(driver);
                    }
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,"Invalid email or password",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
/**
 *  This initializes the GUI
 */

    private void initializeGUI(){

        logo = findViewById(R.id.ivLogLogo);
        name = findViewById(R.id.atvUsernameReg);
        password = findViewById(R.id.atvPasswordLog);
        //forgotPass = findViewById(R.id.tvForgotPass);
        signUp = findViewById(R.id.tvSignIn);
        btnSignInRider = findViewById(R.id.btnSignInRider);
        btnSignInDriver = findViewById(R.id.btnSignInDriver);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

    }
/**
 *  This is used to determine if user input is valid before log in 
 */

    public boolean validateInput(String inemail, String inpassword){

        if(inemail.isEmpty()){
            name.setError("Username field is empty.");
            return false;
        }
        if(inpassword.isEmpty()){
            password.setError("Password is empty.");
            return false;
        }

        return true;
    }

}
