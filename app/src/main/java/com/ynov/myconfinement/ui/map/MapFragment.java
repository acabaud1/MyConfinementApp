package com.ynov.myconfinement.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ynov.myconfinement.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;
    OkHttpClient client = new OkHttpClient();

    private static String VELOV_URL = "https://public.opendatasoft.com/api/records/1.0/search/?dataset=station-velov-grand-lyon&rows=-1";

    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) root.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        MapsInitializer.initialize(getActivity().getApplicationContext());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // Zoom controls
                googleMap.getUiSettings().setZoomControlsEnabled(true);

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (googleMap != null) {
                        googleMap.setMyLocationEnabled(true);
                        fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    LatLng myPosition = new LatLng(location.getLatitude(), location.getLongitude());
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition));
                                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                                }
                            }
                        });
                    }
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{ Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }

                getRequest(VELOV_URL, new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                String jsonData = response.body().string();
                                JSONObject jsonObject = new JSONObject(jsonData);
                                JSONArray jsonArray = jsonObject.getJSONArray("records");
                                List<JSONObject> items = new ArrayList<>();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject item = jsonArray.getJSONObject(i);
                                    items.add(item);
                                }

                                addMarkers(items);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.e("VELOV_ERROR", "Erreur lors de la récupération des stations");
                        }
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("VELOV_ERROR", e.getMessage());
                    }
                });

            }
        });

        return root;
    }

    private void addMarkers(final List<JSONObject> items)
    {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < items.size(); i++) {
                        JSONObject fields = items.get(i).getJSONObject("fields");
                        JSONArray coordinates = fields.getJSONArray("geo_point_2d");
                        LatLng position = new LatLng(coordinates.getDouble(0), coordinates.getDouble(1));

                        googleMap.addMarker(new MarkerOptions()
                                .position(position)
                                .title(fields.getString("name"))
                                .snippet(fields.getString("available") + " / " + fields.getString("bike_stand"))
                                .icon(generateBitmapDescriptorFromRes(getContext(), R.drawable.ic_directions_bike_black_24dp)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    Call getRequest(String url, Callback callback) {
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public static BitmapDescriptor generateBitmapDescriptorFromRes(Context context, int resId) {
        Drawable drawable = ContextCompat.getDrawable(context, resId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}