package com.example.projectceres.ceres.retrofit;

import com.example.projectceres.ceres.model.Localizacion;
import com.example.projectceres.ceres.model.Warning;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiRest {

    @GET(ApiConstants.GET_LOCATION)
    Call<Localizacion> getLocationApi();

    @GET(ApiConstants.GET_WARNING)
    Call<Warning> getWarning(@Query(ApiConstants.PARAM_LAT) String lat, @Query(ApiConstants.PARAM_LNG) String lng, @Query(ApiConstants.PARAM_OLAT) String olat, @Query(ApiConstants.PARAM_OLNG) String olng);

    //@GET(ApiConstants.GET_LIBROS)
    //Call<Libros> getLibrosApi(@Query(ApiConstants.TAG_ApiKey) String apikey, @Query(ApiConstants.TAG_kimmodify) String kimmodify);

    //@GET(ApiConstants.GET_RSS_CRIMVIAL)
    //Call<Articulos> getRssCrimVial();
}