package dev.peihana.yourbestlive;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class ReviewActivity extends AppCompatActivity implements View.OnClickListener, RatingBar.OnRatingBarChangeListener {

    private CheckBox original_check;
    private CheckBox   tribute_Check;
    private CheckBox   email_Check;
    private CheckBox   facebook_Check;
    private CheckBox   telephone_Check;
    private CheckBox   cachet_Check;
    private CheckBox   tickets_Check;
    private CheckBox   yback_check;
    private CheckBox   nback_check;
    private CheckBox   ycena_check;
    private CheckBox   ncena_check;
    private CheckBox   yes_check;
    private CheckBox   no_check;
    private CheckBox   poco_check;
    private CheckBox   medio_check;
    RatingBar ratingBar;
    private EditText commentText;
    private boolean usato=false;
    private ProgressDialog dialog;
    String genere_ref;
    int mixerr=0;
    int ampl11=0;
    int ampl22=0;
    int micc=0;
    int batteriaa=0;
    Button reg_button;
    int importo;
    int vecchio_minimo;
    int vecchio_massimo;
    private String city;
    private String via;
    private String nomeLocale;
    FirebaseUser user;
    DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
    TextView spinner_text;
    ImageButton dropDown;
    View spinner;
    EditText telefono;
    EditText facebook;
    EditText emaill;
    int cena_si=0;
    int cena_no=0;
    int pubblico_tanto=0;
    int pubblico_poco=0;
    int pubblico_medio=0;
    int pubblico_no=0;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {//this activity takes everything related to what the user selected and writes all in the db, which will be read by the recycler view in LocaleActivity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nomeLocale = extras.getString("nome_locale");
            city= extras.getString("città");
            // and get whatever type user account id is
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Review "+nomeLocale);
        user= FirebaseAuth.getInstance().getCurrentUser();
        genere_ref="";
        commentText=(EditText) findViewById(R.id.comment_edit);
        reg_button=(Button) findViewById(R.id.reg_local_button);
        original_check=(CheckBox) findViewById(R.id.Original_Check);
        tribute_Check=(CheckBox) findViewById(R.id.Tribute_Check);
        ycena_check=(CheckBox) findViewById(R.id.ycena_check);
        ncena_check=(CheckBox) findViewById(R.id.ncena_check);
        email_Check=(CheckBox) findViewById(R.id.Email_Check);
        facebook_Check=(CheckBox) findViewById(R.id.Facebook_Check);
        telephone_Check=(CheckBox) findViewById(R.id.Telephone_Check);
        cachet_Check=(CheckBox) findViewById(R.id.Cachet_Check);
        tickets_Check=(CheckBox) findViewById(R.id.Tickets_Check);
        yback_check=(CheckBox) findViewById(R.id.yback_check);
        nback_check=(CheckBox) findViewById(R.id.nback_check);
        no_check=(CheckBox) findViewById(R.id.no_check);
        yes_check=(CheckBox) findViewById(R.id.yes_check);
        facebook=(EditText) findViewById(R.id.Facebook);
        emaill=(EditText) findViewById(R.id.indirizzo_email);
        telefono=(EditText) findViewById(R.id.Telefono);
        poco_check=(CheckBox) findViewById(R.id.poco_check);
        medio_check=(CheckBox) findViewById(R.id.medio_check);
        original_check.setOnClickListener(this);
        medio_check.setOnClickListener(this);
        poco_check.setOnClickListener(this);
        tribute_Check.setOnClickListener(this);
        email_Check.setOnClickListener(this);
        facebook_Check.setOnClickListener(this);
        telephone_Check.setOnClickListener(this);
        cachet_Check.setOnClickListener(this);
        tickets_Check.setOnClickListener(this);
        yback_check.setOnClickListener(this);
        nback_check.setOnClickListener(this);
        yes_check.setOnClickListener(this);
        no_check.setOnClickListener(this);
        reg_button.setOnClickListener(this);
        ycena_check.setOnClickListener(this);
        ncena_check.setOnClickListener(this);
        dialog=new ProgressDialog(this);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(this);
        spinner=findViewById(R.id.spinner);
        spinner_text=(TextView) findViewById(R.id.spinner_text);
        dropDown=(ImageButton) findViewById(R.id.dropDown);
        dropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genereAlert();
            }
        });
        spinner.setOnClickListener(this);
        spinner_text.setText("  Seleziona un genere");
        vecchioMaxFunction();
        vecchioMinFunction();}

    public void genereAlert() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_genere);

        RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);

        RadioButton varioo=new RadioButton(this);
        varioo.setText("Vario");
        rg.addView(varioo);

        RadioButton hard1=new RadioButton(this);
        hard1.setText("Hard Rock");
        rg.addView(hard1);

        RadioButton jazz1=new RadioButton(this);
        jazz1.setText("Jazz/Blues");
        rg.addView(jazz1);

        RadioButton metal1=new RadioButton(this);
        metal1.setText("Metal");
        rg.addView(metal1);

        RadioButton indie1=new RadioButton(this);
        indie1.setText("Indie");
        rg.addView(indie1);

        RadioButton alternative1=new RadioButton(this);
        alternative1.setText("Alternative");
        rg.addView(alternative1);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int childCount = group.getChildCount();
                for (int x = 0; x < childCount; x++) {
                    RadioButton btn = (RadioButton) group.getChildAt(x);
                    if (btn.getId() == checkedId) {
                        if(btn.getText().toString().equals("Vario")) {
                            genere_ref="vario";
                            spinner_text.setText("Vario");
                        }
                        if(btn.getText().toString().equals("Hard Rock")) {
                            genere_ref="hard";
                            spinner_text.setText("Hard Rock");
                        }
                        if(btn.getText().toString().equals("Jazz/Blues")) {
                            genere_ref="jazz";
                            spinner_text.setText("Jazz/Blues");
                        }
                        if(btn.getText().toString().equals("Metal")) {
                            genere_ref="metal";
                            spinner_text.setText("Metal");
                        }
                        if(btn.getText().toString().equals("Indie")) {
                            genere_ref="indie";
                            spinner_text.setText("Indie");
                        }
                        if(btn.getText().toString().equals("Alternative")){
                            genere_ref="alternative";
                            spinner_text.setText("Alternative");
                        }
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask(){
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        }, 350);}}}});
        dialog.show();
    }



    public void backLineAlert() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.alert_backline, null);
        final EditText n_chitarre=(EditText) deleteDialogView.findViewById(R.id.n_chitarra);
        final EditText n_mic=(EditText) deleteDialogView.findViewById(R.id.n_microfoni);
        n_chitarre.setClickable(false);
        n_chitarre.setEnabled(false);
        n_mic.setClickable(false);
        n_mic.setEnabled(false);
        final CheckBox batteria=(CheckBox) deleteDialogView.findViewById(R.id.batteria);
        final CheckBox ampl1=(CheckBox) deleteDialogView.findViewById(R.id.ampl1);
        final CheckBox ampl2=(CheckBox) deleteDialogView.findViewById(R.id.ampl2);
        final CheckBox mic=(CheckBox) deleteDialogView.findViewById(R.id.microfoni);
        final CheckBox mixer=(CheckBox) deleteDialogView.findViewById(R.id.mixer);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);
        batteria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(batteria.isChecked())
                    batteriaa=1;
                else batteriaa=0;
            }
        });
        ampl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ampl1.isChecked()) {
                    n_chitarre.setEnabled(true);
                    n_chitarre.setClickable(true); }

                else {
                    n_chitarre.setEnabled(false);
                    n_chitarre.setClickable(false);
                    ampl11=0;} }

        });
        ampl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ampl2.isChecked())
                    ampl22=1;
                else ampl22=0;
            }
        });
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mic.isChecked()) {
                    n_mic.setEnabled(true);
                    n_mic.setClickable(true);}
                else {
                    n_mic.setEnabled(false);
                    n_mic.setClickable(false);
                    micc=0;
                }
            }
        });
        mixer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mixer.isChecked())
                    mixerr=1;
                else mixerr=0;

            }
        });
        deleteDialogView.findViewById(R.id.conferma_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!n_chitarre.getText().toString().equals("")) {
                    if(Integer.parseInt(n_chitarre.getText().toString())>9) {
                        Toast.makeText(ReviewActivity.this,
                                "Number too big",
                                Toast.LENGTH_SHORT).show();
                    } }
                else if(!n_mic.getText().toString().equals("")) {
                    if(Integer.parseInt(n_mic.getText().toString())>9) {
                        Toast.makeText(ReviewActivity.this,
                                "Number too big",
                                Toast.LENGTH_SHORT).show();
                    } }
                else if(ampl1.isChecked() && n_chitarre.getText().toString().trim().length() == 0) {
                    Toast.makeText(ReviewActivity.this,
                            "Insert the number of guitar amplificators",
                            Toast.LENGTH_SHORT).show();
                }
                else if(mic.isChecked() && n_mic.getText().toString().trim().length() == 0) {
                    Toast.makeText(ReviewActivity.this,
                            "Insert number of mics",
                            Toast.LENGTH_SHORT).show();
                }

                if(ampl1.isChecked()) {
                    if(n_chitarre.getText().toString().trim().length() != 0) {
                        ampl11=Integer.parseInt(n_chitarre.getText().toString());
                        deleteDialog.dismiss();
                    } }
                if(mic.isChecked()) {
                    if(n_mic.getText().toString().trim().length() != 0) {
                        micc=Integer.parseInt(n_mic.getText().toString());
                        deleteDialog.dismiss();
                    } }
                else deleteDialog.dismiss();


            }
        });
        deleteDialogView.findViewById(R.id.annulla_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yback_check.setChecked(false);
                batteriaa=0;
                micc=0;
                ampl11=0;
                ampl22=0;
                mixerr=0;
                deleteDialog.dismiss();
            }
        });
        deleteDialog.setCanceledOnTouchOutside(false);
        dialog.setCanceledOnTouchOutside(false);
        deleteDialog.show(); }



    public void tipoBand1() {
        tribute_Check.setChecked(false);}
    public void tipoBand2() {
        original_check.setChecked(false);}
    public void tipoContatto1() {
        facebook_Check.setChecked(false);
        telephone_Check.setChecked(false);}
    public void tipoContatto2() {
        email_Check.setChecked(false);
        telephone_Check.setChecked(false);}
    public void tipoContatto3() {
        facebook_Check.setChecked(false);
        email_Check.setChecked(false);}
    public void tipoPagamento1() {tickets_Check.setChecked(false);
        importo=-1;}
    public void tipoPagamento2() {cachet_Check.setChecked(false);}
    public void tipoBackline1() {
        nback_check.setChecked(false);
        backLineAlert();
    }
    public void tipoBackline2() {
        yback_check.setChecked(false);
    }
    public void tipoPubblicoTanto() {
        no_check.setChecked(false);
        poco_check.setChecked(false);
        medio_check.setChecked(false);
        pubblico_tanto=1;
        pubblico_no=0;
        pubblico_medio=0;
        pubblico_poco=0;}
    public void tipoPubblicoNo() {
        yes_check.setChecked(false);
        poco_check.setChecked(false);
        medio_check.setChecked(false);
        pubblico_tanto=0;
        pubblico_no=1;
        pubblico_medio=0;
        pubblico_poco=0;}
    public void tipoPubblicoMedio() {
        no_check.setChecked(false);
        poco_check.setChecked(false);
        yes_check.setChecked(false);
        pubblico_tanto=0;
        pubblico_no=0;
        pubblico_medio=1;
        pubblico_poco=0;}
    public void tipoPubblicoPoco() {
        yes_check.setChecked(false);
        no_check.setChecked(false);
        medio_check.setChecked(false);
        pubblico_tanto=0;
        pubblico_no=0;
        pubblico_medio=0;
        pubblico_poco=1;}
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromTouch) {
        usato=true;
    }
    public void cena1() {
        ycena_check.setChecked(false);
        cena_no=1;
        cena_si=0;}
    public void cena2() {
        ncena_check.setChecked(false);
        cena_si=1;
        cena_no=0;}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


    ////incrementa contatore
    public void incrementa(final String string, final String string2) {
        ref.child("Città").child(city).child(nomeLocale).child(string).child(string2).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int value=snapshot.getValue(Integer.class);
                ref.child("Città").child(city).child(nomeLocale).child(string).child(string2).setValue(value+1);}
            @Override
            public void onCancelled(DatabaseError firebaseError) { } }); }

    //incrementa contatore livello superiore
    public void incrementa1(final String string) {
        ref.child("Città").child(city).child(nomeLocale).child(string).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int value=snapshot.getValue(Integer.class);
                ref.child("Città").child(city).child(nomeLocale).child(string).setValue(value+1);}
            @Override
            public void onCancelled(DatabaseError firebaseError) { } }); }

    //somma rate livello superiore
    public void somma(final String string) {
        ref.child("Città").child(city).child(nomeLocale).child(string).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int value=snapshot.getValue(Integer.class);
                ref.child("Città").child(city).child(nomeLocale).child(string).setValue(value+ratingBar.getRating());}
            @Override
            public void onCancelled(DatabaseError firebaseError) { } }); }

    //setta band
    public void bandFunction(final String string, final String string2) {
        ref.child("Città").child(city).child(nomeLocale).child(string).child(string2).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String value=snapshot.getValue(String.class);
                if(value.equals("")) {
                    ref.child("Città").child(city).child(nomeLocale).child(string).child(string2).setValue("si");} }
            @Override
            public void onCancelled(DatabaseError firebaseError) { } }); }

    //setta tipo pagamento
    public void pagamentoFunction(final String string, final String string2, int vecchio_max, final int vecchio_min) {
        ref.child("Città").child(city).child(nomeLocale).child(string).child(string2).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String value=snapshot.getValue(String.class);
                if(string2.equals("ticket")) {
                    ref.child("Città").child(city).child(nomeLocale).child(string).child(string2).setValue("si");}
                if(string2.equals("fisso")) {
                    ref.child("Città").child(city).child(nomeLocale).child(string).child(string2).setValue("si");
                    if(importo>vecchio_massimo && importo>-1) {
                        ref.child("Città").child(city).child(nomeLocale).child(string).child("importo_max").setValue(importo);}
                    if((importo<vecchio_minimo && importo>-1) || vecchio_minimo==-1) {
                        ref.child("Città").child(city).child(nomeLocale).child(string).child("importo_min").setValue(importo);} } }

            @Override
            public void onCancelled(DatabaseError firebaseError) { } });  }

    //setta tipo contatto
    public void contattoFunction(final String string, final String string2) {
        ref.child("Città").child(city).child(nomeLocale).child(string).child(string2).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(string2.equals("telefono")) {
                    int value=snapshot.getValue(Integer.class);
                    ref.child("Città").child(city).child(nomeLocale).child(string).child(string2).setValue(value+1);}
                if(string2.equals("email")) {
                    int value=snapshot.getValue(Integer.class);
                    ref.child("Città").child(city).child(nomeLocale).child(string).child(string2).setValue(value+1);}
                if(string2.equals("facebook")) {
                    int value=snapshot.getValue(Integer.class);
                    ref.child("Città").child(city).child(nomeLocale).child(string).child(string2).setValue(value+1);}}
            @Override
            public void onCancelled(DatabaseError firebaseError) { } }); }


    //ritorna pagamento vecchio massimo
    public void vecchioMaxFunction() {
        ref.child("Città").child(city).child(nomeLocale).child("pagamento").child("importo_max").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                vecchio_massimo=snapshot.getValue(Integer.class); }
            @Override
            public void onCancelled(DatabaseError firebaseError) { } });  }

    //ritorna pagamento vecchio minimo
    public void vecchioMinFunction() {
        ref.child("Città").child(city).child(nomeLocale).child("pagamento").child("importo_min").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                vecchio_minimo=snapshot.getValue(Integer.class); }
            @Override
            public void onCancelled(DatabaseError firebaseError) { } });  }

    public void setGenere() {

        ref.child("Città").child(city).child(nomeLocale).child("genere").child(genere_ref).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int a=dataSnapshot.getValue(Integer.class);
                a++;
                ref.child("Città").child(city).child(nomeLocale).child("genere").child(genere_ref).setValue(a);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //crea dialog del cachet
    public void createAlert() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.alert_importo, null);
        final EditText taskEditText=(EditText) deleteDialogView.findViewById(R.id.text);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);
        deleteDialogView.findViewById(R.id.dona_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((taskEditText.getText().toString().equals(""))) { Toast.makeText(ReviewActivity.this,
                        "Insert a valid amount",
                        Toast.LENGTH_SHORT).show(); }
                else {
                    importo=Integer.parseInt(taskEditText.getText().toString());
                    deleteDialog.dismiss(); }

            }
        });
        deleteDialogView.findViewById(R.id.annulla2_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cachet_Check.setChecked(false);

                deleteDialog.dismiss();
            }
        });
        deleteDialog.setCanceledOnTouchOutside(false);
        dialog.setCanceledOnTouchOutside(false);
        deleteDialog.show(); }


    public void eseguiCommento(String commento, String utente) {

        final Map<String, Object> commenti_list = new HashMap<String, Object>();
        final Map<String, Object> utente_list = new HashMap<String, Object>();

        commenti_list.put("commento",commento);
        utente_list.put("utente",utente);

        ref.child("Città").child(city).child(nomeLocale).child("commenti").child(utente).updateChildren(commenti_list);
        ref.child("Città").child(city).child(nomeLocale).child("commenti").child(utente).updateChildren(utente_list);
    }

    public void setEmail() {
        ref.child("Città").child(city).child(nomeLocale).child("indirizzo_email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String value=snapshot.getValue(String.class);
                if(value.equals("")) {
                    ref.child("Città").child(city).child(nomeLocale).child("indirizzo_email").setValue(emaill.getText().toString());} }
            @Override
            public void onCancelled(DatabaseError firebaseError) { } }); }

    public void setFacebook() {
        ref.child("Città").child(city).child(nomeLocale).child("pagina_facebook").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String value=snapshot.getValue(String.class);
                if(value.equals("")) {
                    ref.child("Città").child(city).child(nomeLocale).child("pagina_facebook").setValue(facebook.getText().toString());} }
            @Override
            public void onCancelled(DatabaseError firebaseError) { } }); }

    public void setTelefono() {
        ref.child("Città").child(city).child(nomeLocale).child("numero_telefono").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String value=snapshot.getValue(String.class);
                if(value.equals("")) {
                    ref.child("Città").child(city).child(nomeLocale).child("numero_telefono").setValue(telefono.getText().toString());} }
            @Override
            public void onCancelled(DatabaseError firebaseError) { } }); }

    public void backLineFunction() {
    ref.child("Città").child(city).child(nomeLocale).child("backline").addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            int batteria=dataSnapshot.child("batteria").getValue(Integer.class);
            int ampl_chitarra=dataSnapshot.child("ampl_chitarra").getValue(Integer.class);
            int ampl_basso=dataSnapshot.child("ampl_basso").getValue(Integer.class);
            int microfoni=dataSnapshot.child("microfoni").getValue(Integer.class);
            int mixer=dataSnapshot.child("mixer").getValue(Integer.class);

            if(batteriaa!=0 && batteria==0) ref.child("Città").child(city).child(nomeLocale).child("backline").child("batteria").setValue(batteriaa);
            if(ampl22!=0 && ampl_basso==0) ref.child("Città").child(city).child(nomeLocale).child("backline").child("ampl_basso").setValue(ampl22);
            if(mixerr!=0 && mixer==0) ref.child("Città").child(city).child(nomeLocale).child("backline").child("mixer").setValue(mixerr);
            if(ampl11>ampl_chitarra) ref.child("Città").child(city).child(nomeLocale).child("backline").child("ampl_chitarra").setValue(ampl11);
            if(micc>microfoni) ref.child("Città").child(city).child(nomeLocale).child("backline").child("microfoni").setValue(micc);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
}

    public void cena() {
        ref.child("Città").child(city).child(nomeLocale).child("cena").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int si=dataSnapshot.child("si").getValue(Integer.class);
                int no=dataSnapshot.child("no").getValue(Integer.class);

                if(cena_si==1)  {
                    si++;
                    ref.child("Città").child(city).child(nomeLocale).child("cena").child("si").setValue(si);
                }
                if(cena_no==1)  {
                    no++;
                    ref.child("Città").child(city).child(nomeLocale).child("cena").child("no").setValue(no);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void pubblicoAllFuction() {
        ref.child("Città").child(city).child(nomeLocale).child("pubblico").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int tanto=dataSnapshot.child("tanto").getValue(Integer.class);
                int medio=dataSnapshot.child("medio").getValue(Integer.class);
                int poco=dataSnapshot.child("poco").getValue(Integer.class);
                int no=dataSnapshot.child("no").getValue(Integer.class);

                if(pubblico_tanto==1)  {
                    tanto++;
                    ref.child("Città").child(city).child(nomeLocale).child("pubblico").child("tanto").setValue(tanto);
                }
                if(pubblico_medio==1)  {
                    medio++;
                    ref.child("Città").child(city).child(nomeLocale).child("pubblico").child("medio").setValue(medio);
                }
                if(pubblico_poco==1)  {
                    poco++;
                    ref.child("Città").child(city).child(nomeLocale).child("pubblico").child("poco").setValue(poco);
                }
                if(pubblico_no==1)  {
                    no++;
                    ref.child("Città").child(city).child(nomeLocale).child("pubblico").child("no").setValue(no);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void onClick(View v) {
        if (v == reg_button) {
            if (!commentText.getText().toString().isEmpty() && ratingBar.getRating()==0) {
                Toast.makeText(ReviewActivity.this,
                        "To leave a comment, enter also a star rating",
                        Toast.LENGTH_SHORT).show();
            }
            else {
            dialog.setMessage("Sending in progress..");
            dialog.show();
            somma("rate");
            if (!commentText.getText().toString().isEmpty() && user!=null) {
                eseguiCommento(commentText.getText().toString(),user.getDisplayName());
            }
            if(cachet_Check.isChecked()) {
                pagamentoFunction("pagamento","fisso",vecchio_massimo,vecchio_minimo); }
            if(tickets_Check.isChecked()) {
                pagamentoFunction("pagamento","ticket",vecchio_massimo,vecchio_minimo); }
            if(tribute_Check.isChecked()) {
                bandFunction("band","tribute"); }
            if(yback_check.isChecked()) {
                backLineFunction();
            }
            if(ncena_check.isChecked()) {
                cena(); }
            if(ycena_check.isChecked()) {
                cena(); }
            if(original_check.isChecked()) {
                bandFunction("band","originale"); }
            if(yes_check.isChecked() || no_check.isChecked() || medio_check.isChecked() || poco_check.isChecked()) {
                pubblicoAllFuction(); }
            if(facebook_Check.isChecked()) {
                contattoFunction("contatto","facebook"); }
            if (ratingBar.getRating()!=0) {
                final Map<String, Object> voto_list= new HashMap<String,Object>();
                final Map<String, Object> utente_list= new HashMap<>();
                voto_list.put("stelle",ratingBar.getRating());
                utente_list.put("utente",user.getDisplayName());
                ref.child("Città").child(city).child(nomeLocale).child("commenti").child(user.getDisplayName()).updateChildren(voto_list);
                ref.child("Città").child(city).child(nomeLocale).child("commenti").child(user.getDisplayName()).updateChildren(utente_list);
            }
            if(!genere_ref.equals("")) {
                setGenere();
            }
            if(telephone_Check.isChecked()) {
                contattoFunction("contatto","telefono"); }
            if(email_Check.isChecked()) {
                contattoFunction("contatto","email"); }
            if(usato) incrementa1("iterazioni");
            setEmail(); setFacebook(); setTelefono();
            Toast.makeText(ReviewActivity.this,
                    "Completed",
                    Toast.LENGTH_SHORT).show();
            ref.child("Città").child(city).child(nomeLocale).child("totale_recensioni").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int a=dataSnapshot.getValue(Integer.class);
                    a++;
                    ref.child("Città").child(city).child(nomeLocale).child("totale_recensioni").setValue(a);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            //salva i locali recensiti dagli utenti
            ref.child("Users").child(user.getDisplayName()).child("Recensioni").child(city).child(nomeLocale).child(nomeLocale).setValue(nomeLocale);
            dialog.hide();
            finish(); } }


        if(v==tribute_Check) { tipoBand2(); }
        if(v==original_check) { tipoBand1(); }
        if(v==email_Check) { tipoContatto1(); }
        if(v==facebook_Check) { tipoContatto2(); }
        if(v==telephone_Check) { tipoContatto3(); }
        if(v==cachet_Check) { tipoPagamento1(); createAlert();}
        if(v==tickets_Check) { tipoPagamento2(); }
        if(v==yback_check) { tipoBackline1(); }
        if(v==nback_check) { tipoBackline2(); }
        if(v==ncena_check) { cena1();   }
        if(v==ycena_check) { cena2();   }
        if(v==yes_check) {tipoPubblicoTanto(); }
        if(v==no_check) {tipoPubblicoNo(); }
        if(v==medio_check) {tipoPubblicoMedio(); }
        if(v==poco_check) {tipoPubblicoPoco(); }
        if(v==spinner) { genereAlert(); }


    } }





