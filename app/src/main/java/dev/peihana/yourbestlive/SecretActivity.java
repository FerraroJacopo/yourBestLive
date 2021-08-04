package dev.peihana.yourbestlive;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SecretActivity extends AppCompatActivity implements View.OnClickListener {


    private RecyclerView Recycler;
    public FirebaseDatabase database= FirebaseDatabase.getInstance();
    public DatabaseReference ref=database.getReference();
    public DatabaseReference commenti;
    Button button_recensionii;
    TextView contatto_risp;
    TextView pubblico_risp;
    TextView genere_risp;
    TextView backline_risp;
    TextView pagamento_risp;
    TextView band_risp;
    TextView via;
    String nome_locale;
    TextView via_text;
    ImageButton image_b;
    RatingBar rating_bar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nome_locale = extras.getString("nome_locale");
        }
        setContentView(R.layout.activity_secret);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(nome_locale);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        // Set title of Detail page
        collapsingToolbar.setTitle(nome_locale);
        ref.child("Secret").child(nome_locale).child("imageUrl").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String url=dataSnapshot.getValue(String.class);
                ImageView immagine= (ImageView)findViewById(R.id.image);
                Picasso.with(getApplicationContext()).load(url).into(immagine);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        image_b=(ImageButton) findViewById(R.id.imageButton);
        image_b.setOnClickListener(this);
        via_text=(TextView) findViewById(R.id.via_text);
        rating_bar=(RatingBar) findViewById(R.id.add_rating);
        rating_bar.setStepSize(0.5f);
        button_recensionii=(Button) findViewById(R.id.b_review);
        button_recensionii.setOnClickListener(this);
        contatto_risp=(TextView) findViewById(R.id.contatto_risp);
        pubblico_risp=(TextView) findViewById(R.id.pubblico_risp);
        backline_risp=(TextView) findViewById(R.id.backline_risp);
        genere_risp=(TextView) findViewById(R.id.genere_risp);
        band_risp=(TextView) findViewById(R.id.band_risp);
        pagamento_risp=(TextView) findViewById(R.id.pagamento_risp);
        via=(TextView) findViewById(R.id.via);
        iniziaCommenti();
        setBand();
        setGenere();
        setBackline();
        setContatto();
        setPubblico();
        setPagamento();
        setVia();
        setStelline();

    }

    public void iniziaCommenti() {
        Recycler= (RecyclerView) findViewById(R.id.list_commenti);
        Recycler.setLayoutManager(new LinearLayoutManager(this));
        //cioccabicciocca
        commenti= ref.child("Secret").child(nome_locale).child("commenti");

        commenti.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()>0) {
                    TextView tex= (TextView)findViewById(R.id.commentoscomparsa);
                    tex.setVisibility(View.GONE);
                    parti();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_locale, menu);
        return true; }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); }

        return super.onOptionsItemSelected(item);
    }

    public void parti() {
        FirebaseRecyclerAdapter<Commento,CommentoViewHolder> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<Commento, CommentoViewHolder>(
                        Commento.class,
                        R.layout.single_row,
                        CommentoViewHolder.class,
                        commenti
                ) {
                    @Override
                    protected void populateViewHolder(CommentoViewHolder viewHolder, Commento model, int position) {
                        viewHolder.setCommento(model.getCommento());
                        viewHolder.setUtente(model.getUtente());
                    }
                };
        Recycler.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onClick(View v) {
        if(v==button_recensionii) {
            Toast.makeText(SecretActivity.this,
                    "No need to review it",
                    Toast.LENGTH_SHORT).show(); }
        if(v==image_b) {
            Toast.makeText(SecretActivity.this,
                    "We want this place to remain secret, don't you think?",
                    Toast.LENGTH_SHORT).show(); } }

    public void setStelline() {

        ref.child("Secret").child(nome_locale).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                float rate=snapshot.child("rate").getValue(Float.class);
                float iter=snapshot.child("iterazioni").getValue(Float.class);
                if(rate/iter<0) rating_bar.setRating(0);
                else rating_bar.setRating(rate/iter);
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) { } }); }


    public void setBand() {
        ref.child("Secret").child(nome_locale).child("band").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String string=snapshot.getValue(String.class);
                band_risp.setText(string);
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) { } }); }

    public void setVia() {
        ref.child("Secret").child(nome_locale).child("via").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String via=snapshot.getValue(String.class);
                via_text.setText(via);
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) { } }); }


    public void setBackline() {
        ref.child("Secret").child(nome_locale).child("backline").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String string = dataSnapshot.getValue(String.class);
                backline_risp.setText(string);
            }
            @Override
            public void onCancelled(DatabaseError error) {

            } } );}

    public void setPubblico() {
        ref.child("Secret").child(nome_locale).child("pubblico").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String string = dataSnapshot.getValue(String.class);
                pubblico_risp.setText(string); }

            @Override
            public void onCancelled(DatabaseError error) {

            } } );}

    public void setContatto() {
        ref.child("Secret").child(nome_locale).child("contatto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String string = dataSnapshot.getValue(String.class);
                contatto_risp.setText(string); }

            @Override
            public void onCancelled(DatabaseError error) {

            } } );}

    public void setPagamento() {
        ref.child("Secret").child(nome_locale).child("pagamento").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);
                pagamento_risp.setText(string); }

            @Override
            public void onCancelled(DatabaseError error) {

            } } );}

    public void setGenere() {
        ref.child("Secret").child(nome_locale).child("genere").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String string=snapshot.getValue(String.class);
                genere_risp.setText(string);
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) { } }); }

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
    }

}
