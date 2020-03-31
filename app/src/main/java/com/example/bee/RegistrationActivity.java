package com.example.bee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistrationActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    private ImageView logo;
    private AutoCompleteTextView username, email, password, phone, firstName, lastName;
    private Button signup;
    private TextView signin,passwordhint;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 4 characters
                    "$");
    FirebaseFirestore db;
    String userID, registerEmail;




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
                final String infirstName = firstName.getText().toString().trim();
                final String inlastName = lastName.getText().toString().trim();

//
//                if (validateInput(inputName, inputPw, inputPhone, inputEmail)&& isUsernameExists(inputName)==false)
//                    registerUser(inputName, inputPw, inputPhone, inputEmail);
                if (validateEmail(inputEmail)&&validatePassword(inputPw)&&validateUsername(inputName)&&validatePhone(inputPhone))
                    registerUser(inputName, inputPw, inputPhone, inputEmail,infirstName,inlastName);


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
        firstName = findViewById(R.id.atvFirstName);
        lastName = findViewById(R.id.atvLastName);
        phone = findViewById(R.id.phoneNum);
        email = findViewById(R.id.atvEmailReg);
        password = findViewById(R.id.atvPasswordReg);
        signin = findViewById(R.id.tvSignIn);
        signup = findViewById(R.id.btnSignUp);
        passwordhint = findViewById(R.id.tvPasswordHint);
        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
    }




    public void registerUser(final String inputName, final String inputPw, final String phone, final String inputEmail, final String infirstName, final String inlastName) {

        progressDialog.setMessage("Verifying...");
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

                    // Add by Ruichen, to create a empty ArrayList<Request> for new user
                    // To store their history requests
                    final FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                    final DatabaseReference hisRef = database1.getReference("history");
                    ArrayList<Request> history = new ArrayList<>();
                    hisRef.child(userID).setValue(history);

                    final DatabaseReference usersRef = ref.child(userID);

                    HashMap<String,Object> user = new HashMap<>();

                    user.put("Name",inputName);
                    user.put("email",inputEmail);
                    user.put("phone",phone);
                    user.put("firstName",infirstName);
                    user.put("lastName",inlastName);
                    user.put("thumbUp",0);
                    user.put("thumbDown",0);
                    user.put("Wallet", new QRWallet(userID));

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
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                }else {
                    //Toast.makeText(RegistrationActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(RegistrationActivity.this, "Username Taken", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }






    public boolean validateEmail(String emailInput) {


        if (emailInput.isEmpty()) {
            email.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError("Please enter a valid email address");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    public boolean validateUsername(String usernameInput) {

        if (usernameInput.isEmpty()) {
            username.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 15) {
            username.setError("Username too long");
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }

    public boolean validatePassword(String passwordInput) {


        if (passwordInput.isEmpty()) {
            password.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            password.setError("Password too weak");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }
    public boolean validatePhone(String phoneInput) {


        if (phoneInput.isEmpty()) {
            phone.setError("Field can't be empty");
            return false;
        } else if (!isNumeric(phoneInput)) {
            phone.setError("Invalid Phone");
            return false;
        } else {
            phone.setError(null);
            return true;
        }
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
