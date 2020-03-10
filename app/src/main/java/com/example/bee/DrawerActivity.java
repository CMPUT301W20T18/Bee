////package com.example.bee;
////
//
////
////public class DrawerActivity extends AppCompatActivity {
////
////    private Button btnLogout;
////    private FirebaseAuth firebaseAuth;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_drawer);
////
////        btnLogout = findViewById(R.id.btnLogout);
////        firebaseAuth = FirebaseAuth.getInstance();
////
////        FirebaseUser user = firebaseAuth.getCurrentUser();
////        startActivity(new Intent(DrawerActivity.this, DrawerActivity.class));
////
////        if (user != null) {
////            // currently have a user signed in
////            btnLogout.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View view) {
////
////                    firebaseAuth.signOut();
////                    finish();
////
////
////
////                }
////            });
////
////        } else {
////            startActivity(new Intent(DrawerActivity.this, LoginActivity.class));
////            startActivity(new Intent(DrawerActivity.this, DrawerActivity.class));
////
////        }
////
////    }
////}
//package com.example.bee;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.drawerlayout.widget.DrawerLayout;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.ActionBarDrawerToggle;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.drawerlayout.widget.DrawerLayout;
//
//import android.os.Bundle;
//import android.view.MenuItem;
//import android.widget.Toast;
//
//import com.google.android.material.navigation.NavigationView;
//
//public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
//
//    DrawerLayout drawerLayout;
//    Toolbar toolbar;
//    NavigationView navigationView;
//    ActionBarDrawerToggle toggle;
//
//    private Button btnLogout;
//    private FirebaseAuth firebaseAuth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_drawer);
//
//        drawerLayout = findViewById(R.id.drawer);
//        toolbar = findViewById(R.id.toolbar);
//        navigationView = findViewById(R.id.navigationView);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen,R.string.drawerClose);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
//        navigationView.setNavigationItemSelectedListener(this);
//
//
//        btnLogout = findViewById(R.id.btnLogout);
//        firebaseAuth = FirebaseAuth.getInstance();
////
////        FirebaseUser user = firebaseAuth.getCurrentUser();
////        startActivity(new Intent(DrawerActivity.this, DrawerActivity.class));
////
////        if (user != null) {
////            // currently have a user signed in
////            btnLogout.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View view) {
////
////                    firebaseAuth.signOut();
////                    finish();
////
////
////
////                }
////            });
////
////        } else {
////            startActivity(new Intent(DrawerActivity.this, LoginActivity.class));
////            startActivity(new Intent(DrawerActivity.this, DrawerActivity.class));
////
////        }
////
////    }
//
//
//    }
//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//        switch (menuItem.getItemId()){
//            case R.id.profile:
//                Toast.makeText(DrawerActivity.this, "Profile Selected", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.contact:
//                Toast.makeText(DrawerActivity.this, "Contact us Selected", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.about:
//                Toast.makeText(DrawerActivity.this, "About us Selected", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.logout:
//                Toast.makeText(DrawerActivity.this, "Logout Selected", Toast.LENGTH_SHORT).show();
//                break;
//            default:
//                break;
//        }
//        return false;
//    }
//}
package com.example.bee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    View headerView;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    private Button btnLogout;
    private FirebaseAuth firebaseAuth;
    private EditText displayName;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_drawer);


        drawerLayout = findViewById(R.id.drawer);

        //displayName = findViewById(R.id.profileName);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
        //headerView = navigationView.inflateHeaderView(R.layout.header);
        //displayName  = headerView.findViewById(R.id.profileName);
        displayName = navigationView.getHeaderView(0).findViewById(R.id.profileName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        btnLogout = findViewById(R.id.btnLogout);
        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("users");
        ref.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snap: dataSnapshot.getChildren()){
                    displayName.setText(dataSnapshot.child("Name").getValue().toString());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(DrawerActivity.this, LoginActivity.class));

                    firebaseAuth.signOut();
                    finish();



                }
            });

        }







    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.profile:
                Toast.makeText(DrawerActivity.this, "Profile Selected", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DrawerActivity.this, EditProfileActivity.class));
                return true;
            case R.id.rating:
                Toast.makeText(DrawerActivity.this, "My Rating Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.about:
                Toast.makeText(DrawerActivity.this, "About us Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                Toast.makeText(DrawerActivity.this, "Logout Selected", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return false;
    }
}