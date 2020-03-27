
package com.example.bee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
/**
 *  This class supports log out function and a drawer for user to view, edit profile
 */
public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    View headerView;
    NavigationView navigationView;

    private ImageView button;
    private FirebaseAuth firebaseAuth;
    private TextView displayName;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_drawer);


        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        displayName = navigationView.getHeaderView(0).findViewById(R.id.profileName);
        navigationView.setNavigationItemSelectedListener(this);






        button = findViewById(R.id.profile_btn);
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




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
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
