package dev.peihana.yourbestlive;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;
    String nomelocale="";
    String citta="";
    Double x=0d,y=0d;
    int i=0;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView= inflater.inflate(R.layout.fragment_map, container, false);
        nomelocale= getArguments().getString("fragmentlocale");
        citta= getArguments().getString("fragmentcitta");
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView= (MapView) mView.findViewById(R.id.mappone);
        if (mMapView!=null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        ref.child("Città").child(citta).child(nomelocale).child("location").child("locX").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                x = dataSnapshot.getValue(Double.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        ref.child("Città").child(citta).child(nomelocale).child("location").child("locY").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                y = dataSnapshot.getValue(Double.class);
                mGoogleMap= googleMap;
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMap.addMarker(new MarkerOptions().position(new LatLng(x, y)).title(nomelocale));
                CameraPosition cameraP=CameraPosition.builder().target(new LatLng(x, y)).zoom(16).build();
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraP)); }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}