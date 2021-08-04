package dev.peihana.yourbestlive;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class LocalRegister extends AppCompatActivity implements View.OnClickListener, RatingBar.OnRatingBarChangeListener {


    int vario=0;
    int hard=0;
    int jazz=0;
    int alternative=0;
    int metal=0;
    int indie=0;

    private EditText localNameText;
    private EditText viaText;
    private Button reg_local_button;
    String telefono_string="";
    String facebook_string="";
    String email_string="";
    AutoCompleteTextView cityText;
    String [] cityList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref1=database.getReference("Users");
    DatabaseReference ref = database.getReference("Città");
    DatabaseReference ref_città = database.getReference();
    FirebaseUser user;
    private RatingBar ratingBar;
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
    private EditText commentText;
    private String band1="";
    private String band2="";
    int conttato_num1=0;
    int conttato_num2=0;
    int conttato_num3=0;
    private String pagamento1="";
    private String pagamento2="";
    private String genere="";
    private String city;
    private String via;
    private String nomeLocale;
    private ProgressDialog dialog;
    private float rate=0;
    private float num_rate=0;
    private int importo_min=-1;
    private int importo_max=-1;
    int backline_si=0;
    int backline_no=0;
    int pubblico_tanto=0;
    int pubblico_poco=0;
    int pubblico_medio=0;
    int pubblico_no=0;
    TextView spinner_text;
    ImageButton dropDown;
    View spinner;
    int cena_si=0;
    int cena_no=0;
    EditText telefono;
    EditText facebook;
    EditText emaill;
    String url= "https://firebasestorage.googleapis.com/v0/b/yourbestlive-bb0bd.appspot.com/o/Noimage.jpg?alt=media&token=cd4d2479-4d02-479b-b5bf-f0b96074c358";
    int batteriaa=0;
    int micc=0;
    int mixerr=0;
    int ampl11=0;
    int ampl22=0;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Register a club");
        user = FirebaseAuth.getInstance().getCurrentUser();
        Intent i=getIntent();
        cityText=(AutoCompleteTextView) findViewById(R.id.cityName);
        cityList=getResources().getStringArray(R.array.city_list);
        localNameText=(EditText) findViewById(R.id.localName);
        viaText=(EditText) findViewById(R.id.via);
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
        spinner_text.setText("Select a genre");
        reg_local_button=(Button) findViewById(R.id. reg_local_button);
        reg_local_button.setOnClickListener(this);
        facebook=(EditText) findViewById(R.id.Facebook);
        emaill=(EditText) findViewById(R.id.indirizzo_email);
        telefono=(EditText) findViewById(R.id.Telefono);
        commentText=(EditText) findViewById(R.id.comment_edit);
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
        poco_check=(CheckBox) findViewById(R.id.poco_check);
        medio_check=(CheckBox) findViewById(R.id.medio_check);
        original_check.setOnClickListener(this);
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
        ycena_check.setOnClickListener(this);
        ncena_check.setOnClickListener(this);
        medio_check.setOnClickListener(this);
        poco_check.setOnClickListener(this);
        dialog=new ProgressDialog(this);

        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(this);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,cityList);
        cityText.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


    public boolean isCityGood() {
        int i=0;
        String città=cityText.getText().toString();
        for(; i<cityList.length;i++) {
            if (città.equals(cityList[i])) return true;
        } return false; }

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

                if((taskEditText.getText().toString().equals(""))) { Toast.makeText(LocalRegister.this,
                        "Please enter a valid amount",
                        Toast.LENGTH_SHORT).show(); }
                else {
                    importo_min=Integer.parseInt(taskEditText.getText().toString());
                    importo_max=importo_min;
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
                        Toast.makeText(LocalRegister.this,
                            "Please enter a smaller number",
                            Toast.LENGTH_SHORT).show();
                } }
                else if(!n_mic.getText().toString().equals("")) {
                    if(Integer.parseInt(n_mic.getText().toString())>9) {
                        Toast.makeText(LocalRegister.this,
                            "Please enter a smaller number",
                            Toast.LENGTH_SHORT).show();
                } }
                else if(ampl1.isChecked() && n_chitarre.getText().toString().trim().length() == 0) {
                   Toast.makeText(LocalRegister.this,
                           "Enter the number of guitar amplifiers",
                           Toast.LENGTH_SHORT).show();
               }
                else if(mic.isChecked() && n_mic.getText().toString().trim().length() == 0) {
                    Toast.makeText(LocalRegister.this,
                            "Enter the number of microphones",
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
                            vario=1;
                            spinner_text.setText("Vario");
                        }
                        if(btn.getText().toString().equals("Hard Rock")) {
                            hard=1;
                            spinner_text.setText("Hard Rock");
                        }
                        if(btn.getText().toString().equals("Jazz/Blues")) {
                            jazz=1;
                            spinner_text.setText("Jazz/Blues");
                        }
                        if(btn.getText().toString().equals("Metal")) {
                            metal=1;
                            spinner_text.setText("Metal");
                        }
                        if(btn.getText().toString().equals("Indie")) {
                            indie=1;
                            spinner_text.setText("Indie");
                        }
                        if(btn.getText().toString().equals("Alternative")){
                            alternative=1;
                            spinner_text.setText("Alternative");
                        }
                                Timer timer = new Timer();
                                timer.schedule(new TimerTask(){
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        }, 350);}}}});
        dialog.show(); }



    //funzioni di tutti i checkbox
    public void tipoBand1() {
        tribute_Check.setChecked(false);
        band1="si";
        band2="";}
    public void tipoBand2() {
        original_check.setChecked(false);
        band2="si";
        band1=""; }
    public void cena1() {
        ycena_check.setChecked(false);
        cena_no=1;
        cena_si=0;}
    public void cena2() {
        ncena_check.setChecked(false);
        cena_si=1;
        cena_no=0;}
    public void tipoContatto1() {
        facebook_Check.setChecked(false);
        telephone_Check.setChecked(false);
        conttato_num1=1;}
    public void tipoContatto2() {
        email_Check.setChecked(false);
        telephone_Check.setChecked(false);
        conttato_num2=1;}
    public void tipoContatto3() {
        facebook_Check.setChecked(false);
        email_Check.setChecked(false);
        conttato_num3 = 1;
    }
    public void tipoPagamento1() {
        tickets_Check.setChecked(false);
        pagamento1="si";
        pagamento2="";}
    public void tipoPagamento2() {
        cachet_Check.setChecked(false);
        pagamento2="si";
        importo_max=-1;
        importo_min=-1;
        pagamento1="";}
    public void tipoBackline1() {
        nback_check.setChecked(false);
        backline_si=1;
    }
    public void tipoBackline2() {
        yback_check.setChecked(false);
        backline_no=1;
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
        rate=ratingBar.getRating();
        num_rate=1;
    }

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    //salva nel database
    public void saveDatabase() {

        city=cityText.getText().toString();
        via=viaText.getText().toString();
        nomeLocale=capitalizeString(localNameText.getText().toString());
        facebook_string=facebook.getText().toString();
        email_string=emaill.getText().toString();
        telefono_string=telefono.getText().toString();
        dialog.setMessage("Registration in progress..");
        dialog.show();
        ref.child(city).child(nomeLocale).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    Toast.makeText(LocalRegister.this,
                            "Club already registered or name already in use\n try to change it slightly",
                            Toast.LENGTH_SHORT).show();
                    dialog.hide();
                }
                else {
                    if (!commentText.getText().toString().isEmpty() && ratingBar.getRating()==0) {
                        Toast.makeText(LocalRegister.this,
                                "To leave a comment, insert also a star rating",
                                Toast.LENGTH_SHORT).show();
                        dialog.hide();
                    }
                    else if (TextUtils.isEmpty(nomeLocale)) {
                        Toast.makeText(LocalRegister.this,
                                "Insert the name of the club",
                                Toast.LENGTH_SHORT).show();
                        dialog.hide(); }
                    else if (TextUtils.isEmpty(city)) {
                        Toast.makeText(LocalRegister.this,
                                "Insert a city",
                                Toast.LENGTH_SHORT).show();
                        dialog.hide(); }
                    else if (TextUtils.isEmpty(via)) {
                        Toast.makeText(LocalRegister.this,
                                "Insert an address",
                                Toast.LENGTH_SHORT).show();
                        dialog.hide(); }
                    else if (!isCityGood()) {
                        Toast.makeText(LocalRegister.this,
                                "Select one of the city suggested",
                                Toast.LENGTH_SHORT).show();
                        dialog.hide(); }
                    else {

                        final Map<String, Object> citta_list = new HashMap<String, Object>();
                        final Map<String, Object> nomeLocale_list = new HashMap<String, Object>();
                        final Map<String, Object> via_list = new HashMap<String, Object>();

                        final Map<String, Object> band_list1 = new HashMap<String, Object>();
                        final Map<String, Object> band_list2 = new HashMap<String, Object>();

                        final Map<String, Object> contatto_list1 = new HashMap<String, Object>();
                        final Map<String, Object> contatto_list2 = new HashMap<String, Object>();
                        final Map<String, Object> contatto_list3 = new HashMap<String, Object>();

                        final Map<String, Object> pagamento_list1 = new HashMap<String, Object>();
                        final Map<String, Object> pagamento_list2 = new HashMap<String, Object>();
                        final Map<String,Object> imageurl_list= new HashMap<String, Object>();

                        final Map<String, Object> vario_list = new HashMap<String, Object>();
                        final Map<String, Object> hard_list = new HashMap<String, Object>();
                        final Map<String, Object> indie_list = new HashMap<String, Object>();
                        final Map<String, Object> metal_list = new HashMap<String, Object>();
                        final Map<String, Object> jazz_list = new HashMap<String, Object>();
                        final Map<String, Object> alternative_list = new HashMap<String, Object>();

                        final Map<String, Object> rating_list = new HashMap<String, Object>();
                        final Map<String, Object> num_rating_list = new HashMap<String, Object>();
                        final Map<String, Object> importo_list1 = new HashMap<String, Object>();
                        final Map<String, Object> importo_list2 = new HashMap<String, Object>();

                        final Map<String, Object> pagina_facebbok_num_list = new HashMap<String, Object>();
                        final Map<String, Object> indirizzo_email_num_list = new HashMap<String, Object>();
                        final Map<String, Object> numero_telefono_num_list = new HashMap<String, Object>();

                        final Map<String, Object> commenti_list = new HashMap<String, Object>();
                        final Map<String, Object> utente_list = new HashMap<String, Object>();
                        final Map<String, Object> stelle_list = new HashMap<String, Object>();
                        final Map<String, Object> username_creatore_list= new HashMap<String, Object>();
                        final Map<String, Object> locale_sponso_list= new HashMap<String, Object>();


                        if (!commentText.getText().toString().isEmpty()) {
                            commenti_list.put("commento",commentText.getText().toString());
                            utente_list.put("utente", (user.getDisplayName()));
                            stelle_list.put("stelle",rate);
                        }
                        if (rate!=0) {
                            stelle_list.put("stelle",rate);
                            utente_list.put("utente", (user.getDisplayName()));
                        }

                        citta_list.put("city",city);
                        nomeLocale_list.put("nome_locale", nomeLocale);
                        pagina_facebbok_num_list.put("pagina_facebook",facebook_string);
                        indirizzo_email_num_list.put("indirizzo_email",email_string);
                        numero_telefono_num_list.put("numero_telefono",telefono_string);
                        imageurl_list.put("imageUrl",url);

                        via_list.put("via", via);
                        band_list1.put("originale",band1);
                        band_list2.put("tribute",band2);
                        contatto_list1.put("email",conttato_num1);
                        contatto_list1.put("facebook",conttato_num2);
                        contatto_list1.put("telefono",conttato_num3);
                        pagamento_list1.put("fisso", pagamento1);
                        pagamento_list2.put("ticket",pagamento2);
                        vario_list.put("vario",vario);
                        hard_list.put("hard",hard);
                        jazz_list.put("jazz",jazz);
                        metal_list.put("metal",metal);
                        alternative_list.put("alternative",alternative);
                        indie_list.put("indie", indie);
                        rating_list.put("rate",rate);
                        num_rating_list.put("iterazioni",num_rate);
                        importo_list1.put("importo_min",importo_min);
                        importo_list2.put("importo_max",importo_max);
                        username_creatore_list.put("nome_creatore",user.getDisplayName());
                        locale_sponso_list.put("sponsorizzato",1);


                        if(!via.contains("via") || !via.contains("via") || !via.contains("piazza") || !via.contains("Piazza") ) via="via "+via;
                        String indirizzoc= via+" "+city;
                        Geocoder gc= new Geocoder(getApplicationContext());
                        List<Address> list= null;
                            try {
                                list = gc.getFromLocationName(indirizzoc,1);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Address address= list.get(0);
                            Map<String,Object> testx= new HashMap<>();
                            Map<String,Object> testy= new HashMap<>();
                            Map<String,Object> locationtrovata= new HashMap<>();
                            Double x=address.getLatitude();
                            Double y=address.getLongitude();
                            if(x!=0 && y!=0) {
                                testx.put("locX",x);
                                testy.put("locY",y);
                                // locationtrovata.put("indirizzotrovato",address.getAddressLine(0)+" "+ address.getLocality());
                                ref.child(city).child(nomeLocale).child("location").updateChildren(testx);
                                ref.child(city).child(nomeLocale).child("location").updateChildren(testy); }



                       // ref.child(city).child(nomeLocale).child("location").updateChildren(locationtrovata); }




                        ref_città.child("NomiCittà").child(city).updateChildren(citta_list);
                        ref.child(city).child(nomeLocale).child("commenti").child(user.getDisplayName()).updateChildren(commenti_list);
                        ref.child(city).child(nomeLocale).child("commenti").child(user.getDisplayName()).updateChildren(utente_list);
                        ref.child(city).child(nomeLocale).child("commenti").child(user.getDisplayName()).updateChildren(stelle_list);
                        ref.child(city).child(nomeLocale).updateChildren(nomeLocale_list);
                        ref.child(city).child(nomeLocale).updateChildren(via_list);
                        ref.child(city).child(nomeLocale).updateChildren(rating_list);
                        ref.child(city).child(nomeLocale).updateChildren(num_rating_list);
                        ref.child(city).child(nomeLocale).updateChildren(pagina_facebbok_num_list);
                        ref.child(city).child(nomeLocale).updateChildren( indirizzo_email_num_list);
                        ref.child(city).child(nomeLocale).updateChildren(numero_telefono_num_list);
                        ref.child(city).child(nomeLocale).child("genere").updateChildren(vario_list);
                        ref.child(city).child(nomeLocale).child("genere").updateChildren(hard_list);
                        ref.child(city).child(nomeLocale).child("genere").updateChildren(jazz_list);
                        ref.child(city).child(nomeLocale).child("genere").updateChildren(metal_list);
                        ref.child(city).child(nomeLocale).child("genere").updateChildren(indie_list);
                        ref.child(city).child(nomeLocale).child("genere").updateChildren(alternative_list);
                        ref.child(city).child(nomeLocale).child("band").updateChildren(band_list1);
                        ref.child(city).child(nomeLocale).child("band").updateChildren(band_list2);
                        ref.child(city).child(nomeLocale).child("contatto").updateChildren(contatto_list1);
                        ref.child(city).child(nomeLocale).child("contatto").updateChildren(contatto_list2);
                        ref.child(city).child(nomeLocale).child("contatto").updateChildren(contatto_list3);
                        ref.child(city).child(nomeLocale).child("backline").child("batteria").setValue(batteriaa);
                        ref.child(city).child(nomeLocale).child("backline").child("ampl_chitarra").setValue(ampl11);
                        ref.child(city).child(nomeLocale).child("backline").child("ampl_basso").setValue(ampl22);
                        ref.child(city).child(nomeLocale).child("backline").child("mixer").setValue(mixerr);
                        ref.child(city).child(nomeLocale).child("backline").child("microfoni").setValue(micc);
                        ref.child(city).child(nomeLocale).child("pubblico").child("tanto").setValue(pubblico_tanto);
                        ref.child(city).child(nomeLocale).child("pubblico").child("poco").setValue(pubblico_poco);
                        ref.child(city).child(nomeLocale).child("pubblico").child("medio").setValue(pubblico_medio);
                        ref.child(city).child(nomeLocale).child("pubblico").child("no").setValue(pubblico_no);
                        ref.child(city).child(nomeLocale).child("pagamento").updateChildren(pagamento_list1);
                        ref.child(city).child(nomeLocale).child("pagamento").updateChildren(pagamento_list2);
                        ref.child(city).child(nomeLocale).child("pagamento").updateChildren(importo_list1);
                        ref.child(city).child(nomeLocale).child("pagamento").updateChildren(importo_list2);
                        ref.child(city).child(nomeLocale).child("totale_recensioni").setValue(1);
                        ref.child(city).child(nomeLocale).child("cena").child("si").setValue(cena_si);
                        ref.child(city).child(nomeLocale).child("cena").child("no").setValue(cena_no);
                        ref.child(city).child(nomeLocale).updateChildren(imageurl_list);
                        ref1.child(user.getDisplayName()).child("Recensioni").child(city).child(nomeLocale).child(nomeLocale).setValue(nomeLocale);
                        ref.child(city).child(nomeLocale).updateChildren(username_creatore_list);
                        ref.child(city).child(nomeLocale).updateChildren(locale_sponso_list);
                        Toast.makeText(LocalRegister.this,
                                "Club registered",
                                Toast.LENGTH_SHORT).show();
                        dialog.hide();
                        finish(); } }}


            @Override
            public void onCancelled(DatabaseError firebaseError) { } }); }

    @Override
    public void onClick(View v) {
        if(v==reg_local_button) { saveDatabase(); }
        if(v==tribute_Check) { tipoBand2(); }
        if(v==original_check) { tipoBand1(); }
        if(v==email_Check) { tipoContatto1(); }
        if(v==facebook_Check) { tipoContatto2(); }
        if(v==telephone_Check) { tipoContatto3(); }
        if(v==cachet_Check) { tipoPagamento1(); createAlert(); }
        if(v==tickets_Check) { tipoPagamento2(); }
        if(v==yback_check) { tipoBackline1(); backLineAlert(); }
        if(v==nback_check) { tipoBackline2(); }
        if(v==yes_check) {tipoPubblicoTanto(); }
        if(v==no_check) {tipoPubblicoNo(); }
        if(v==poco_check) {tipoPubblicoPoco(); }
        if(v==medio_check) {tipoPubblicoMedio(); }
        if(v==spinner) { genereAlert(); }
        if(v==ycena_check) { cena2(); }
        if(v==ncena_check) { cena1(); }

    } }
