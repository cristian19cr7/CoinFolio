package com.example.coinfolio;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile_Screen extends AppCompatActivity {
    private TextView Profile_email, Profile_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__screen);
        Profile_email = findViewById(R.id.profile_email);
        Profile_name = findViewById(R.id.profile_name);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();

            Profile_email.setText(email);
            Profile_name.setText(name);
        }
    }
}
