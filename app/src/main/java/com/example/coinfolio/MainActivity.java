package com.example.coinfolio;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.coinfolio.ui.Addcoins.AddCoinsFragment;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import javax.sql.DataSource;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import static com.google.android.gms.auth.api.signin.GoogleSignIn.getClient;

public class MainActivity extends AppCompatActivity {
    private User CurrUser = User.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        setTheme(R.style.DarkTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        User currentUser = User.getInstance();
        if(getIntent().getExtras() != null)
        {
            String CurrentUUID = (String) getIntent().getExtras().get("uuid");
            currentUser.setUserUUId(CurrentUUID);
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }


    private GoogleSignInClient mGoogleSignInClient;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.signoutButton) {
            FirebaseAuth.getInstance().signOut();
            Request();
            mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    startActivity(new Intent(MainActivity.this,Login.class));
                    Toast.makeText(getApplicationContext(), "User Signed Out", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            return true;
        }
        else if(item.getItemId() == R.id.myPortfolio)
        {
            Toast.makeText(getApplicationContext(), "test ", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, Profile_Screen.class));
        }
        return super.onOptionsItemSelected(item);
    }

    // Lets the sign out get the right client, otherwise crashes the app
    public void Request(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = getClient(this, gso);
    }
}
