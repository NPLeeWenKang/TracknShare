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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import sg.edu.np.tracknshare.handlers.TrackingDBHandler;
import sg.edu.np.tracknshare.models.MyLatLng;

public class MapsFragment extends Fragment {
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
                    Log.e("MAP", "onMapLoaded: ");
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();

                    TrackingDBHandler trackingDB = new TrackingDBHandler(c);
                    ArrayList<MyLatLng> llList = trackingDB.getAllPoints();
                    for (int i = 0; i < llList.size() - 1; i++) {
                        LatLng latLng = new LatLng(llList.get(i).latitude, llList.get(i).longitude);// in this line put you lat and long
                        builder.include(latLng);  //add latlng to builder
                    }

                    LatLngBounds bounds = builder.build();

                    int padding = 50; // offset from edges of the map in pixels
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                    googleMap.moveCamera(cu);
                    //googleMap.getUiSettings().setAllGesturesEnabled(false);

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
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
    @SuppressLint("MissingPermission")
    public void getCurrentLocation(GoogleMap googleMap){
        FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(getActivity());
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        LatLng currentLatLng= new LatLng(location.getLatitude(), location.getLongitude());
                        Log.e("Location 1 found", ""+currentLatLng.latitude+ ", " + currentLatLng.longitude);
                        updateLatLng = currentLatLng;
                        googleMap.setMyLocationEnabled(true);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
                        googleMap.getUiSettings().setAllGesturesEnabled(false);

                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                                .zoom(17)                   // Sets the zoom
                                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                                .build();                   // Creates a CameraPosition from the builder
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    } else {
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000);
                        LocationCallback locationCallback = new LocationCallback(){
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                Location location1 = locationResult.getLastLocation();
                                LatLng latLng = new LatLng(location1.getLatitude(), location1.getLongitude());
                                updateLatLng = latLng;
                                Log.e("Location 2 found", ""+latLng.latitude+ ", " + latLng.longitude);
                            }
                        };
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
                    }
                }
            });
        }
        else{
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
    public void setPoints(GoogleMap googleMap, ArrayList<MyLatLng> llList){
        TrackingDBHandler db = new TrackingDBHandler(getActivity());
        for (int i = 0; i < llList.size() - 2; i++) {
            MyLatLng src = llList.get(i);
            MyLatLng dest = llList.get(i + 1);
            Log.e("Location", "new point"+i);
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