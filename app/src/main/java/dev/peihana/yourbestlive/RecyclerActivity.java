package dev.peihana.yourbestlive;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.internal.view.SupportMenuItem;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.LoginManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rubengees.introduction.IntroductionBuilder;
import com.rubengees.introduction.Slide;
import com.squareup.picasso.Picasso;
import com.stripe.android.model.Card;
import com.stripe.android.view.CardInputWidget;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class RecyclerActivity extends AppCompatActivity {

    public RecyclerView Recy;
    public RecyclerView RecyCity;
    int clickcount=0;
    private DatabaseReference dbref;
    boolean justShow;
    SearchView searchView;
    public DatabaseReference ref;
    public DatabaseReference roma;
    public DatabaseReference Nomi;
    public String stringaa;
    public String stringone;
    ProgressDialog dialog;
    private DrawerLayout mDrawareLayout;
    private ActionBarDrawerToggle mToggle;
    public String citta;
    public Intent i;
    TextView nome_header;
    FirebaseUser user;
    View nav;
    TextView accedi;
    TextView log_out;
    NavigationView nav_view;
    TabLayout tab_layout;
    SearchView search_city;
    String money;
    //String pubb="";
    String stringa;
    Intent go;
    String cvc;
    int exp_month;
    int exp_year;
    String card_number;
    final String send_payment_details = "http://peihana.pythonanywhere.com/charge";
    CardInputWidget mCardInputWidget;
    ProgressDialog dialog2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controllaCitta();//vede la città predefinita dell'utente
        justShow = false;
        dbref = ref.child("Secret");

        if (user == null) noLogin();
        else conLogin();

        //funzioni drawer layout
        drawerL();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            citta = extras.getString("cittadina");

            if (citta.contains("-")) {
                String[] parts = citta.split("-");
                citta = parts[0];
                String nl = parts[1];
                ref.child("Città").child(citta).child(nl).removeValue();
            }
        }

        //dialogo caricamento locali
        dialogo();

        iniziaSchermo();

        //funzioni tab
        funzioniTab();

        tutorial();

        AppUpdater appUpdater = new AppUpdater(this)
                .setDisplay(Display.SNACKBAR)
                .setTitleOnUpdateAvailable("Aggiornamento disponibile!")
                .setContentOnUpdateAvailable("Nuovo aggiornamento disponibile!")
                .setButtonUpdate("Aggiorna");
        appUpdater.start();

        FloatingActionButton flb= (FloatingActionButton) findViewById(R.id.floatingbutton);
        flb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user!=null) {
                    Intent i=new Intent(RecyclerActivity.this,LocalRegister.class);
                    startActivity(i); }
                else  accediAlert();
            }
        });
    }

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            }
            else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') {
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    public void stripe() {
        final Card card = mCardInputWidget.getCard();
        if (card == null) {
            Toast.makeText(RecyclerActivity.this, "Not valid card", Toast.LENGTH_LONG).show();
        }
        else {
            if(card.validateCard()) {
                cvc= card.getCVC();
                card_number=card.getNumber();
                exp_month= card.getExpMonth();
                exp_year= card.getExpYear();
                charge();
            }
        }
    }

    public void charge() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, send_payment_details,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("rimozione_pubb")) {
                            ref.child("Users").child(user.getDisplayName()).child("premium").setValue(true);
                            stringa="";
                            grizie("We have successfully removed the advertisement!");
                            dialog2.hide();
                        }
                        else if(response.contains("donazione")) {
                            if(user!=null) {
                                ref.child("Donatori").child(user.getDisplayName()).setValue(money); }
                            else ref.child("Donatori").child(money).setValue("anonimo");
                            grizie("Thanks for the donation!");
                            dialog2.hide();
                            stringa="";
                        }
                        else Toast.makeText(RecyclerActivity.this, "Transaction failed, please try again", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog2.hide();
                        Toast.makeText(RecyclerActivity.this, "Try again", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<String, String>();
                if(stringa.equals("donazione")) {
                    params.put("amount", String.valueOf(Integer.parseInt(money)*100));
                }
                else if(stringa.equals("rimozione_pubb")) {
                    params.put("amount", String.valueOf(150));
                }
                params.put("cvc", cvc);
                params.put("card_number", card_number);
                params.put("exp_month", String.valueOf(exp_month));
                params.put("exp_year", String.valueOf(exp_year));
                params.put("stringa", stringa);
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

    public void alertAd() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.alert_no_ads, null);
        mCardInputWidget = (CardInputWidget) deleteDialogView.findViewById(R.id.card_input_widget);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);
        deleteDialogView.findViewById(R.id.rimuovi_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringa="rimozione_pubb";
                stripe();
                deleteDialog.dismiss();
                dialog2.setMessage("Payment in progress..");
                dialog2.setCanceledOnTouchOutside(false);
                dialog2.show();
            }
        });
        deleteDialogView.findViewById(R.id.annulla2_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });
        deleteDialog.setCanceledOnTouchOutside(false);
        deleteDialog.show();
    }

    public void accediAlert() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.accedi_alert, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);
        deleteDialogView.findViewById(R.id.accedi_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
                Intent i=new Intent(RecyclerActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
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

    public void segreto() {
        FirebaseRecyclerAdapter<Post, PostViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Post, PostViewHolder>(
                        Post.class,
                        R.layout.single_item,
                        PostViewHolder.class,
                        dbref
                ) {
                    @Override
                    protected void populateViewHolder(final PostViewHolder viewHolder, Post model, final int position) {

                        viewHolder.setNomeLocale(model.getNome_locale());
                        viewHolder.setImmagine(model.getImageUrl(),getApplicationContext());
                        viewHolder.setStelline(model.getRate(), model.getIterazioni());
                        justShow=true;
                        dialog.hide();
                        viewHolder.myView.setOnTouchListener(new OnSwipeTouchListener(RecyclerActivity.this) {
                            @Override
                            public void onClick() {
                                super.onClick();
                                stringaa = getRef(position).getKey();
                                Intent i = new Intent(RecyclerActivity.this, SecretActivity.class);
                                i.putExtra("nome_locale", stringaa);
                                startActivity(i);
                            }

                            @Override
                            public void onSwipeLeft() {
                                super.onSwipeLeft();
                                if(user!=null) {
                                    go = new Intent(RecyclerActivity.this,PreferitiActivity.class);
                                    startActivity(go);
                                    overridePendingTransition(0, 0); }
                                else {
                                    accediAlert(); }
                            }

                            @Override
                            public void onSwipeRight() {
                                super.onSwipeRight();
                            }
                        });
                    }
                };
        Recy.setAdapter(firebaseRecyclerAdapter);
    }

    public void controllaCitta() {
        citta = "Roma";
        stringa = "";
        ref = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences mPrefs = getSharedPreferences("cittapredef", Context.MODE_PRIVATE);
        citta = mPrefs.getString("cittapredefinita", "");
        if (citta.isEmpty()) {
            citta = "Roma";
        }
    }

    public void noLogin() {
        setContentView(R.layout.activity_recycler_view_no_log);
        //navigation menu on the left
        NavigationView navigator = (NavigationView) findViewById(R.id.menu_sotto2);
        //navigator.getMenu().findItem(R.id.supportaci).setTitle("Dona");
        //navigator.getMenu().findItem(R.id.supportaci).setIcon(R.drawable.ic_heart);
        navigator.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.impostazioni:
                        Intent i = (new Intent(RecyclerActivity.this, ImpostazioniActivity.class));
                        startActivity(i);
                        break;
                    //case R.id.supportaci:
                       // alertDona();
                       // break;
                    case R.id.sponsorizza_locale:
                        i = new Intent(RecyclerActivity.this, SponsorActivityDrop.class);
                        startActivity(i);
                        break;
                }
                return true;
            }
        });


        nav_view = (NavigationView) findViewById(R.id.nav_menu2);
        nav = nav_view.inflateHeaderView(R.layout.navigation_header_no_log);

        //this allows to hide every city before the one searched in the navigation menu list
        search_city = (SearchView) findViewById(R.id.search_city);
        search_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_city.onActionViewExpanded();
                RecyCity = (RecyclerView) findViewById(R.id.list_citta);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) RecyCity.getLayoutParams();
                params.weight = 0.0f;
                params.height = RecyCity.getHeight();
                RecyCity.setLayoutParams(params);
            }
        });

        //every text change changes the results list so the adapter has to be modified and re-binded to the RecyclerView
        search_city.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                Query Q;
                Q = ref.child("NomiCittà").orderByChild("city").startAt(capitalizeString(newText)).endAt("~");
                FirebaseRecyclerAdapter<City, CittàViewHolder> firebaseRecyclerAdapter29 = new FirebaseRecyclerAdapter<City, CittàViewHolder>(
                        City.class, R.layout.single_citta, CittàViewHolder.class, Q) {
                    @Override
                    protected void populateViewHolder(CittàViewHolder viewHolder, City model, final int position) {
                        viewHolder.setNomeCittà(model.getCity());
                        viewHolder.myView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                stringone = getRef(position).getKey();
                                Intent i = new Intent(RecyclerActivity.this, RecyclerActivity.class);
                                i.putExtra("cittadina", stringone);
                                startActivity(i);
                                finish();
                            }
                        });
                        if (getRef(position).getKey().equals(citta)) {
                            viewHolder.myView.findViewById(R.id.cittasola).setBackgroundColor(Color.parseColor("#50f5f5f5"));
                            TextView testo = (TextView) viewHolder.myView.findViewById(R.id.nomecitta);
                            testo.setTextColor(Color.parseColor("#881c21"));
                            ImageView img = (ImageView) viewHolder.myView.findViewById(R.id.planetcolor);
                            img.setColorFilter(Color.parseColor("#881c21"));
                        }
                    }
                };
                RecyCity.setAdapter(firebaseRecyclerAdapter29);
                firebaseRecyclerAdapter29.notifyDataSetChanged();
                return false;
            }
        });
        accedi = (TextView) nav.findViewById(R.id.accedi);
        accedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecyclerActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void conLogin() {
        setContentView(R.layout.activity_recycler);
        final NavigationView navigator = (NavigationView) findViewById(R.id.menu_sotto);
        ref.child("Users").child(user.getDisplayName()).child("premium").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //if (snapshot.exists()) {
                //    navigator.getMenu().findItem(R.id.supportaci).setTitle("Dona");
                //    navigator.getMenu().findItem(R.id.supportaci).setIcon(R.drawable.ic_heart);
                //}
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {}
        });
        navigator.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.impostazioni:
                        Intent i = (new Intent(RecyclerActivity.this, ImpostazioniActivity.class));
                        startActivity(i);
                        break;
                    case R.id.supportaci:
                        ref.child("Users").child(user.getDisplayName()).child("premium").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    alertDona();
                                } else alertAd();
                            }
                            @Override
                            public void onCancelled(DatabaseError firebaseError) {}
                        });
                        break;
                    case R.id.sponsorizza_locale:
                        i = new Intent(RecyclerActivity.this, SponsorActivityDrop.class);
                        startActivity(i);
                        break;
                }
                return true;
            }

        });
        nav_view = (NavigationView) findViewById(R.id.nav_menu);
        nav = nav_view.inflateHeaderView(R.layout.navigation_header);
        alertFirebase();

        final ImageView corona = (ImageView) nav.findViewById(R.id.corona);
        ref.child("Donatori").child(user.getDisplayName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    corona.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        nome_header = (TextView) nav.findViewById(R.id.nomeutente);
        search_city = (SearchView) findViewById(R.id.search_city);
        search_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_city.onActionViewExpanded();
                RecyCity = (RecyclerView) findViewById(R.id.list_citta);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) RecyCity.getLayoutParams();
                params.weight = 0.0f;
                params.height = RecyCity.getHeight();
                RecyCity.setLayoutParams(params);
            }
        });
        search_city.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                Query Q;
                Q = ref.child("NomiCittà").orderByChild("city").startAt(capitalizeString(newText)).endAt("~");
                FirebaseRecyclerAdapter<City, CittàViewHolder> firebaseRecyclerAdapter28 = new FirebaseRecyclerAdapter<City, CittàViewHolder>(
                        City.class, R.layout.single_citta, CittàViewHolder.class, Q) {
                    @Override
                    protected void populateViewHolder(CittàViewHolder viewHolder, City model, final int position) {
                        viewHolder.setNomeCittà(model.getCity());
                        viewHolder.myView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                stringone = getRef(position).getKey();
                                Intent i = new Intent(RecyclerActivity.this, RecyclerActivity.class);
                                i.putExtra("cittadina", stringone);
                                startActivity(i);
                                finish();
                            }
                        });
                        if (getRef(position).getKey().equals(citta)) {
                            viewHolder.myView.findViewById(R.id.cittasola).setBackgroundColor(Color.parseColor("#50f5f5f5"));
                            TextView testo = (TextView) viewHolder.myView.findViewById(R.id.nomecitta);
                            testo.setTextColor(Color.parseColor("#881c21"));
                            ImageView img = (ImageView) viewHolder.myView.findViewById(R.id.planetcolor);
                            img.setColorFilter(Color.parseColor("#881c21"));
                        }
                    }
                };
                RecyCity.setAdapter(firebaseRecyclerAdapter28);
                firebaseRecyclerAdapter28.notifyDataSetChanged();
                return false;
            }
        });

        log_out = (TextView) nav.findViewById(R.id.logout);
        nome_header.setText(user.getDisplayName());
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                Intent i = new Intent(RecyclerActivity.this, RecyclerActivity.class);
                startActivity(i);
                Toast.makeText(RecyclerActivity.this,
                        "Disconnected", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void drawerL() {//prepares action bar above with title and buttons (search and navigation view)
        mDrawareLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawareLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
                searchView.clearFocus();
                searchView.setIconified(true);
            }
        };
        mDrawareLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#881c21")));
        getSupportActionBar().setElevation(Float.parseFloat("0.0"));
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>yourBestLive</font>"));

        dialog2=new ProgressDialog(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mToggle.getDrawerArrowDrawable().setColor(getColor(R.color.white));
        }
        else {
            mToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        }
    }

    public void dialogo() {//a simple progress dialog when loading locals
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading clubs..");
        dialog.setCanceledOnTouchOutside(false);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if(!isFinishing()){
                    if(!justShow)
                        dialog.show();
                }
            }
        }, 1000);
    }

    public void funzioniTab() {
        //handles the swipe logic between locals list and favourites list
        tab_layout=(TabLayout) findViewById(R.id.tabs);
        tab_layout.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#ffffff")));
        tab_layout.setSelectedTabIndicatorColor(Color.parseColor("#ffffff"));
        final TabLayout.Tab tab1=tab_layout.getTabAt(0);
        tab1.select();
        Recy.setOnTouchListener(new OnSwipeTouchListener(RecyclerActivity.this) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                if(user!=null) {
                    go = new Intent(RecyclerActivity.this,PreferitiActivity.class);
                    startActivity(go);
                    overridePendingTransition(0, 0);}
                else {
                    accediAlert(); }
            }
        });
        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()) {
                    case 1:
                        if(user!=null) {
                            go = new Intent(RecyclerActivity.this,PreferitiActivity.class);
                            startActivity(go);
                            overridePendingTransition(0, 0);
                            tab1.select();
                            break; }
                        else   {
                            tab_layout.getTabAt(0).select();
                            accediAlert(); }
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Timer timer = new Timer();
                timer.schedule(new TimerTask(){
                    @Override
                    public void run() {
                        clickcount=0;
                    }
                }, 2000);
                clickcount=clickcount+1;
                if(clickcount==3) {
                    segreto();
                }
            }
        });
    }

    public void tutorial() {
        SharedPreferences pref = getSharedPreferences("tutorial",Context.MODE_PRIVATE);
        String tutorial = pref.getString("tutorial", "");
        if (tutorial.isEmpty()) {
            tutorial = "tutorial";
            new IntroductionBuilder(this)
                    .withSlides(
                            new Slide()
                                    .withCustomViewBuilder(new Ciaone())
                                    .withTitle("Find your city")
                                    .withDescription("\n" + "Scroll through the cities in the sidebar\nand look for the right club for you!")
                                    .withImage(R.drawable.tutorial2)
                                    .withColorResource(R.color.trifoglio),
                            new Slide()
                                    .withCustomViewBuilder(new Ciaone())
                                    .withTitle("Register new clubs")
                                    .withDescription("Click on this icon to add a new club")
                                    .withImage(R.drawable.tutorial1)
                                    .withColorResource(R.color.celestino),
                            new Slide()
                                    .withCustomViewBuilder(new Ciaone())
                                    .withTitle("Save your clubs")
                                    .withDescription("Quickly access the clubs that interest you")
                                    .withImage(R.drawable.tutorial3)
                                    .withColorResource(R.color.giallo)
                    ).introduceMyself();
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("tutorial", tutorial);
            editor.apply();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class OnSwipeTouchListener implements View.OnTouchListener {
        private GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context c) {
            gestureDetector = new GestureDetector(c, new GestureListener());
        }

        public boolean onTouch(final View view, final MotionEvent motionEvent) {
            return gestureDetector.onTouchEvent(motionEvent);
        }

        final class GestureListener extends GestureDetector.SimpleOnGestureListener {
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                onClick();
                return super.onSingleTapUp(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                onDoubleClick();
                return super.onDoubleTap(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                onLongClick();
                super.onLongPress(e);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            }
                            else {
                                onSwipeLeft();
                            }
                        }
                    }
                    else {
                        if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffY > 0) {
                                onSwipeDown();
                            }
                            else {
                                onSwipeUp();
                            }
                        }
                    }
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }

        public void onSwipeRight() {
        }

        public void onSwipeLeft() {
        }

        public void onSwipeUp() {
        }

        public void onSwipeDown() {
        }

        public void onClick() {
        }

        public void onDoubleClick() {
        }

        public void onLongClick() {

        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SupportMenuItem searchMenuItem = ((SupportMenuItem) menu.findItem(R.id.action_search));
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        EditText txtSearch = ((EditText)searchView.findViewById(com.google.android.material.R.id.search_src_text));
        txtSearch.setHint("Cerca");
        txtSearch.setHintTextColor(Color.LTGRAY);
        txtSearch.setTextColor(Color.WHITE);
        ImageView searchButton = (ImageView) searchView.findViewById(com.google.android.material.R.id.search_close_btn);
        ImageView searchButton1 = (ImageView) searchView.findViewById(com.google.android.material.R.id.search_button);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            searchButton.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            searchButton1.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        }
        View searchBar = searchView.findViewById(R.id.search_bar);
        if (searchBar != null && searchBar instanceof LinearLayout) {
            ((LinearLayout) searchBar).setLayoutTransition(new LayoutTransition());
        };

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawareLayout.closeDrawers();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final String c= newText;
                mDrawareLayout.closeDrawers();
                Query query=roma.orderByChild("sponsorizzato");
                FirebaseRecyclerAdapter<Post, PostViewHolder> firebaseRecyclerAdapter22 =
                        new FirebaseRecyclerAdapter<Post, PostViewHolder>(
                                Post.class,
                                R.layout.single_item,
                                PostViewHolder.class,
                                query
                        ) {
                            @Override
                            protected void populateViewHolder(final PostViewHolder viewHolder, Post model, final int position) {
                                if (user != null) {
                                    ref.child("Users").child(user.getDisplayName()).child("Preferiti").child(model.getNome_locale() + "_" + citta).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                viewHolder.setIcon("visible");
                                            } else viewHolder.setIcon("gone");
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                                } else viewHolder.setIcon("gone");
                                ref.child("Città").child(citta).child(model.getNome_locale()).child("sponsorizzato").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        int a = dataSnapshot.getValue(Integer.class);
                                        if (a == 1) viewHolder.setPreferiti("");
                                        else viewHolder.setPreferiti("recommended");
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });

                                if (!model.getNome_locale().toLowerCase().contains(c.toLowerCase())) {
                                    RecyclerView.LayoutParams lparams=(RecyclerView.LayoutParams) viewHolder.myView.getLayoutParams();
                                    viewHolder.myView.setVisibility(View.GONE);
                                    lparams.height=0;
                                    lparams.width=0;
                                    viewHolder.myView.setLayoutParams(lparams);
                                }
                                else {
                                    viewHolder.setNomeLocale(model.getNome_locale());
                                    viewHolder.setImmagine(model.getImageUrl(), getApplicationContext());
                                    viewHolder.setStelline(model.getRate(), model.getIterazioni());
                                    justShow = true;
                                    dialog.hide();
                                    viewHolder.myView.setOnTouchListener(new OnSwipeTouchListener(RecyclerActivity.this) {

                                        @Override
                                        public void onClick() {
                                            super.onClick();
                                            stringaa = getRef(position).getKey();
                                            Intent i = new Intent(RecyclerActivity.this, LocaleActivity.class);
                                            i.putExtra("nome_locale", stringaa);
                                            i.putExtra("citta", citta);
                                            startActivity(i);
                                        }

                                        @Override
                                        public void onSwipeLeft() {
                                            super.onSwipeLeft();
                                            if (user != null) {
                                                go = new Intent(RecyclerActivity.this, PreferitiActivity.class);
                                                startActivity(go);
                                                overridePendingTransition(0, 0);
                                            } else {
                                                accediAlert();
                                            }
                                        }
                                    });}
                            }
                        };
                Recy.setAdapter(firebaseRecyclerAdapter22);
                firebaseRecyclerAdapter22.notifyDataSetChanged();
                return false;
            }
        });

        return true;
    }

    public void iniziaSchermo() {//populates the two recyclers, both in navigation menu on the left and in main menu
        Recy = (RecyclerView) findViewById(R.id.post_list);
        Recy.setLayoutManager(new LinearLayoutManager(this));
        roma = ref.child("Città").child(citta);
        RecyCity = (RecyclerView) findViewById(R.id.list_citta);
        RecyCity.setLayoutManager(new LinearLayoutManager(this));
        Nomi = ref.child("NomiCittà");

        roma.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()>0) {
                    TextView tex= (TextView)findViewById(R.id.commentoscomparsa1);
                    tex.setVisibility(View.GONE);
                    parti();
                    partimenu();
                }
                else {
                    TextView tex= (TextView)findViewById(R.id.commentoscomparsa1);
                    tex.setVisibility(View.VISIBLE);
                    justShow=true;
                    dialog.hide();
                    partimenu();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void alertFirebase() {

        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.ringraziamenti_alert, null);
        final TextView taskEditText=(TextView) deleteDialogView.findViewById(R.id.text);
        final TextView taskEditText2=(TextView) deleteDialogView.findViewById(R.id.title_donazione);
        final Button button3 = (Button) deleteDialogView.findViewById(R.id.button3);

        ref.child("Comunicazioni").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String titolo= dataSnapshot.child("titolo_alert").getValue(String.class);
                String testo= dataSnapshot.child("testo_alert").getValue(String.class);
                String bottone= dataSnapshot.child("button_alert").getValue(String.class);
                boolean a=dataSnapshot.child("mostra_alert").getValue(Boolean.class);

                if(a) {
                taskEditText2.setText(titolo);
                taskEditText.setText(testo);
                button3.setText(bottone);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
        ref.child("Comunicazioni").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean a=dataSnapshot.child("mostra_alert").getValue(Boolean.class);
                if(a) deleteDialog.show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
                if(!taskEditText.getText().toString().equals("")) {
                    money=taskEditText.getText().toString();
                    stringa="donazione";
                    stripe();
                    deleteDialog.dismiss();
                    dialog2.setMessage("Payment in progress..");
                    dialog2.setCanceledOnTouchOutside(false);
                    dialog2.show();}
                else  Toast.makeText(RecyclerActivity.this,
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

    public void parti() {
        Query query=roma.orderByChild("sponsorizzato");
        FirebaseRecyclerAdapter<Post, PostViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Post, PostViewHolder>(
                        Post.class,
                        R.layout.single_item,
                        PostViewHolder.class,
                        query
                ) {
                    @Override
                    protected void populateViewHolder(final PostViewHolder viewHolder, Post model, final int position) {
                        if(user!=null) {
                            ref.child("Users").child(user.getDisplayName()).child("Preferiti").child(model.getNome_locale()+"_"+citta).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()) {
                                        viewHolder.setIcon("visible");
                                    }
                                    else  viewHolder.setIcon("gone");
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }
                        else viewHolder.setIcon("gone");
                        ref.child("Città").child(citta).child(model.getNome_locale()).child("sponsorizzato").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int a=dataSnapshot.getValue(Integer.class);
                                if(a==1) viewHolder.setPreferiti("");
                                else viewHolder.setPreferiti("recommended");
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                        viewHolder.setNomeLocale(model.getNome_locale());
                        viewHolder.setImmagine(model.getImageUrl(),getApplicationContext());
                        viewHolder.setStelline(model.getRate(), model.getIterazioni());
                        justShow=true;
                        dialog.hide();
                        viewHolder.myView.setOnTouchListener(new OnSwipeTouchListener(RecyclerActivity.this)
                        {
                            @Override
                            public void onClick() {
                                super.onClick();
                                stringaa = getRef(position).getKey();
                                Intent i = new Intent(RecyclerActivity.this, LocaleActivity.class);
                                i.putExtra("nome_locale", stringaa);
                                i.putExtra("citta", citta);
                                startActivity(i);
                            }

                            @Override
                            public void onSwipeLeft() {
                                super.onSwipeLeft();
                                if(user!=null) {
                                    go = new Intent(RecyclerActivity.this,PreferitiActivity.class);
                                    startActivity(go);
                                    overridePendingTransition(0, 0); }
                                else {
                                    accediAlert(); }
                            }
                        });
                    }
                };
        Recy.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }

    public void partimenu() {
        FirebaseRecyclerAdapter<City, CittàViewHolder> RecyclerAdapter =
                new FirebaseRecyclerAdapter<City, CittàViewHolder>(
                        City.class,
                        R.layout.single_citta,
                        CittàViewHolder.class,
                        Nomi
                ) {
                    @Override
                    protected void populateViewHolder(CittàViewHolder viewHolder, City model, final int position) {
                        viewHolder.setNomeCittà(model.getCity());
                        viewHolder.myView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick (View view){
                                stringone = getRef(position).getKey();
                                Intent i = new Intent(RecyclerActivity.this, RecyclerActivity.class);
                                i.putExtra("cittadina", stringone);
                                mDrawareLayout.closeDrawers();
                                startActivity(i);
                                overridePendingTransition(0, 0);
                                finish();
                            }
                        });
                        if (getRef(position).getKey().equals(citta)){
                            viewHolder.myView.findViewById(R.id.cittasola).setBackgroundColor(Color.parseColor("#50f5f5f5"));
                            TextView testo= (TextView) viewHolder.myView.findViewById(R.id.nomecitta);
                            testo.setTextColor(Color.parseColor("#881c21"));
                            ImageView img= (ImageView) viewHolder.myView.findViewById(R.id.planetcolor);
                            img.setColorFilter(Color.parseColor("#881c21"));
                        }
                    }
                };
        RecyCity.setAdapter(RecyclerAdapter);
        RecyclerAdapter.notifyDataSetChanged();
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
                deleteDialog.dismiss();
            }
        });
        deleteDialog.setCanceledOnTouchOutside(false);
        deleteDialog.show();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {

            AlertDialog alertbox = new AlertDialog.Builder(this)
                    .setMessage("Do you want to exit??")
                    .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        // do something when the button is clicked
                        public void onClick(DialogInterface arg0, int arg1) {
                            }
                    })
                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        // do something when the button is clicked
                        public void onClick(DialogInterface arg0, int arg1) {
                            finishAffinity();
                        }
                    })
                    .show(); }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        View myView;

        public PostViewHolder(View itemView) {
            super(itemView);
            myView = itemView;
        }

        public void setNomeLocale(String nomeLocale) {
            TextView post_nomelocale = (TextView) myView.findViewById(R.id.nomelocale);
            post_nomelocale.setText(nomeLocale);
        }

        public void setImmagine(String url,Context c)  {
            ImageView post_immagine = (ImageView) myView.findViewById(R.id.immagine);
            Picasso.with(c).load(url).centerCrop().resize(100,60).into(post_immagine);
        }

        public void setPreferiti(String string) {
            TextView preferiti_add=(TextView) myView.findViewById(R.id.preferiti_add);
            preferiti_add.setText(string);
        }

        public void setStelline(float rate, float iter) {
            RatingBar post_stelline = (RatingBar) myView.findViewById(R.id.stelline);
            if (rate / iter < 0) {
                post_stelline.setRating(0);
            } else {
                post_stelline.setRating(rate/iter);
            }
        }

        public void setIcon(String s) {
            ImageView i=(ImageView) myView.findViewById(R.id.iconcina);
            if(s.equals("gone"))  i.setVisibility(View.INVISIBLE);
            else  i.setVisibility(View.VISIBLE);

        }
    }

    public static class CittàViewHolder extends RecyclerView.ViewHolder {
        View myView;

        public CittàViewHolder(View itemView) {
            super(itemView);
            myView = itemView;
        }

        private void setNomeCittà(String nomeLocale) {
            TextView post_nomelocale = (TextView) myView.findViewById(R.id.nomecitta);
            post_nomelocale.setText(nomeLocale);
        }
    }
}