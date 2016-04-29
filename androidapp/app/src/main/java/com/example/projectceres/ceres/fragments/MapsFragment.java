package com.example.projectceres.ceres.fragments;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.LineChartView;
import com.example.projectceres.ceres.R;
import com.example.projectceres.ceres.model.Localizacion;
import com.example.projectceres.ceres.model.Warning;
import com.example.projectceres.ceres.retrofit.HandlerRetrofitCeres;
import com.google.android.gms.maps.GoogleMap;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsFragment extends Fragment {

    //region Variables
    private final int PORT = 8080;
    private final String IP = "192.168.171.106";

    private float[] riesgo = new float[5];
    private float[] prediccionRiesgo;// = {3.5f, 4.7f, 4.3f, 8f, 6.5f};
    private Localizacion localizacion;
    LineChartView mChart = null;

    String[] mLabels = {"Ahora", "P1", "P2", "P3", "P4"};
    String[] label = {"", "", "", "", ""};
    private View view;

    FloatingActionButton fab;

    private GoogleMap mMap;
    //endregion


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_maps, null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //fab = (FloatingActionButton) view.findViewById(R.id.fab_takeoff);

        Log.i("thread", "out of the thread");
        this.view = view;

        mChart = (LineChartView) view.findViewById(R.id.chart1);

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


    private void chartOne() {

        // linea azul

        LineSet dataset = new LineSet(mLabels, prediccionRiesgo);
        dataset.setColor(Color.parseColor("#758cbb"))
                .setFill(Color.parseColor("#2d374c"))
                .setDotsColor(Color.parseColor("#758cbb"))
                .setThickness(1)
                .setDashed(new float[]{10f, 10f})
                .beginAt(0)
                .endAt(5);
        mChart.addData(dataset);

        //linea amarilla
        dataset = new LineSet(label, riesgo);
        dataset.setColor(Color.parseColor("#b3b5bb"))
                .setFill(Color.parseColor("#2d374c"))
                .setDotsColor(Color.parseColor("#ffc755"))
                .setThickness(1)
                .beginAt(0)
                .endAt(1);
        mChart.addData(dataset);

        // Chart
        mChart.setBorderSpacing(Tools.fromDpToPx(20))
                .setAxisBorderValues(0, 1)
                .setYLabels(AxisController.LabelPosition.NONE)
                .setLabelsColor(Color.parseColor("#6a84c3"))
                .setXAxis(false)
                .setYAxis(false);


        /*Animation anim = new Animation()
                .setEasing(new BounceEase())
                .setEndAction(new Runnable() {
                    @Override
                    public void run() {

                    }
                });

        mChart.show(anim)*/

        mChart.show();

        mChart.notifyDataUpdate();
        mChart.refreshDrawableState();
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


    private void updateChart() {
        this.chartOne();
    }


    private void retrofit(Location location) {
        HandlerRetrofitCeres.getWarning(HandlerRetrofitCeres.initRetrofit(), new Callback<Warning>() {
            @Override
            public void onResponse(Call<Warning> call, Response<Warning> response) {
                if (response != null && response.body() != null) {

                    Log.d("CERES", response.body().predictions);

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



                    prediccionRiesgo = arrayAux;


                    prediccionRiesgo[0] = Float.parseFloat(response.body().risk);
                    riesgo[0] = Float.parseFloat(response.body().risk);

                    //region Locura drawable
                    int auxDra = 0;

                    if(riesgo[0] == 1)
                        auxDra = R.drawable.ic_off;
                    else auxDra = R.drawable.ic_takeoff;

                    //fab.setBackgroundResource(auxDra);
                    //fab.setImageResource(auxDra);

                    //fab.setImageIcon();

                    //endregion

                    Log.d("CERES", "Array riesgo: " + arrayRiesgo.length);
                    Log.d("CERES", "Array aux: " + arrayAux.length);

                    updateChart();

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
}