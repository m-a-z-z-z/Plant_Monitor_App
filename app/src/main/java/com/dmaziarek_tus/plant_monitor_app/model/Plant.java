package com.dmaziarek_tus.plant_monitor_app.model;

import java.io.Serializable;

public class Plant implements Serializable {
    String plantID, plantName, plantType, photoUrl;
    int soilMoisture, minSoilMoisture, maxSoilMoisture;
    double irLight, uvLight, visLight, humidity, temperature, minTemp, maxTemp;

    public Plant() {}

    public Plant(String plantID, String plantName, String plantType, String photoUrl, int minSoilMoisture, int maxSoilMoisture, double minTemp, double maxTemp) {
        this.plantID = plantID;
        this.plantName = plantName;
        this.plantType = plantType;
        this.photoUrl = photoUrl;
        this.minSoilMoisture = minSoilMoisture;
        this.maxSoilMoisture = maxSoilMoisture;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.soilMoisture = 0;
        this.irLight = 0;
        this.uvLight = 0;
        this.visLight = 0;
        this.humidity = 0;
        this.temperature = 0;
    }

    public String getPlantID() {return plantID;}

    public void setPlantID(String plantID) { this.plantID = plantID;}

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getPlantName() {
        return plantName;
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

    public String getPlantType() {
        return plantType;
    }

    public void setPlantType(String plantType) {
        this.plantType = plantType;
    }

    public String getPhotoUrl() { return photoUrl; }

    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public int getMinSoilMoisture() { return minSoilMoisture; }

    public void setMinSoilMoisture(int minSoilMoisture) { this.minSoilMoisture = minSoilMoisture; }

    public int getMaxSoilMoisture() { return maxSoilMoisture; }

    public void setMaxSoilMoisture(int maxSoilMoisture) { this.maxSoilMoisture = maxSoilMoisture; }

    public double getMinTemp() { return minTemp; }

    public void setMinTemp(double minTemp) { this.minTemp = minTemp; }

    public double getMaxTemp() { return maxTemp; }

    public void setMaxTemp(double maxTemp) { this.maxTemp = maxTemp; }
}
