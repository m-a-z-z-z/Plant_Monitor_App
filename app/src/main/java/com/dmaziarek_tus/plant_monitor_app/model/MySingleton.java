package com.dmaziarek_tus.plant_monitor_app.model;

import java.util.ArrayList;

public class MySingleton {
    private static MySingleton instance = null;
    private ArrayList<String> plantNames;

    private MySingleton() {}

    public static MySingleton getInstance() {
        if (instance == null) {
            instance = new MySingleton();
        }
        return instance;
    }

    public ArrayList<String> getPlantNames() {
        return plantNames;
    }

    public void setPlantNames(ArrayList<String> plantNames) {
        this.plantNames = plantNames;
    }
}
