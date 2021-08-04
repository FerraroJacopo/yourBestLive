package dev.peihana.yourbestlive;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class PreferitiActivity extends AppCompatActivity {

    private RecyclerView recycler;
    public FirebaseDatabase database= FirebaseDatabase.getInstance();
    public DatabaseReference ref=database.getReference();
    private DatabaseReference mRef;
    private FirebaseUser user;
    private ProgressDialog dialog;
    LinearLayout linear;
    TabLayout tab_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {//this method sets actionbars and colors, swipe logic to the recycler activity and message when there are no favourite locals
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferiti);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Favourites");

        recycler = (RecyclerView) findViewById(R.id.preferiti_list);
        user= FirebaseAuth.getInstance().getCurrentUser();
        recycler.setLayoutManager(new LinearLayoutManager(this));
        linear=(LinearLayout) findViewById(R.id.lineare);
        mRef=ref.child("Users").child(user.getDisplayName()).child("Preferiti");
        Intent i=getIntent();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading favourite clubs..");
        tab_layout=(TabLayout) findViewById(R.id.tabs);
        tab_layout.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#ffffff")));
        tab_layout.setSelectedTabIndicatorColor(Color.parseColor("#ffffff"));
        final TabLayout.Tab tab2=tab_layout.getTabAt(1);
        tab2.select();
        recycler.setOnTouchListener(new OnSwipeTouchListener(PreferitiActivity.this)
        {

            @Override
            public void onClick()
            {
                super.onClick();
            }

            @Override
            public void onDoubleClick()
            {
                super.onDoubleClick();
                // your on onDoubleClick here
            }

            @Override
            public void onLongClick()
            {
                super.onLongClick();
                // your on onLongClick here
            }

            @Override
            public void onSwipeUp() {
                super.onSwipeUp();
                // your swipe up here
            }

            @Override
            public void onSwipeDown() {
                super.onSwipeDown();
                // your swipe down here.
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                // your swipe right here.
                Intent go = new Intent(PreferitiActivity.this,RecyclerActivity.class);
                startActivity(go);
                overridePendingTransition(0, 0);
                finish();

            }
        });
        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()) {
                    case 0:
                        Intent go = new Intent(PreferitiActivity.this,RecyclerActivity.class);
                        startActivity(go);
                        overridePendingTransition(0, 0);
                        tab2.select();
                        finish();
                        break;

                } }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()==0) {
                    TextView tex= (TextView)findViewById(R.id.cscomparsa);
                    tex.setVisibility(View.VISIBLE);
                    dialog.hide();
                }
                else if (dataSnapshot.getChildrenCount()>0) {

                    parti(mRef);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            Intent go = new Intent(PreferitiActivity.this,RecyclerActivity.class);
            startActivity(go);
            overridePendingTransition(0, 0);
            finish();// close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


    public class OnSwipeTouchListener implements View.OnTouchListener {

        private GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context c) {
            gestureDetector = new GestureDetector(c, new OnSwipeTouchListener.GestureListener());
        }

        public boolean onTouch(final View view, final MotionEvent motionEvent) {
            return gestureDetector.onTouchEvent(motionEvent);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

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

            // Determines the fling velocity and then fires the appropriate swipe event accordingly
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD)
                        {
                            if (diffX > 0)
                            {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                        }
                    } else {
                        if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffY > 0) {
                                onSwipeDown();
                            } else {
                                onSwipeUp();
                            }
                        }
                    }
                } catch (Exception exception) {
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

        } }


    public void parti(final DatabaseReference reference) {
        FirebaseRecyclerAdapter<Preferiti, PreferitiViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Preferiti, PreferitiViewHolder>(
                        Preferiti.class,
                        R.layout.single_item_pref,
                        PreferitiViewHolder.class,
                        reference
                ) {
                    @Override
                    protected void populateViewHolder(final PreferitiViewHolder viewHolder, final Preferiti model, final int position) {

                        String stringa = getRef(position).getKey();
                        int index=stringa.indexOf("_");
                        String nome_locale=stringa.substring(0,index);
                        final String city=stringa.substring(index+1);

                        ref.child("Città").child(city).child(nome_locale).child("imageUrl").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String immaginerl = dataSnapshot.getValue(String.class);
                                if (model.getImageUrl().equals(immaginerl)) {
                                    viewHolder.setImmagine(model.getImageUrl(),getApplicationContext());
                                }
                                else {
                                    mRef.child(model.getNome_locale()+"_"+city).child("imageUrl").setValue(immaginerl);
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                        viewHolder.setNomeLocale(model.getNome_locale());
                        viewHolder.setVia(model.getVia());
                        viewHolder.setImmagine(model.getImageUrl(),getApplicationContext());
                        dialog.hide();
                        viewHolder.myView.setOnTouchListener(new OnSwipeTouchListener(PreferitiActivity.this)
                        {

                            @Override
                            public void onClick()
                            {
                                super.onClick();
                                // your on click here
                                String stringa = getRef(position).getKey();
                                int index=stringa.indexOf("_");
                                String nome_locale=stringa.substring(0,index);
                                String city=stringa.substring(index+1);

                                Intent i = new Intent(PreferitiActivity.this, LocaleActivity.class);
                                i.putExtra("nome_locale",nome_locale);
                                i.putExtra("citta",city);
                                startActivity(i);
                            }

                            @Override
                            public void onDoubleClick()
                            {
                                super.onDoubleClick();
                                // your on onDoubleClick here
                            }

                            @Override
                            public void onLongClick()
                            {
                                super.onLongClick();
                                // your on onLongClick here
                            }

                            @Override
                            public void onSwipeUp() {
                                super.onSwipeUp();
                                // your swipe up here
                            }

                            @Override
                            public void onSwipeDown() {
                                super.onSwipeDown();
                                // your swipe down here.
                            }

                            @Override
                            public void onSwipeLeft() {
                                super.onSwipeLeft();
                            }

                            @Override
                            public void onSwipeRight() {
                                super.onSwipeRight();
                                // your swipe right here.
                                Intent go = new Intent(PreferitiActivity.this,RecyclerActivity.class);
                                startActivity(go);
                                overridePendingTransition(0, 0);
                                finish();

                            }
                        });
                    }
                };
        recycler.setAdapter(firebaseRecyclerAdapter);
    }

    public static class PreferitiViewHolder extends RecyclerView.ViewHolder {
        View myView;
        //inserire roba da far vedere

        public PreferitiViewHolder(View itemView) {
            super(itemView);
            myView = itemView;
        }

        public void setNomeLocale(String nomeLocale) {
            TextView post_nomelocale = (TextView) myView.findViewById(R.id.nomelocale);
            post_nomelocale.setText(nomeLocale);
        }

        public void setVia(String string) {
            TextView post_via = (TextView) myView.findViewById(R.id.via_id);
            post_via.setText(string);
        }

        public void setImmagine(String url,Context c)  {
            ImageView post_immagine = (ImageView) myView.findViewById(R.id.immagine);
            Picasso.with(c).load(url).centerCrop().resize(100,60).into(post_immagine);
        }
    }
}

