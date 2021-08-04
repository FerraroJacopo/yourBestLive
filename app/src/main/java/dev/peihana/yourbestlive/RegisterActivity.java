package dev.peihana.yourbestlive;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Users");
    private FirebaseAuth auth;
    private FirebaseUser user;
    private EditText emailText;
    private EditText nomeText;
    private EditText passText;
    private ProgressDialog dialog;
    private Button reg_button;
    private String nomeUtente;
    private String email;
    private String password;
    private FirebaseAuth.AuthStateListener mAuthListener;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sign Up");

        auth = FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(nomeUtente).build();
                    user.updateProfile(profileUpdates);
                    sendVerification();

                }
            }
        };
        dialog=new ProgressDialog(this);
        emailText=(EditText) findViewById(R.id.emailText);
        passText=(EditText) findViewById(R.id.passText);
        nomeText=(EditText) findViewById(R.id.text);
        reg_button=(Button) findViewById(R.id.reg_button);
        reg_button.setOnClickListener(this); }


    //invia email di verifica
    public void sendVerification() {
        user = auth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth.getInstance().signOut();
                        }
                    }
                });  }


    @Override
    public void onResume(){
        super.onResume();
        auth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mAuthListener != null){
            auth.removeAuthStateListener(mAuthListener);
        }
    }


    //gestisce la registrazione
    public void register() {
        email=emailText.getText().toString().trim();
        password=passText.getText().toString().trim();
        nomeUtente=nomeText.getText().toString().trim();


        if(TextUtils.isEmpty(email)) {
            Toast.makeText(RegisterActivity.this,"Insert email",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this,"Insert password",Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.length()<6) {
            Toast.makeText(RegisterActivity.this,"Password too short",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(nomeUtente)) {
            Toast.makeText(RegisterActivity.this,"Insert username",Toast.LENGTH_SHORT).show();
            return;
        }

        else {
            dialog.setMessage("Registration in progress..");
            dialog.show();
            ref.child(nomeUtente).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Toast.makeText(RegisterActivity.this,"This username already exists",Toast.LENGTH_SHORT).show();
                        dialog.hide();
                    }
                    else {
                        auth.createUserWithEmailAndPassword(email,password)
                                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()) {
                                            saveInDatabase();
                                            dialog.hide();
                                            Toast.makeText(RegisterActivity.this,
                                                    "Verificatione email sended to " + emailText.getText().toString(),
                                                    Toast.LENGTH_SHORT).show();
                                            finish(); }
                                        else {
                                            Toast.makeText(RegisterActivity.this,
                                                    "Try again",
                                                    Toast.LENGTH_SHORT).show();
                                            dialog.hide();
                                            finish();
                                        } } }); } }


                @Override
                public void onCancelled(DatabaseError DatabaseError) { } } ); }}



    //salva email e nome utente nel Database
    public void saveInDatabase() {
        email=emailText.getText().toString().trim();
        password=passText.getText().toString().trim();
        nomeUtente=nomeText.getText().toString().trim();

        DatabaseReference usersRef = ref.child(nomeUtente);
        Map<String, Object> nomeUtente_list = new HashMap<String, Object>();
        Map<String, Object> email_list = new HashMap<String, Object>();

        nomeUtente_list.put("nome utente", nomeUtente);
        email_list.put("email", email);

        usersRef.updateChildren(nomeUtente_list);
        usersRef.updateChildren(email_list);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        if(v==reg_button) { register(); }

    }
}
