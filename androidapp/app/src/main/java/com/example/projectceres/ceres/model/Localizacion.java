package com.example.projectceres.ceres.model;

import com.google.gson.annotations.SerializedName;

public class Localizacion {

    @SerializedName("lat")
    public String latitud;

    @SerializedName("lng")
    public String longitud;

    @Override
    public String toString() {
        return "Localizacion{" +
                "latitud='" + latitud + '\'' +
                ", longitud='" + longitud + '\'' +
                '}';
    }
}