package sg.edu.np.tracknshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import sg.edu.np.tracknshare.handlers.TrackingDBHandler;
import sg.edu.np.tracknshare.models.MyLatLng;

public class FullMapFragment extends Fragment {
    public static GoogleMap map;
    private Context c;
    LatLng updateLatLng = new LatLng(0, 0);
    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {

            googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    Log.e("MAPFULL", "onMapLoaded: ");
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();

                    TrackingDBHandler trackingDB = new TrackingDBHandler(c);
                    ArrayList<MyLatLng> llList = trackingDB.getAllPoints();
                    for (int i = 0; i <= llList.size() - 1; i++) {
                        LatLng latLng = new LatLng(llList.get(i).latitude, llList.get(i).longitude);// in this line put you lat and long
                        builder.include(latLng);  //add latlng to builder
                    }
                    LatLngBounds bounds = builder.build();

                    int padding = 150; // offset from edges of the map in pixels
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                    googleMap.moveCamera(cu);

                    googleMap.getUiSettings().setAllGesturesEnabled(false);

                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                        public boolean onMarkerClick(Marker marker) {
                            return true;
                        }
                    });
                    setPoints(googleMap, llList);
                }
            });
            map = googleMap;
            //getCurrentLocation(googleMap);
        }

    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        c = view.getContext();
        Log.d("MAPFULL", "onViewCreated: ");
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fullMap);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    public void setPoints(GoogleMap googleMap, ArrayList<MyLatLng> llList){
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(llList.get(0).latitude, llList.get(0).longitude))
                .title("Start"));
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(llList.get(llList.size()-1).latitude, llList.get(llList.size()-1).longitude))
                .title("End"));
        for (int i = 0; i <= llList.size() - 2; i++) {
            MyLatLng src = llList.get(i);
            MyLatLng dest = llList.get(i + 1);
            // mMap is the Map Object
            googleMap.addPolyline(new PolylineOptions()
                    .add(
                            new LatLng(src.latitude, src.longitude),
                            new LatLng(dest.latitude,dest.longitude)
                    )
                    .width(10).color(R.color.purple_200)
            );
        }
    }
}