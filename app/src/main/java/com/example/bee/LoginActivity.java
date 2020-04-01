package com.example.bee;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

/**
 *  This class logs a user in if they already registered in the database
 *  connects to the RegistrationActivity
 */
public class LoginActivity extends FragmentActivity {

    private ImageView logo, ivSignIn;
    private AutoCompleteTextView name, password;
    private TextView forgotPass, signUp;
    private Button btnSignInRider, btnSignInDriver;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        showCovidDialog();
        initializeGUI();

//        Fragment mFragment = null;
//        mFragment = new CovidHint();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.covidHint, mFragment).commit();
//        Intent i = new Intent(LoginActivity.this,CovidHint.class);
//        startActivity(i);


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
        progressDialog.setCancelable(false);;
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
                    Toast.makeText(LoginActivity.this,"Invalid username or password",Toast.LENGTH_SHORT).show();
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

    private void showCovidDialog(){


        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        View view = getLayoutInflater().inflate(R.layout.covid_hint,null);
        TextView title = (TextView) view.findViewById(R.id.dialog_title);
        title.setText("Flatten the Curve!");
        Button gotBtn = view.findViewById(R.id.gotBtn);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
        gotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });




    }

}


