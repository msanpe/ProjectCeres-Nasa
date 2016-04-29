package com.example.projectceres.ceres.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Proyectos on 24/4/16.
 */
public class Medidas {

    @SerializedName("temp")
    public String temperatura;

    @SerializedName("windSpeed")
    public String velocidadViento;

    @SerializedName("windGust")
    public String rafagaViento;

    @SerializedName("rain")
    public String precipitaciones;

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public String getVelocidadViento() {
        return velocidadViento;
    }

    public void setVelocidadViento(String velocidadViento) {
        this.velocidadViento = velocidadViento;
    }

    public String getRafagaViento() {
        return rafagaViento;
    }

    public void setRafagaViento(String rafagaViento) {
        this.rafagaViento = rafagaViento;
    }

    public String getPrecipitaciones() {
        return precipitaciones;
    }

    public void setPrecipitaciones(String precipitaciones) {
        this.precipitaciones = precipitaciones;
    }
}