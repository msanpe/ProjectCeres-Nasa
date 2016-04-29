package com.example.projectceres.ceres.model;

import com.google.gson.annotations.SerializedName;

public class Warning {

    @SerializedName("risk")
    public String risk;

    @SerializedName("prediction")
    public String predictions;

    @SerializedName("weatherData")
    public Medidas medidas;
}