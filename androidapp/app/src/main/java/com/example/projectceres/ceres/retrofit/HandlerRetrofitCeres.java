package com.example.projectceres.ceres.retrofit;

import com.example.projectceres.ceres.model.Localizacion;
import com.example.projectceres.ceres.model.Warning;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HandlerRetrofitCeres {


    public HandlerRetrofitCeres() {

    }


    public static ApiRest initRetrofit() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.URL_BASE)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiRest apiRest = retrofit.create(ApiRest.class);

        return apiRest;
    }

    public static void getLocation(ApiRest apiRest, Callback<Localizacion> localizacion) {

        Call<Localizacion> call;

        call = apiRest.getLocationApi();

        call.clone().enqueue(localizacion);
    }

    public static void getWarning(ApiRest apiRest, Callback<Warning> warning, String lat, String lng, String olat, String olng) {

        Call<Warning> call;

        call = apiRest.getWarning(lat, lng, olat, olng);

        call.clone().enqueue(warning);
    }

}