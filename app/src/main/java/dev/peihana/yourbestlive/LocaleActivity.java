package dev.peihana.yourbestlive;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LocaleActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView Recycler;
    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference ref = database.getReference();
    public DatabaseReference ref2 = database.getReference();
    public DatabaseReference commenti;
    String creator;
    ProgressDialog dialogo;
    Button button_recensionii;
    Button delete_button;
    TextView contatto_risp;
    TextView pubblico_risp;
    TextView genere_risp;
    TextView backline_risp;
    TextView pagamento_risp;
    TextView band_risp;
    TextView min_text;
    TextView max_text;
    TextView via;
    TextView mic_risp;
    TextView mixer_risp;
    TextView batteria_risp;
    TextView ampl_chitarra_risp;
    TextView ampl_basso_risp;
    TextView cena_risp;
    String city;
    String nome_locale;
    FirebaseUser user;
    TextView via_text;
    ImageButton image_b;
    ImageButton facebook_icon;
    ImageButton email_icon;
    ImageButton phone_icon;
    RatingBar rating_bar;
    private InterstitialAd mInterstitialAd;
    TextView visualizza_altro;
    TextView visualizza_meno;
    String pubb = "";
    String indirizzoc;
    View ll;
    ImageView stars;
    Map<String, Float> map;
    HorizontalBarChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nome_locale = extras.getString("nome_locale");
            city = extras.getString("citta");
            // and get whatever type user account id is
        }
        setContentView(R.layout.activity_locale);
        delete_button = (Button) findViewById(R.id.b_delete);
        MobileAds.initialize(this, "ca-app-pub-8443545218197702~7746558476");
        mInterstitialAd = new InterstitialAd(this);
        //mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.setAdUnitId("ca-app-pub-8443545218197702/3955487279");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        //sets above toolbar and  collapsing logic
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(nome_locale);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        // Set title of Detail page
        collapsingToolbar.setTitle(nome_locale);


        ref.child("Città").child(city).child(nome_locale).child("imageUrl").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);
                ImageView immagine = (ImageView) findViewById(R.id.image);
                Picasso.with(getApplicationContext()).load(url).into(immagine);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //sets every icon under the image and the visualizza meno/altro button
        user = FirebaseAuth.getInstance().getCurrentUser();
        image_b = (ImageButton) findViewById(R.id.imageButton);
        phone_icon = (ImageButton) findViewById(R.id.phone_icon);
        min_text = (TextView) findViewById(R.id.min);
        visualizza_altro = (TextView) findViewById(R.id.visualizza_altro);
        ll = (View) findViewById(R.id.linear_invisible);
        visualizza_altro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll.setVisibility(View.VISIBLE);
                visualizza_altro.setVisibility(View.GONE);
            }
        });
        visualizza_meno = (TextView) findViewById(R.id.visualizza_meno);
        visualizza_meno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll.setVisibility(View.GONE);
                visualizza_altro.setVisibility(View.VISIBLE);
            }
        });
        max_text = (TextView) findViewById(R.id.max);
        facebook_icon = (ImageButton) findViewById(R.id.facebook_icon);
        email_icon = (ImageButton) findViewById(R.id.mail_icon);
        facebook_icon.setVisibility(View.GONE);
        phone_icon.setVisibility(View.GONE);
        email_icon.setVisibility(View.GONE);
        image_b.setOnClickListener(this);

        ads();

        if (user == null) {
            image_b.setTag(R.drawable.ic_bookmark1);
            image_b.setImageResource(R.drawable.ic_bookmark1);
        } else {

            ref.child("Città").child(city).child(nome_locale).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    creator = snapshot.child("nome_creatore").getValue(String.class);


                    if (creator.equals(user.getDisplayName())) {
                        delete_button.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onCancelled(DatabaseError firebaseError) {
                }
            });


            ref.child("Users").child(user.getDisplayName()).child("Preferiti").child(nome_locale + "_" + city).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        image_b.setTag(R.drawable.ic_bookmark2);
                        image_b.setImageResource(R.drawable.ic_bookmark2);
                    } else {
                        image_b.setTag(R.drawable.ic_bookmark1);
                        image_b.setImageResource(R.drawable.ic_bookmark1);
                    }


                }

                @Override
                public void onCancelled(DatabaseError firebaseError) {
                }
            });
        }

        ref.child("Città").child(city).child(nome_locale).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String facebook = snapshot.child("pagina_facebook").getValue(String.class);
                String telefono = snapshot.child("numero_telefono").getValue(String.class);
                String email = snapshot.child("indirizzo_email").getValue(String.class);

                if (!facebook.equals("")) {
                    facebook_icon.setVisibility(View.VISIBLE);
                    facebook_icon.setClickable(true);
                }

                if (!telefono.equals("")) {
                    phone_icon.setVisibility(View.VISIBLE);
                    phone_icon.setClickable(true);
                }

                if (!email.equals("")) {
                    email_icon.setVisibility(View.VISIBLE);
                    email_icon.setClickable(true);
                }

            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocaleActivity.this, RecyclerActivity.class);
                intent.putExtra("cittadina", city + "-" + nome_locale);
                startActivity(intent);
            }
        });

        facebook_icon.setOnClickListener(this);
        phone_icon.setOnClickListener(this);
        email_icon.setOnClickListener(this);
        dialogo = new ProgressDialog(this);
        via_text = (TextView) findViewById(R.id.via_text);
        rating_bar = (RatingBar) findViewById(R.id.add_rating);
        rating_bar.setStepSize(0.5f);
        button_recensionii = (Button) findViewById(R.id.b_review);
        button_recensionii.setOnClickListener(this);
        mic_risp = (TextView) findViewById(R.id.mic_risp);
        mixer_risp = (TextView) findViewById(R.id.mixer_risp);
        batteria_risp = (TextView) findViewById(R.id.batteria_risp);
        ampl_basso_risp = (TextView) findViewById(R.id.ampli_basso_risp);
        ampl_chitarra_risp = (TextView) findViewById(R.id.ampli_chitarra_risp);
        contatto_risp = (TextView) findViewById(R.id.contatto_risp);
        pubblico_risp = (TextView) findViewById(R.id.pubblico_risp);
        cena_risp = (TextView) findViewById(R.id.cena_risp);
        backline_risp = (TextView) findViewById(R.id.backline_risp);
        genere_risp = (TextView) findViewById(R.id.genere_risp);
        stars = (ImageView) findViewById(R.id.stars);
        band_risp = (TextView) findViewById(R.id.band_risp);
        pagamento_risp = (TextView) findViewById(R.id.pagamento_risp);
        via = (TextView) findViewById(R.id.via);
        dialogo.setMessage("Loading comments..");
        dialogo.show();
        iniziaCommenti();
        dialogo.hide();
        setBand();
        setGenere();
        setBackline();
        setPubblico();
        setContatto();
        setPagamento();
        setVia();
        setStelline();
        setCena();

        map = new HashMap<String, Float>();
        chart = (HorizontalBarChart) findViewById(R.id.chart);
        map.put("1.0", 0f);
        map.put("2.0", 0f);
        map.put("3.0", 0f);
        map.put("4.0", 0f);
        map.put("5.0", 0f);
        commenti.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) chart.setVisibility(View.VISIBLE);
                else {
                    chart.setVisibility(View.GONE);
                    stars.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        ref.child("Città").child(city).child(nome_locale).child("via").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                //qui per farla mette dentro il layout
                MapFragment mapFragment = new MapFragment();
                Bundle bundle = new Bundle();
                bundle.putString("fragmentlocale", nome_locale);
                bundle.putString("fragmentcitta", city);
                mapFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();

            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {
        if (user != null) {
            ref.child("Users").child(user.getDisplayName()).child("premium").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError firebaseError) {
                }
            });
        } else {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }
        }

        finish();
    }

    public void accediAlert() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.accedi_alert, null);
        final TextView taskEditText = (TextView) deleteDialogView.findViewById(R.id.text);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);
        deleteDialogView.findViewById(R.id.accedi_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                deleteDialog.dismiss();
                Intent i = new Intent(LocaleActivity.this, LoginActivity.class);
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

        deleteDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_locale, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            if (user != null) {
                ref.child("Users").child(user.getDisplayName()).child("premium").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            mInterstitialAd.loadAd(new AdRequest.Builder().build());
                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                    }
                });
            } else {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
            finish(); // close this activity and return to preview activity (if there is any)
        }
        if (item.getItemId() == R.id.segnala_locale) {
            if (user != null) segnala();
            else accediAlert();
        }
        if (item.getItemId() == R.id.sponsorizza_locale) {
            Intent i = new Intent(LocaleActivity.this, SponsorActivity.class);
            i.putExtra("locale", nome_locale);
            i.putExtra("city", city);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    public void iniziaCommenti() {
        Recycler = (RecyclerView) findViewById(R.id.list_commenti);
        Recycler.setLayoutManager(new LinearLayoutManager(this));

        commenti = ref.child("Città").child(city).child(nome_locale).child("commenti");

        commenti.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    TextView tex = (TextView) findViewById(R.id.commentoscomparsa);
                    tex.setVisibility(View.GONE);
                    parti();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void parti() {
        FirebaseRecyclerAdapter<Commento, CommentoViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Commento, CommentoViewHolder>(
                        Commento.class,
                        R.layout.single_row,
                        CommentoViewHolder.class,
                        commenti
                ) {
                    @Override
                    protected void populateViewHolder(CommentoViewHolder viewHolder, Commento model, int position) {
                        if (model.getCommento() == null) {
                            RecyclerView.LayoutParams lparams = (RecyclerView.LayoutParams) viewHolder.myView.getLayoutParams();
                            viewHolder.myView.setVisibility(View.GONE);
                            lparams.height = 0;
                            lparams.width = 0;
                            viewHolder.myView.setLayoutParams(lparams);
                        } else viewHolder.setCommento(model.getCommento());
                        viewHolder.setUtente(model.getUtente());
                        viewHolder.setStelle(model.getStelle());


                        float x = model.getStelle();
                        if (x != Math.ceil(x)) {
                            if (x >= 1) x = Math.round(x) - 1;
                            else x = 1;
                        }

                        float ite = map.get(String.valueOf(x));
                        map.put(String.valueOf(x), ite + 1);
                        chart.getAxisRight().setEnabled(false);
                        chart.getAxisLeft().setEnabled(false);
                        chart.getXAxis().setDrawGridLines(false);
                        chart.getXAxis().setDrawAxisLine(false);
                        chart.getXAxis().setDrawLabels(false);
                        chart.getLegend().setEnabled(false);

                        chart.setGridBackgroundColor(Color.parseColor("#FFFFFF"));
                        chart.setBorderColor(Color.parseColor("#FFFFFF"));
                        chart.setDrawingCacheBackgroundColor(Color.parseColor("#FFFFFF"));
                        chart.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        BarData data = new BarData(getXAxisValues(), getDataSet());
                        data.setValueFormatter(new Formattatore());
                        data.setValueTextSize(13);
                        chart.setData(data);
                        chart.setDescription("");
                        chart.invalidate();
                        dialogo.hide();
                    }
                };

        Recycler.setAdapter(firebaseRecyclerAdapter);
    }

    public void segnala() {

        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.segnalazione_alert, null);
        final EditText taskEditText = (EditText) deleteDialogView.findViewById(R.id.text);
        taskEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);
        deleteDialogView.findViewById(R.id.dona_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                String s = taskEditText.getText().toString();
                if (!s.equals("")) {
                    ref.child("Locali_segnalati").child(city + "_" + nome_locale).child(user.getDisplayName()).setValue(s);
                } else {
                    Toast.makeText(LocaleActivity.this,
                            "Insert a message",
                            Toast.LENGTH_SHORT).show();
                }
                deleteDialog.dismiss();
            }
        });
        deleteDialogView.findViewById(R.id.annulla_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });

        deleteDialog.show();
    }

    public Intent getFacebookIntent(String url) {

        PackageManager pm = this.getPackageManager();
        Uri uri = Uri.parse(url);

        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }

        return new Intent(Intent.ACTION_VIEW, uri);
    }

    //recensire una volta sola e devi essere loggato
    public void oneReview() {
        if (user == null) accediAlert();
        else {
            ref.child("Users").child((user.getDisplayName())).child("Recensioni").child(city).child(nome_locale).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Toast.makeText(LocaleActivity.this,
                                "You have already reviewed this club",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Intent i = new Intent(LocaleActivity.this, ReviewActivity.class);
                        i.putExtra("nome_locale", nome_locale);
                        i.putExtra("città", city);
                        startActivity(i);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    public void preferiti() {
        if (user == null) {
            accediAlert();
        } else {

            if ((int) image_b.getTag() == R.drawable.ic_bookmark1) {
                image_b.setImageResource(R.drawable.ic_bookmark2);
                image_b.setTag(R.drawable.ic_bookmark2);
                final Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.scrollview), "", Snackbar.LENGTH_LONG);
                View snackbarLayout = snackbar.getView();
                TextView textView = (TextView) snackbarLayout.findViewById(com.google.android.material.R.id.snackbar_text);
                textView.setText("Salvato per dopo                                                Annulla");
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        image_b.setImageResource(R.drawable.ic_bookmark1);
                        image_b.setTag(R.drawable.ic_bookmark1);
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.scrollview), "Rimosso", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        ref.child("Users").child(user.getDisplayName()).child("Preferiti").child(nome_locale + "_" + city).removeValue();
                        ref.child("Users").child(user.getDisplayName()).child("Preferiti").child(nome_locale + "_" + city).removeValue();
                    }
                });

                snackbar.show();
                final Map<String, Object> nome_locale_list = new HashMap<String, Object>();
                ref.child("Città").child(city).child(nome_locale).child("via").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        String viaa = snapshot.getValue(String.class);
                        final Map<String, Object> via_list = new HashMap<String, Object>();

                        via_list.put("via", viaa);
                        ref2.child("Users").child(user.getDisplayName()).child("Preferiti").child(nome_locale + "_" + city).updateChildren(via_list);
                    }

                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                    }
                });

                ref.child("Città").child(city).child(nome_locale).child("imageUrl").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String immaginerl = dataSnapshot.getValue(String.class);
                        final Map<String, Object> immagine_list = new HashMap<String, Object>();

                        immagine_list.put("imageUrl", immaginerl);
                        ref2.child("Users").child(user.getDisplayName()).child("Preferiti").child(nome_locale + "_" + city).updateChildren(immagine_list);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


                nome_locale_list.put("nome_locale", nome_locale);
                ref.child("Users").child(user.getDisplayName()).child("Preferiti").child(nome_locale + "_" + city).updateChildren(nome_locale_list);
            } else {
                image_b.setImageResource(R.drawable.ic_bookmark1);
                image_b.setTag(R.drawable.ic_bookmark1);
                Snackbar snackbar = Snackbar.make(findViewById(R.id.scrollview), "Rimosso", Snackbar.LENGTH_LONG);
                snackbar.show();
                ref.child("Users").child(user.getDisplayName()).child("Preferiti").child(nome_locale + "_" + city).removeValue();
                ref.child("Users").child(user.getDisplayName()).child("Preferiti").child(nome_locale + "_" + city).removeValue();

            }
        }
    }

    //these methods allow to jump to another app depending on the clicked button
    public void FacebookButton() {

        ref.child("Città").child(city).child(nome_locale).child("pagina_facebook").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String string = snapshot.getValue(String.class);
                if (!string.equals("")) startActivity(getFacebookIntent(string));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void PhoneButton() {

        ref.child("Città").child(city).child(nome_locale).child("numero_telefono").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String string = snapshot.getValue(String.class);
                if (!string.equals("")) {

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + string));
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void EmailButton() {

        ref.child("Città").child(city).child(nome_locale).child("indirizzo_email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String string = snapshot.getValue(String.class);
                if (!string.equals("")) {

                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.setType("plain/text");
                    sendIntent.setData(Uri.parse(string));
                    sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                    startActivity(sendIntent);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("1");
        xAxis.add("2");
        xAxis.add("3");
        xAxis.add("4");
        xAxis.add("5");
        return xAxis;
    }

    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();

        float v = map.get("1.0");
        BarEntry v1e1 = new BarEntry(v, 0); // 5
        valueSet1.add(v1e1);

        v = map.get("2.0");
        BarEntry v1e2 = new BarEntry(v, 1); // 4
        valueSet1.add(v1e2);

        v = map.get("3.0");
        BarEntry v1e3 = new BarEntry(v, 2); // 3
        valueSet1.add(v1e3);

        v = map.get("4.0");
        BarEntry v1e4 = new BarEntry(v, 3); // 2
        valueSet1.add(v1e4);

        v = map.get("5.0");
        BarEntry v1e5 = new BarEntry(v, 4); // 1
        valueSet1.add(v1e5);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Stelle");
        int[] x = {Color.parseColor("#F78B5D"),
                Color.parseColor("#FCB232"),
                Color.parseColor("#FDD930"),
                Color.parseColor("#ADD137"),
                Color.parseColor("#A0C25A")};
        barDataSet1.setColors(x);
        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        return dataSets;
    }

    @Override
    public void onClick(View v) {
        if (v == button_recensionii) {
            oneReview();
        }
        if (v == image_b) {
            preferiti();
        }
        if (v == facebook_icon) {
            FacebookButton();
        }
        if (v == phone_icon) {
            PhoneButton();
        }
        if (v == email_icon) {
            EmailButton();
        }
    }

    //these sets allow the edittexts to be populated with values from the firebase db
    public void setStelline() {
        ref.child("Città").child(city).child(nome_locale).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                float rate = snapshot.child("rate").getValue(Float.class);
                float iter = snapshot.child("iterazioni").getValue(Float.class);
                if (rate / iter < 0) rating_bar.setRating(0);
                else rating_bar.setRating(rate / iter);
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
    }

    public void setBand() {
        ref.child("Città").child(city).child(nome_locale).child("band").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String originale = snapshot.child("originale").getValue(String.class);
                String tribute = snapshot.child("tribute").getValue(String.class);
                if (originale.equals("si") && tribute.equals("")) {
                    band_risp.setText("Originali");
                } else if (originale.equals("") && tribute.equals("si")) {
                    band_risp.setText("Tribute");
                } else if (originale.equals("si") && tribute.equals("si")) {
                    band_risp.setText("Tutte");
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
    }

    public void setVia() {
        ref.child("Città").child(city).child(nome_locale).child("via").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String via = snapshot.getValue(String.class);
                if (via.contains("via") || via.contains("Via") || via.contains("Piazza") || via.contains("piazza") || via.contains("Largo") || via.contains("largo")) {
                    via_text.setText(via);
                } else via_text.setText("Via " + via);
                indirizzoc = via + " " + city;

            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
    }

    public void setCena() {
        ref.child("Città").child(city).child(nome_locale).child("cena").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int si = dataSnapshot.child("si").getValue(Integer.class);
                int no = dataSnapshot.child("no").getValue(Integer.class);

                if (si > no) cena_risp.setText("Compresa");
                else cena_risp.setText("Non Compresa");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setBackline() {
        ref.child("Città").child(city).child(nome_locale).child("backline").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int ampl_basso = dataSnapshot.child("ampl_basso").getValue(Integer.class);
                int ampl_chitarra = dataSnapshot.child("ampl_chitarra").getValue(Integer.class);
                int mixer = dataSnapshot.child("mixer").getValue(Integer.class);
                int microfoni = dataSnapshot.child("microfoni").getValue(Integer.class);
                int batteria = dataSnapshot.child("batteria").getValue(Integer.class);

                if (ampl_basso == 0 && ampl_chitarra == 0 && mixer == 0 && microfoni == 0 && batteria == 0) {
                    backline_risp.setVisibility(View.VISIBLE);
                    backline_risp.setText("Assente");
                } else {
                    backline_risp.setVisibility(View.GONE);
                    if (ampl_basso != 0) {
                        ampl_basso_risp.setVisibility(View.VISIBLE);
                        ampl_basso_risp.setText("• Amplificatore per basso");
                    }
                    if (mixer != 0) {
                        mixer_risp.setVisibility(View.VISIBLE);
                        mixer_risp.setText("• Mixer");
                    }
                    if (batteria != 0) {
                        batteria_risp.setVisibility(View.VISIBLE);
                        batteria_risp.setText("• Batteria");
                    }
                    if (ampl_chitarra != 0) {
                        if (ampl_chitarra == 1) {
                            ampl_chitarra_risp.setVisibility(View.VISIBLE);
                            ampl_chitarra_risp.setText("• 1 Amplificatore per chitarre");
                        } else {
                            ampl_chitarra_risp.setVisibility(View.VISIBLE);
                            ampl_chitarra_risp.setText("• " + ampl_chitarra + " Amplificatori per chitarre");
                        }
                    }
                    if (microfoni != 0) {
                        if (microfoni == 1) {
                            mic_risp.setVisibility(View.VISIBLE);
                            mic_risp.setText("• 1 Microfono");
                        } else {
                            mic_risp.setVisibility(View.VISIBLE);
                            mic_risp.setText("• " + microfoni + " Microfoni");
                        }
                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void setPubblico() {
        ref.child("Città").child(city).child(nome_locale).child("pubblico").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int tanto = dataSnapshot.child("tanto").getValue(Integer.class);
                int no = dataSnapshot.child("no").getValue(Integer.class);
                int poco = dataSnapshot.child("poco").getValue(Integer.class);
                int medio = dataSnapshot.child("medio").getValue(Integer.class);

                HashMap<String, Integer> map = new HashMap<String, Integer>();
                map.put("Tanto", tanto);
                map.put("Medio", medio);
                map.put("Poco", poco);
                map.put("Assente", no);
                int maxValueInMap = (Collections.max(map.values()));  // This will return max value in the Hashmap
                for (Map.Entry<String, Integer> entry : map.entrySet()) {  // Itrate through hashmap
                    if (entry.getValue() == maxValueInMap) {
                        if (maxValueInMap == 0) pubblico_risp.setText("");
                        else pubblico_risp.setText(entry.getKey());


                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void setContatto() {
        ref.child("Città").child(city).child(nome_locale).child("contatto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int email = dataSnapshot.child("email").getValue(Integer.class);
                int facebook = dataSnapshot.child("facebook").getValue(Integer.class);
                int telefono = dataSnapshot.child("telefono").getValue(Integer.class);
                if (email == 0 && facebook == 0 && telefono == 0) contatto_risp.setText("");
                else {
                    if (email >= facebook && email >= telefono) contatto_risp.setText("Email");
                    else if (facebook >= email && facebook >= telefono)
                        contatto_risp.setText("Facebook");
                    else if (telefono >= email && telefono >= facebook)
                        contatto_risp.setText("Telefono");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void setPagamento() {
        ref.child("Città").child(city).child(nome_locale).child("pagamento").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String fisso = dataSnapshot.child("fisso").getValue(String.class);
                String ticket = dataSnapshot.child("ticket").getValue(String.class);
                int max = dataSnapshot.child("importo_max").getValue(Integer.class);
                int min = dataSnapshot.child("importo_min").getValue(Integer.class);

                if (fisso.equals("") && ticket.equals("si")) pagamento_risp.setText("A biglietto");
                else if (fisso.equals("si") && ticket.equals("")) {
                    if (max == -1 && min == -1) pagamento_risp.setText("Fisso");
                    else if (max != -1 && min == -1) {
                        pagamento_risp.setText("Fisso");
                        max_text.setVisibility(View.VISIBLE);
                        max_text.setText("• Massimo: " + max + " €");
                    } else if (max == -1 && min != -1) {
                        pagamento_risp.setText("Fisso");
                        min_text.setVisibility(View.VISIBLE);
                        min_text.setText("• Minimo: " + min + " €");
                    } else if (max != -1 && min != -1) {
                        pagamento_risp.setText("Fisso");
                        max_text.setVisibility(View.VISIBLE);
                        max_text.setText("• Massimo: " + max + " €");
                        min_text.setVisibility(View.VISIBLE);
                        min_text.setText("• Minimo: " + min + " €");
                    }
                } else if (fisso.equals("si") && ticket.equals("si")) {
                    if (max == -1 && min == -1) {
                        pagamento_risp.setText("Fisso/A biglietto");
                    } else if (max != -1 && min == -1) {
                        pagamento_risp.setText("Fisso/A biglietto");
                        max_text.setVisibility(View.VISIBLE);
                        max_text.setText("• Massimo: " + max + " €");
                    } else if (max == -1 && min != -1) {
                        pagamento_risp.setText("Fisso/A biglietto");
                        min_text.setVisibility(View.VISIBLE);
                        min_text.setText("• Minimo: " + min + " €");
                    } else if (max != -1 && min != -1) {
                        pagamento_risp.setText("Fisso/a biglietto");
                        max_text.setVisibility(View.VISIBLE);
                        max_text.setText("• Massimo: " + max + " €");
                        min_text.setVisibility(View.VISIBLE);
                        min_text.setText("• Minimo: " + min + " €");

                    }
                }
            }


            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void setGenere() {
        ref.child("Città").child(city).child(nome_locale).child("genere").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int vario = snapshot.child("vario").getValue(Integer.class);
                int hard = snapshot.child("hard").getValue(Integer.class);
                int metal = snapshot.child("metal").getValue(Integer.class);
                int alternative = snapshot.child("alternative").getValue(Integer.class);
                int jazz = snapshot.child("jazz").getValue(Integer.class);
                int indie = snapshot.child("indie").getValue(Integer.class);

                HashMap<String, Integer> map = new HashMap<String, Integer>();
                map.put("Vario", vario);
                map.put("Hard Rock", hard);
                map.put("Metal", metal);
                map.put("Alternative", alternative);
                map.put("Jazz/Blues", jazz);
                map.put("Indie", indie);
                int maxValueInMap = (Collections.max(map.values()));  // This will return max value in the Hashmap
                for (Map.Entry<String, Integer> entry : map.entrySet()) {  // Itrate through hashmap
                    if (entry.getValue() == maxValueInMap) {
                        if (maxValueInMap == 0) genere_risp.setText("");
                        else genere_risp.setText(entry.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
    }

    public static class CommentoViewHolder extends RecyclerView.ViewHolder {
        private TextView testo;
        View myView;
        //inserire roba da far vedere

        public CommentoViewHolder(View itemView) {
            super(itemView);
            myView = itemView;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }

        public void setCommento(String commento) {
            TextView post_nomelocale = (TextView) myView.findViewById(R.id.Commento);
            post_nomelocale.setText(commento);
        }

        public void setUtente(String utente) {
            TextView post_nomelocale = (TextView) myView.findViewById(R.id.NomeUtente);
            post_nomelocale.setText(utente);
        }

        public void setStelle(Float stelle) {
            RatingBar ratingBar = (RatingBar) myView.findViewById(R.id.ratingBar);
            ratingBar.setRating(stelle);
        }
    }

    public class Formattatore implements ValueFormatter {

        private DecimalFormat mFormat;

        public Formattatore() {
            mFormat = new DecimalFormat("###,###,###,##0.0");
        }

        @Override
        public String getFormattedValue(float value) {
            return "" + ((int) value);
        }
    }

    public void ads() {
        if (user!=null){
        ref.child("Users").child(user.getDisplayName()).child("premium").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    LayoutInflater factory = LayoutInflater.from(LocaleActivity.this);
                    final View deleteDialogView = factory.inflate(R.layout.alert_pubb, null);
                    final AlertDialog deleteDialog = new AlertDialog.Builder(LocaleActivity.this).create();
                    deleteDialog.setView(deleteDialogView);
                    deleteDialogView.findViewById(R.id.closeb).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteDialog.dismiss();
                        }
                    });

                    deleteDialog.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    else{
            LayoutInflater factory = LayoutInflater.from(LocaleActivity.this);
            final View deleteDialogView = factory.inflate(R.layout.alert_pubb, null);
            final AlertDialog deleteDialog = new AlertDialog.Builder(LocaleActivity.this).create();
            deleteDialog.setView(deleteDialogView);
            deleteDialogView.findViewById(R.id.closeb).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteDialog.dismiss();
                }
            });

            deleteDialog.show();

    }}
}
