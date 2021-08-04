package dev.peihana.yourbestlive;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private FirebaseUser user;
    private FirebaseAuth auth;
    private EditText emailText;
    private EditText passText;
    private Button log_button;
    private ProgressDialog dialog;
    private TextView textReg;
    private TextView noPassText;
    private CheckBox rememberMe;
    private TextView no_log;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    LoginButton loginButton;
    CallbackManager callbackManager;
    DatabaseReference ref= FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //populate the toolbar above
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("yourBestLive");
        auth = FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        //facebook API stuff
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                dialog.setMessage("Logging in..");
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_SHORT).show();
            }
        });

        //listeners binding and shared preferences to save infos across activities
        textReg=(TextView) findViewById(R.id.textReg);
        noPassText=(TextView) findViewById(R.id.noPassText);
        log_button = (Button) findViewById(R.id.log_button);
        no_log = (TextView) findViewById(R.id.no_log);
        emailText = (EditText) findViewById(R.id.emailText);
        passText = (EditText) findViewById(R.id.passText);
        rememberMe= (CheckBox) findViewById(R.id.rememberMe);
        log_button.setOnClickListener(this);
        textReg.setOnClickListener(this);
        rememberMe.setOnClickListener(this);
        noPassText.setOnClickListener(this);
        no_log.setOnClickListener(this);
        dialog = new ProgressDialog(this);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);

        if (saveLogin == true) {
            emailText.setText(loginPreferences.getString("username", ""));
            passText.setText(loginPreferences.getString("password", ""));
            rememberMe.setChecked(true);
        }
    }

    //other fb API stuff, also adds the fb user to the Firebase db as a normal registered user and sends the user to the RecyclerActivity
    public void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    user = auth.getCurrentUser();
                    ref.child("Users").child(user.getDisplayName()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.exists()) {
                                ref.child("Users").child(user.getDisplayName()).child("nome utente").setValue(user.getDisplayName());
                                ref.child("Users").child(user.getDisplayName()).child("email").setValue(user.getEmail());
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });

                    Intent i=new Intent(LoginActivity.this,RecyclerActivity.class);
                    dialog.hide();
                    startActivity(i);
                    finish();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Failed authentication", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void changePassword() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.alert_password, null);
        final EditText taskEditText=(EditText) deleteDialogView.findViewById(R.id.sceglicittà);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);
        deleteDialogView.findViewById(R.id.conferma2_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.sendPasswordResetEmail(String.valueOf(taskEditText.getText()));
                Toast.makeText(LoginActivity.this,
                        "Email reset password sended to " + String.valueOf(taskEditText.getText()),
                        Toast.LENGTH_SHORT).show();
                deleteDialog.dismiss();
            }
        });
        deleteDialogView.findViewById(R.id.annulla2_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });
        deleteDialog.show(); }

    public void remember() {
        String username = emailText.getText().toString();
        String password = passText.getText().toString();
        if (rememberMe.isChecked()) {
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("username", username);
            loginPrefsEditor.putString("password", password);
            loginPrefsEditor.commit();
        } else {
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }
    }

    public void login() {
        final String email = emailText.getText().toString().trim();
        final String password = passText.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(LoginActivity.this, "Insert email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Insert password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(LoginActivity.this, "Too short password", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            dialog.setMessage("Loggin in..");
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                checkEmailVerification();
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "Wrong credentials", Toast.LENGTH_SHORT).show();
                                dialog.hide();
                            }
                        }
                    });
        }
    }

    public void checkEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.isEmailVerified()) {
            Intent i = new Intent(LoginActivity.this,RecyclerActivity.class);
            startActivity(i);
            finish(); }

        else {
            Toast.makeText(LoginActivity.this, "Before logging in, verify your email!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(LoginActivity.this, LoginActivity.class);
            startActivity(i);
            overridePendingTransition(0,0);
            FirebaseAuth.getInstance().signOut();
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        if(v==log_button) {
            login();
            remember();
        }
        if(v==textReg)  {
            Intent i=new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(i);
        }
        if(v==noPassText) changePassword();
        if(v==no_log) {
            Intent i=new Intent(LoginActivity.this,RecyclerActivity.class);
            startActivity(i);
            finish(); }
    }
}