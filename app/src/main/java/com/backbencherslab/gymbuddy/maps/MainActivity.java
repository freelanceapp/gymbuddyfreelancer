package com.backbencherslab.gymbuddy.maps;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.backbencherslab.gymbuddy.DatabaseHelper;
import com.backbencherslab.gymbuddy.DatabaseModel;
import com.backbencherslab.gymbuddy.SecondActivity;
import com.backbencherslab.gymbuddy.app.App;
import com.backbencherslab.gymbuddy.constants.Constants;
import com.backbencherslab.gymbuddy.maps.log.L;
import com.backbencherslab.gymbuddy.maps.model.CoffeeShops;
import com.backbencherslab.gymbuddy.maps.model.Results;
import com.backbencherslab.gymbuddy.maps.rest.GooglePlacesService;
import com.backbencherslab.gymbuddy.util.CustomRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import com.backbencherslab.gymbuddy.R;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, Constants,
        GoogleMap.OnCameraChangeListener,
        GoogleMap.InfoWindowAdapter,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnInfoWindowLongClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnInfoWindowCloseListener, GoogleMap.OnInfoWindowClickListener,
        SeekBar.OnSeekBarChangeListener{


    public static MainActivity newInstance() {
        MainActivity fragment = new MainActivity();
        return fragment;
    }

    public MainActivity() {

    }

    public static final int RESULT_OK = -1;

    private ProgressDialog pDialog;
    private Boolean loading = false;
    DatabaseHelper helpher;
    List<DatabaseModel> dbList;

    private static final String STATE_RESOLVING_ERROR = "resolving_error";
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    protected GoogleApiClient mGoogleApiClient;
    private boolean mResolvingError;
    private LocationRequest mLocationRequest;
    private Location mLocation;
    private LatLng mDummyLatLng = new LatLng(App.getInstance().getLat(),App.getInstance().getLng());
    private LatLng toPosition = null;
    private GoogleMap mMap;
    private RecyclerView mRecyclerCoffeeShops;
    private CoffeeShopsAdapter mAdapter;
        String location;
    private void loadNearbyCoffeeShops(double latitude, double longitude) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://maps.googleapis.com")
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .setLog(new RestAdapter.Log() {

                    @Override
                    public void log(String message) {
                        Log.d("VIVZ", message);
                    }
                }).build();

        GooglePlacesService service = restAdapter.create(GooglePlacesService.class);
        service.getCafes(getHashMapWithQueryParameters(latitude, longitude), new CoffeeShopsCallback());
    }

    private Map<String, String> getHashMapWithQueryParameters(double latitude, double longitude) {
        Map<String, String> map = new HashMap<>(5);
        map.put("location", getCsvLatLng(latitude, longitude));
        map.put("radius", "2000");
        map.put("types", "gym");
        map.put("sensor", true + "");
        map.put("key", getString(R.string.google_maps_key));
        return map;
    }

    private String getCsvLatLng(double latitude, double longitude) {
        return latitude + "," + longitude;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_maps, container, false);
        setHasOptionsMenu(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        mAdapter = new CoffeeShopsAdapter(getActivity());
        mRecyclerCoffeeShops = (RecyclerView) rootView.findViewById(R.id.recycler_coffee_shops);
        mRecyclerCoffeeShops.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
        mRecyclerCoffeeShops.setAdapter(mAdapter);

        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(LocationServices.API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(Places.GEO_DATA_API)
                        .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setInterval(1000);
        mResolvingError = savedInstanceState != null
                && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false);

        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }



        return rootView;
    }

    protected void initpDialog() {

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.msg_loading));
        pDialog.setCancelable(false);
    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it to view nearby gyms and buddies?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onConnected(Bundle bundle) {
        L.m("onConnected");
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLocation == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            updateLocation(mLocation);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        L.m("onConnectionSuspended");
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(getActivity(), REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GooglePlayServicesUtil.getErrorDialog()
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }
    }
    // The rest of getActivity() code is all about building the error dialog

    /* Creates a dialog for an error message */
    protected void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getActivity().getSupportFragmentManager(), "errordialog");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == getActivity().RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }

    public void onDialogDismissed() {
        mResolvingError = false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(mDummyLatLng)
                .title(App.getInstance().getFullname())
                .draggable(true)
                .snippet("Friends: " + App.getInstance().getNewFriendsCount())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_gym_location_people));
        googleMap.addMarker(markerOptions);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(mDummyLatLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(App.getInstance().getLat(), App.getInstance().getLng()), 14.0F));
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnCameraChangeListener(this);
        googleMap.setOnInfoWindowClickListener(this);
        googleMap.setOnInfoWindowLongClickListener(this);
        googleMap.setOnMarkerDragListener(this);
        googleMap.setOnMapClickListener(this);
        googleMap.setBuildingsEnabled(true);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        updateLocation(mLocation);
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    private void updateLocation(Location mLocation) {
        LatLng currentPosition = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        onLocationUpdate(currentPosition);
    }
    public void onLocationUpdate(LatLng latLng) {
        loadNearbyCoffeeShops(latLng.latitude, latLng.longitude);

    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        mMap.clear();

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

        toPosition = marker.getPosition();

        loadNearbyCoffeeShops(marker.getPosition().latitude, marker.getPosition().longitude);

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }


    @Override
    public void onInfoWindowLongClick(Marker marker) {
        marker.getTitle();
        marker.getSnippet();
        marker.getPosition();

    }

    @Override
    public void onInfoWindowClose(Marker marker) {

    }

    @Override
    public void onInfoWindowClick(final Marker marker) {


        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.view_custom_marker, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptView);

        final TextView gname = (TextView) promptView.findViewById(R.id.textView1);
        final TextView gaddress = (TextView) promptView.findViewById(R.id.textView2);

        gname.setText(marker.getTitle());
        gaddress.setText(marker.getSnippet());
        alertDialogBuilder.setCancelable(false)

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        location = marker.getTitle();
                        saveSettings();

                        String name = (marker.getTitle());

                        String address = (marker.getSnippet());

                        Double branch = (marker.getPosition().latitude);
                        Double email = (marker.getPosition().longitude);

                            helpher = new DatabaseHelper(getActivity());
                            helpher.insertIntoDB(name, address, branch, email);

                    }


                })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(latLng.latitude,latLng.longitude))
                .title(App.getInstance().getFullname())
                .draggable(true)
                .snippet("Friends: " + App.getInstance().getNewFriendsCount())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_gym_location_people));
        mMap.addMarker(markerOptions);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latLng.latitude,latLng.longitude)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude,latLng.longitude), 14.0F));
        mMap.setOnMarkerClickListener(this);
        mMap.setOnCameraChangeListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnInfoWindowLongClickListener(this);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapClickListener(this);
        loadNearbyCoffeeShops(latLng.latitude, latLng.longitude);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        return null;
    }

    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GooglePlayServicesUtil.getErrorDialog(errorCode,
                    this.getActivity(), REQUEST_RESOLVE_ERROR);
        }
    }


        public class CoffeeShopsCallback implements Callback<CoffeeShops> {

                @Override
                public void success (CoffeeShops coffeeShops, Response response){
                Log.d("Maps", coffeeShops.toString());
                String status = coffeeShops.getStatus();

                if (status.equals(getString(R.string.status_ok))) {

                    ArrayList<Results> listCoffeeShops = new ArrayList<>(60);
                    for (Results current : coffeeShops.getResults()) {

                        double latitude = Double.valueOf(current.getGeometry().getLocation().getLatitude());
                        double longitude = Double.valueOf(current.getGeometry().getLocation().getLongitude());
                        LatLng position = new LatLng(latitude, longitude);
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(position)
                                .title(current.getName())
                                .snippet(current.getVicinity() + ", Rating: " +current.getRating())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_gym_location));
                        mMap.addMarker(markerOptions);
                    }

                } else if (status.equals(getString(R.string.status_over_query_limit))) {

                    Toast.makeText(getActivity(), "Limit Reached", Toast.LENGTH_SHORT).show();

                } else if (status.equals(getString(R.string.status_request_denied))) {
                    //The key is invalid in getActivity() case
                    Toast.makeText(getActivity(), "Request Denied", Toast.LENGTH_SHORT).show();
                } else if (status.equals(getString(R.string.status_invalid_request))) {
                    //Some parameters are missing
                } else {
                    L.s(getActivity(), "We didnt find any fitness centers near you.. Please try again in a minute");
                mMap.clear();
                }
            }
             @Override
                public void failure (RetrofitError error){
                L.s(getActivity(), error.toString());
            }
    }

    public void saveSettings() {

        loading = true;

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_ACCOUNT_SAVE_SETTINGS, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.has("error")) {

                                if (!response.getBoolean("error")) {

                                    location = response.getString("location");

                                    Toast.makeText(getActivity(), getText(R.string.msg_settings_saved), Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent();
                                    i.putExtra("location", location);

                                    getActivity().setResult(RESULT_OK, i);

                                }
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            loading = false;

                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loading = false;

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("location", location);

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }



    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);

        menu.clear();

        inflater.inflate(R.menu.menu_nearby1, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_map: {

                startActivity(new Intent(getActivity(), SecondActivity.class));

                return true;
            }
            default: {

                return super.onOptionsItemSelected(item);
            }
        }
    }

}



