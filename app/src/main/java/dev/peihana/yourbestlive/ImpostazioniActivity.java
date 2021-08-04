package dev.peihana.yourbestlive;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.database.ValueEventListener;
import com.stripe.android.model.Card;
import com.stripe.android.view.CardInputWidget;

import java.util.HashMap;
import java.util.Map;


public class ImpostazioniActivity extends AppCompatActivity {

    String[] cityList;
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    AutoCompleteTextView automatictext;
    String stringa;
    //production
    DatabaseReference ref=FirebaseDatabase.getInstance().getReference();
    TextView imp;
    String money;
    String cvc;
    int exp_month;
    int exp_year;
    String card_number;
    final String send_payment_details = "http://peihana.pythonanywhere.com/charge";
    CardInputWidget mCardInputWidget;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {//populates the button list due to the user type
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impostazioni);
        dialog = new ProgressDialog(this);
        stringa="";

        final View v2=(View) findViewById(R.id.view_fine);
        final ImageView i=(ImageView) findViewById(R.id.imageView9);
        imp = (TextView) findViewById(R.id.text_impost);

        if(user!=null)  {
            ref.child("Users").child(user.getDisplayName()).child("premium").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        imp.setVisibility(View.GONE);
                        v2.setVisibility(View.GONE);
                        i.setVisibility(View.GONE); } }
                @Override
                public void onCancelled(DatabaseError firebaseError) { } }); }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");
        mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);


        View ly = findViewById(R.id.impostacitta);
        ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlert();
            }
        });

        View lyy = findViewById(R.id.dona);
        lyy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDona();
            }
        });

        View lyyy = findViewById(R.id.invitaAmici);
        lyyy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Ho trovato l'applicazione perfetta per te!\nhttps://kbqb6.app.goo.gl/mVFa");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        View lyyyy = findViewById(R.id.rimuovi_pubb);
        lyyyy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user!=null)  alertAd();
                else accediAlert();
            }
        });


        View lyyyyy = findViewById(R.id.info);
        lyyyyy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(ImpostazioniActivity.this,InfoActivity.class);
                startActivity(i);
            }
        });

        View lyyyyyy = findViewById(R.id.registra_local);
        lyyyyyy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user!=null) {
                Intent i= new Intent(ImpostazioniActivity.this,LocalRegister.class);
                startActivity(i); }
                else accediAlert();
            }
        });

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public void alertDona() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.alert_donation, null);
        final EditText taskEditText=(EditText) deleteDialogView.findViewById(R.id.text);
        mCardInputWidget = (CardInputWidget) deleteDialogView.findViewById(R.id.card_input_widget);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);
        deleteDialogView.findViewById(R.id.dona_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                if(!taskEditText.getText().toString().equals("")) {
                    money=taskEditText.getText().toString();
                    stringa="donazione";
                    dialog.setMessage("Payment in progress...");
                    dialog.setCanceledOnTouchOutside(false);


                    stringa="";
                    grizie("Thanks for your donation!");

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            dialog.dismiss();
                        }
                    }, 1500);
                    //stripe();
                    deleteDialog.dismiss();
                    dialog.show();
                }

                else  Toast.makeText(ImpostazioniActivity.this,
                        "Insert an amount",
                        Toast.LENGTH_SHORT).show();
            }
        });
        deleteDialogView.findViewById(R.id.annulla2_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });
        deleteDialog.setCanceledOnTouchOutside(false);

        deleteDialog.show(); }

    public void alertAd() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.alert_no_ads, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        mCardInputWidget = (CardInputWidget) deleteDialogView.findViewById(R.id.card_input_widget);
        deleteDialog.setView(deleteDialogView);
        deleteDialogView.findViewById(R.id.rimuovi_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic

                stringa="rimozione_pubb";
                dialog.setMessage("Payment in progress...");
                dialog.setCanceledOnTouchOutside(false);
                ref.child("Users").child(user.getDisplayName()).child("premium").setValue(true);

                stringa="";
                grizie("Thanks, we have succesfully remove the ads!");

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        dialog.dismiss();
                    }
                }, 1500);
                //stripe();
                deleteDialog.dismiss();
                dialog.show();
            }
        });
        deleteDialogView.findViewById(R.id.annulla2_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });
        deleteDialog.setCanceledOnTouchOutside(false);

        deleteDialog.show(); }

    public void accediAlert() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.accedi_alert, null);
        final TextView taskEditText=(TextView) deleteDialogView.findViewById(R.id.text);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);
        deleteDialogView.findViewById(R.id.accedi_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                deleteDialog.dismiss();
                Intent i=new Intent(ImpostazioniActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        deleteDialogView.findViewById(R.id.annulla_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                deleteDialog.dismiss();
            }
        });

        deleteDialog.show(); }





    public boolean isCityGood() {
        int i=0;
        String città=automatictext.getText().toString();
        for(; i<cityList.length;i++) {
            if (città.equals(cityList[i])) return true;
        } return false; }




    public void createAlert() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.alert_pref_city, null);
        cityList=getResources().getStringArray(R.array.city_list);
        automatictext= (AutoCompleteTextView) deleteDialogView.findViewById(R.id.sceglicittà);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,cityList);
        automatictext.setAdapter(adapter);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);
        deleteDialogView.findViewById(R.id.conferma2_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean test=isCityGood();

                if (test) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

                    ref.child("NomiCittà").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            boolean sonoinutile=false;
                            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                City città = postSnapshot.getValue(City.class);
                                if (automatictext.getText().toString().equals(città.getCity())) {
                                    @SuppressLint("WrongConstant") SharedPreferences pref = getSharedPreferences("cittapredef", 10);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("cittapredefinita", automatictext.getText().toString());
                                    editor.apply();
                                    sonoinutile=true;
                                    Toast.makeText(ImpostazioniActivity.this, "City set correctly", Toast.LENGTH_SHORT).show();
                                    deleteDialog.dismiss();
                                }
                            }
                            if (!sonoinutile) {
                                Toast.makeText(ImpostazioniActivity.this, "City not present in the database", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                else {
                    Toast.makeText(ImpostazioniActivity.this,
                            "Select one of the city suggested",
                            Toast.LENGTH_SHORT).show(); } } });
        deleteDialogView.findViewById(R.id.annulla2_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss(); }});

        deleteDialog.setCanceledOnTouchOutside(false);
        deleteDialog.show(); }


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

            }
        });

        deleteDialog.setCanceledOnTouchOutside(false);
        deleteDialog.show(); }

}


