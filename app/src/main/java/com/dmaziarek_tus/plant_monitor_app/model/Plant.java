package com.dmaziarek_tus.plant_monitor_app.model;

public class Plant {
    String plantID;
    String plantName;
    int soil_Moisture;
    double irLight;
    double uvLight;
    double visLight;
    double humidity;
    double temperature;
    String plantType;
    String photoUrl;

    public Plant() {

    }

    public Plant(String plantID, String plantName, String plantType, String photoUrl) {
        this.plantID = plantID;
        this.plantName = plantName;
        this.plantType = plantType;
        this.photoUrl = photoUrl;
        this.soil_Moisture = 0;
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

    public int getSoil_Moisture() {
        return soil_Moisture;
    }

    public void setSoil_Moisture(int soil_Moisture) {
        this.soil_Moisture = soil_Moisture;
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
}
