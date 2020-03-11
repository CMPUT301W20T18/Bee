package com.example.bee;

import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistrationActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    private ImageView logo;
    private AutoCompleteTextView username, email, password, phone;
    private Button signup;
    private TextView signin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    FirebaseFirestore db;
    String userID, registerEmail;
    ProgressBar progressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initializeGUI();




        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String inputName = username.getText().toString().trim();
                final String inputPhone = phone.getText().toString().trim();
                final String inputPw = password.getText().toString().trim();
                final String inputEmail = email.getText().toString().trim();

//
//                if (validateInput(inputName, inputPw, inputPhone, inputEmail)&& isUsernameExists(inputName)==false)
//                    registerUser(inputName, inputPw, inputPhone, inputEmail);
                if (validateInput(inputName, inputPw, inputPhone, inputEmail))
                    registerUser(inputName, inputPw, inputPhone, inputEmail);


            }

        });


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });

    }



    private void initializeGUI() {

        logo = findViewById(R.id.ivRegLogo);
        username = findViewById(R.id.atvUsernameReg);
        phone = findViewById(R.id.phoneNum);
        email = findViewById(R.id.atvEmailReg);
        password = findViewById(R.id.atvPasswordReg);
        signin = findViewById(R.id.tvSignIn);
        signup = findViewById(R.id.btnSignUp);
        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
    }




    private void registerUser(final String inputName, final String inputPw, final String phone, final String inputEmail) {

        progressDialog.setMessage("Verificating...");
        progressDialog.show();


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("users");
//        System.out.println("test");

        registerEmail = inputName+"@gmailbiugvuebgiuv.com";




        firebaseAuth.createUserWithEmailAndPassword(registerEmail,inputPw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegistrationActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                    userID = firebaseAuth.getCurrentUser().getUid();


                    final DatabaseReference usersRef = ref.child(userID);

                    HashMap<String,Object> user = new HashMap<>();

                    user.put("Name",inputName);
                    user.put("email",inputEmail);
                    user.put("phone",phone);

                    usersRef.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + e.toString());
                        }
                    });
                    startActivity(new Intent(getApplicationContext(), DrawerActivity.class));

                }else {
                    //Toast.makeText(RegistrationActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(RegistrationActivity.this, "Username Taken", Toast.LENGTH_SHORT).show();
                    //progressBar.setVisibility(View.GONE);
                }
            }
        });






    }






    private boolean validateInput(String inName, String inPw, String inPhone, String inEmail){

        if(inName.isEmpty()){
            username.setError("Username is empty.");
            return false;
        }

        if(isNumeric(inPhone)==false||inPhone.length()!=10){
            password.setError("Invalid Phone number ");
            return false;
        }
        if(inPw.length()!= 6){
            password.setError("Invalid Password ");
            return false;
        }
        if(inEmail.contains("@")==false){
            email.setError("Invalid Email");
            return false;
        }

        return true;
    }



        public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }


}