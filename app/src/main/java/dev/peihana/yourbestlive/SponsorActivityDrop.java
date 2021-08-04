package dev.peihana.yourbestlive;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.stripe.android.model.Card;
import com.stripe.android.view.CardInputWidget;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SponsorActivityDrop extends AppCompatActivity implements View.OnClickListener{

    EditText nome_locale;
    CheckBox uno;
    CheckBox tre;//
    CheckBox sei;
    int durata=0;
    String money;
    Button conferma;
    String locale="";
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    String citta;
    String cvc;
    int exp_month;
    int exp_year;
    String card_number;
    final String send_payment_details = "http://peihana.pythonanywhere.com/charge";
    CardInputWidget mCardInputWidget;
    DatabaseReference ref=FirebaseDatabase.getInstance().getReference();
    ProgressDialog dialog;
    Spinner areaSpinner;
    Spinner localSpinner;


    //this activity is created when trying to sponsor a local and allows to decide for how much time to sponsor it, sending money through Stripe APIs
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_drop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sponsor a club");
        dialog = new ProgressDialog(this);
        Intent i=getIntent();
        mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);

        areaSpinner=(Spinner) findViewById(R.id.city);
        localSpinner=(Spinner) findViewById(R.id.club);

        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                citta=areaSpinner.getSelectedItem().toString();
                ref.child("Città").child(citta).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Is better to use a List, because you don't know the size
                        // of the iterator returned by dataSnapshot.getChildren() to
                        // initialize the array
                        final List<String> areas = new ArrayList<String>();

                        for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                            String localName = areaSnapshot.getKey();
                            areas.add(localName);
                        }


                        ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(SponsorActivityDrop.this, android.R.layout.simple_spinner_item, areas);
                        areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        localSpinner.setAdapter(areasAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });


        citta="Roma";
        ref.child("Città").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                final List<String> areas = new ArrayList<String>();

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.getKey();
                    areas.add(areaName);
                }


                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(SponsorActivityDrop.this, android.R.layout.simple_spinner_item, areas);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                areaSpinner.setAdapter(areasAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref.child("Città").child(citta).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                final List<String> areas = new ArrayList<String>();

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String localName = areaSnapshot.getKey();
                    areas.add(localName);
                }


                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(SponsorActivityDrop.this, android.R.layout.simple_spinner_item, areas);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                localSpinner.setAdapter(areasAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        uno=(CheckBox) findViewById(R.id.uno);
        tre=(CheckBox) findViewById(R.id.tre);
        sei=(CheckBox) findViewById(R.id.sei);
        conferma=(Button) findViewById(R.id.conferma);
        conferma.setOnClickListener(this);
        uno.setOnClickListener(this);
        tre.setOnClickListener(this);
        sei.setOnClickListener(this);

        // new HttpRequest().execute();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public void uno_b() {
        if(uno.isChecked()) {
            tre.setChecked(false);
            sei.setChecked(false); }
    }
    public void tre_b() {
        if(tre.isChecked()) {
            uno.setChecked(false);
            sei.setChecked(false);
        }
    }
    public void sei_b() {
        if(sei.isChecked()) {
            tre.setChecked(false);
            uno.setChecked(false);
        }
    }

    public void sponsorizza() {

        locale=localSpinner.getSelectedItem().toString();
        citta=areaSpinner.getSelectedItem().toString();

        if(!locale.equals("") && !citta.equals("") && (uno.isChecked() || tre.isChecked() || sei.isChecked())) {

            ref.child("Città").child(citta).child(locale).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()) {
                        Toast.makeText(SponsorActivityDrop.this,
                                "Make sure you write the city and the name of the venue correctly",
                                Toast.LENGTH_SHORT).show(); }
                    else { pay();  }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            }); }
        else Toast.makeText(SponsorActivityDrop.this,
                "You can't leave empty fields",
                Toast.LENGTH_SHORT).show();

    }


    private void pay() {
        money = "0";
        if(uno.isChecked()) {
            money="299";
            durata=1; }
        else if(tre.isChecked()) {
            money="699";
            durata=3;
        }
        else if(sei.isChecked()) {
            money="1199";
            durata=6;
        } }
    //stripe(); }


    @Override
    public void onClick(View v) {

        if(v==conferma) {
            dialog.setMessage("Payment in progress...");
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
            sponsorizza();
            ref.child("Città").child(citta).child(locale).child("sponsorizzato").setValue(0);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    dialog.dismiss();
                }
            }, 1500);
            Toast.makeText(SponsorActivityDrop.this,
                    "Club sponsored",
                    Toast.LENGTH_SHORT).show();
            Intent i = new Intent(SponsorActivityDrop.this, RecyclerActivity.class);
            startActivity(i); }
        if(v==uno) uno_b();
        if(v==tre) tre_b();
        if(v==sei) sei_b();

    }


    public void grizie(String s) {

        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.ringraziamenti_alert, null);
        final TextView taskEditText=(TextView) deleteDialogView.findViewById(R.id.text);
        taskEditText.setText(s);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);
        deleteDialogView.findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                deleteDialog.dismiss();
                finish();

            }
        });

        deleteDialog.setCanceledOnTouchOutside(false);
        deleteDialog.show(); }

}
