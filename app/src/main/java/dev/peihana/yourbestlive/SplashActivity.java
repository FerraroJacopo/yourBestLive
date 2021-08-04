package dev.peihana.yourbestlive;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseAuth;


public class SplashActivity extends AppCompatActivity {

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(FirebaseAuth.getInstance().getCurrentUser()==null)
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        else {
            loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
            loginPrefsEditor = loginPreferences.edit();
            saveLogin = loginPreferences.getBoolean("saveLogin", false);
            if(saveLogin || AccessToken.getCurrentAccessToken()!= null)  startActivity(new Intent(SplashActivity.this, RecyclerActivity.class));
            else {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
        }
       //  close splash activity
        finish();

    }
}
