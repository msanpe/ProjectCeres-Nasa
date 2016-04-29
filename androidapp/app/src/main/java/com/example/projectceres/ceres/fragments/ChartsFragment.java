package com.example.projectceres.ceres.fragments;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.projectceres.ceres.R;
import com.example.projectceres.ceres.model.Localizacion;
import com.example.projectceres.ceres.model.Warning;
import com.example.projectceres.ceres.retrofit.HandlerRetrofitCeres;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChartsFragment extends Fragment{

    private Localizacion localizacion;
    private View view;
    TextView textView, temperature, velocidad_viento, rafaga;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;

        textView = (TextView) view.findViewById(R.id.titulo_precipitaciones);
        temperature = (TextView) view.findViewById(R.id.temperature);
        velocidad_viento = (TextView) view.findViewById(R.id.velocidad_viento);
        rafaga = (TextView) view.findViewById(R.id.rafaga);

        final Handler handler = new Handler();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        comprobarServiciosLocalizacion();
                    }
                });
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask, 0, 5000);
    }

    private void comprobarServiciosLocalizacion() {

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(view.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(view.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationManager locationManager = (LocationManager) view.getContext().getSystemService(view.getContext().LOCATION_SERVICE);


        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 100, new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                getLocationRetrofit(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
        Log.d("activity", "LOC Network Enabled");
        if (locationManager != null) {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                Log.d("activity", "LOC by Network");

                getLocationRetrofit(location);
            }
        }
    }

    private void retrofit(Location location) {
        HandlerRetrofitCeres.getWarning(HandlerRetrofitCeres.initRetrofit(), new Callback<Warning>() {
            @Override
            public void onResponse(Call<Warning> call, Response<Warning> response) {
                if (response != null && response.body() != null) {

                    Log.d("CERES", response.body().predictions);

                    Log.d("CERES", "Lluvia: " + response.body().medidas.getPrecipitaciones());
                    textView.setText(response.body().medidas.getPrecipitaciones() + " mm");
                    temperature.setText(response.body().medidas.getTemperatura() + "º");
                    velocidad_viento.setText(response.body().medidas.getVelocidadViento() + " KM/h");
                    rafaga.setText(response.body().medidas.getRafagaViento() + " KM/h");


                    String aux = response.body().predictions.replace("[", "");
                    aux = aux.replace("]", "");

                    String[] arrayRiesgo = aux.split(",");
                    float[] arrayAux = new float[arrayRiesgo.length];

                    for (int i = 0; i < arrayRiesgo.length; i++) {
                        //Log.d("CERES", "Predicción riesgo double: " + (i+1) + ": " + arrayRiesgo[i]);
                        arrayAux[i] = Float.parseFloat(arrayRiesgo[i]);
                        Log.d("CERES", "Predicción riesgo: " + (i + 1) + ": " + arrayRiesgo[i]);
                    }

                    Log.d("CERES", "Riesgo: " + response.body().risk);


                    //fab.setBackgroundResource(auxDra);
                    //fab.setImageResource(auxDra);

                    //fab.setImageIcon();

                    //endregion

                    Log.d("CERES", "Array riesgo: " + arrayRiesgo.length);
                    Log.d("CERES", "Array aux: " + arrayAux.length);


                } else Log.d("CERES", "El objeto está vacío");
            }

            @Override
            public void onFailure(Call<Warning> call, Throwable t) {
                Log.e("CERES", "Ha caido");

            }
        }, localizacion.latitud, localizacion.longitud, "" + location.getLatitude(), "" + location.getLongitude());
    }


    private void getLocationRetrofit(final Location location) {

        HandlerRetrofitCeres.getLocation(HandlerRetrofitCeres.initRetrofit(), new Callback<Localizacion>() {
            @Override
            public void onResponse(Call<Localizacion> call, Response<Localizacion> response) {

                if (response != null) {
                    Log.d("CERES", response.body().toString());
                    localizacion = response.body();

                    retrofit(location);
                } else Log.d("CERES", "El objeto esta vacío");
            }

            @Override
            public void onFailure(Call<Localizacion> call, Throwable t) {

            }
        });

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_charts, container, false);
    }
}