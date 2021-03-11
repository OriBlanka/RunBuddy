package com.runbuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class MapsFragment extends Fragment {

    GoogleMap map;
    LatLng currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    Button submit;
    TextInputEditText radiusText;
    int radius = 100;

    private RequestQueue _queue;

    private static final String ACTIVITY_TAG = "mapFragment";
    private static final String SERVER_ADDRESS = "http://10.0.0.25:8080/";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 100;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            Location location = null;

            map = googleMap;
            map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                location = getMyLocation();
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }

            currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Your current location"));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,17));
            drawCircle(radius);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        radiusText = (TextInputEditText)view.findViewById(R.id.radiusEditText);
        submit = (Button)view.findViewById(R.id.submitRadiusButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radius = Integer.parseInt(radiusText.getText().toString());
                drawCircle(radius);
                JSONObject requestObject = new JSONObject();

                    try {
                        requestObject.put("email", LoginActivity.userName);
                        requestObject.put("radius", radius);
                    }
                    catch (JSONException e) {
                        Log.e(ACTIVITY_TAG, "error in changing radius setting");
                        Toast.makeText(view.getContext(), "Please try again later :)", Toast.LENGTH_SHORT).show();
                    }

                    JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,  SERVER_ADDRESS + LoginActivity.userName + "/radius",
                            requestObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(ACTIVITY_TAG, "radius sent successfully");
                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e(ACTIVITY_TAG, "Failed to send radius - " + error);
                                }
                            });
                    _queue.add(req);
                }
            });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }


    public MapsFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingPermission")
    public Location getMyLocation() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null)
        {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude())) // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        return location;
    }

    public void drawCircle (int radius){
        map.addCircle(new CircleOptions()
                .center(currentLocation)
                .radius(radius)
                .strokeWidth(0f)
                .fillColor(Color.argb(70,150,50,50)));
    }
}