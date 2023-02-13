package com.dmaziarek_tus.plant_monitor_app;

public class Plant {
    String name;
    int soilMoisture;
    double irLight;
    double uvLight;
    double visLight;
    double humidity;
    double temperature;

    public Plant() {
    }

    public Plant(String name, int soilMoisture, double irLight, double uvLight, double visLight, double humidity, double temperature) {
        this.name = name;
        this.soilMoisture = soilMoisture;
        this.irLight = irLight;
        this.uvLight = uvLight;
        this.visLight = visLight;
        this.humidity = humidity;
        this.temperature = temperature;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSoilMoisture() {
        return soilMoisture;
    }

    public void setSoilMoisture(int soilMoisture) {
        this.soilMoisture = soilMoisture;
    }

    public double getIrLight() {
        return irLight;
    }

    public void setIrLight(double irLight) {
        this.irLight = irLight;
    }

    public double getUvLight() {
        return uvLight;
    }

    public void setUvLight(double uvLight) {
        this.uvLight = uvLight;
    }

    public double getVisLight() {
        return visLight;
    }

    public void setVisLight(double visLight) {
        this.visLight = visLight;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
