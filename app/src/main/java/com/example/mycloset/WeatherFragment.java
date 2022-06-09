package com.example.mycloset;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RelativeLayout homeRL;
    private ProgressBar loadingPB;
    private TextView cityNameTV, temperatureTV, conditionTV, codyTipTV;
    private TextInputEditText cityEdt;
    private ImageView backIV, iconIV, searchIV;
    private LocationManager locationManager;
    private String cityName;
    private final int PERMISSION_CODE = 1;
    private String[] codyTipArray;


    public WeatherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeatherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeatherFragment newInstance(String param1, String param2) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        codyTipArray = getResources().getStringArray(R.array.codyTip_array);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_weather, container, false);
        homeRL = v.findViewById(R.id.idRLHome);
        loadingPB = v.findViewById(R.id.idPBLoading);
        cityNameTV = v.findViewById(R.id.idTVCityName);
        temperatureTV = v.findViewById(R.id.idTVTemperature);
        conditionTV = v.findViewById(R.id.idTVCondition);
        cityEdt = v.findViewById(R.id.idEditCity);
        backIV = v.findViewById(R.id.idIVBack);
        iconIV = v.findViewById(R.id.idIVIcon);
        searchIV = v.findViewById(R.id.idIVSearch);
        codyTipTV = v.findViewById(R.id.idTVCodyTip);


        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        cityName = getCityName(location.getLongitude(), location.getLatitude());
        getWeatherInfo(cityName);

        searchIV.setOnClickListener(view -> {
            String city = cityEdt.getText().toString();
            if(city.isEmpty()){
                Toast.makeText(getActivity(), "Please enter city Name", Toast.LENGTH_SHORT).show();
            }
            else {
                cityNameTV.setText(cityName);
                getWeatherInfo(city);
            }
        });
        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getActivity(), "Permissions granted..", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getActivity(), "Please provide the permissions", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }

    private String getCityName(double longitude, double latitude){
        String cityName = "Not found";
        Geocoder gcd = new Geocoder(getActivity().getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 10);

            for(Address adr : addresses){
                if(adr != null) {
                    String city = adr.getLocality();
                    if(city!=null && !city.equals("")) {
                        cityName = city;
                    }
                    else {
                        Log.d("TAG", "CITY NOT FOUND");
                        //Toast.makeText(getActivity(), "User City Not Found..", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return cityName;
    }

    private void getWeatherInfo(String cityName){
        String url = "http://api.weatherapi.com/v1/forecast.json?key=c03ce3c4e0dc4e509d3183527220506&q="+cityName+"&days=1&aqi=no&alerts=no";
        cityNameTV.setText(cityName);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingPB.setVisibility(View.GONE);
                homeRL.setVisibility(View.VISIBLE);

                try {
                    String temperature = response.getJSONObject("current").getString("temp_c");
                    temperatureTV.setText(temperature+"°C");
                    float temperatureFloat = Float.parseFloat(temperature);
                    if (temperatureFloat < 5) {
                        codyTipTV.setText(codyTipArray[0]);
                    }
                    else if (temperatureFloat >= 5 && temperatureFloat < 9){
                        codyTipTV.setText(codyTipArray[1]);
                    }
                    else if (temperatureFloat >= 9 && temperatureFloat < 12){
                        codyTipTV.setText(codyTipArray[2]);
                    }
                    else if (temperatureFloat >= 12 && temperatureFloat < 17){
                        codyTipTV.setText(codyTipArray[3]);
                    }
                    else if (temperatureFloat >= 17 && temperatureFloat < 20){
                        codyTipTV.setText(codyTipArray[4]);
                    }
                    else if (temperatureFloat >= 20 && temperatureFloat < 23){
                        codyTipTV.setText(codyTipArray[5]);
                    }
                    else if (temperatureFloat >= 23 && temperatureFloat < 28){
                        codyTipTV.setText(codyTipArray[6]);
                    }
                    else if (temperatureFloat >= 28){
                        codyTipTV.setText(codyTipArray[7]);
                    }
                    int isDay = response.getJSONObject("current").getInt("is_day");
                    String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String conditionIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("http:".concat(conditionIcon)).into(iconIV);
                    conditionTV.setText(condition);
                    if(isDay==1){
                        Picasso.get().load("https://images.unsplash.com/photo-1582716830767-589de2cd25a2?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1035&q=80").into(backIV);
                    }
                    else {
                        Picasso.get().load("https://images.unsplash.com/photo-1436891620584-47fd0e565afb?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=774&q=80").into(backIV);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(), "Please enter valid city name..", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}