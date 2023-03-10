package com.dmaziarek_tus.plant_monitor_app.model;

import java.util.ArrayList;

public class PlantNamesSingleton {
    private static PlantNamesSingleton instance = null;
    private ArrayList<String> plantNameList;

    private PlantNamesSingleton() {}

    public static PlantNamesSingleton getInstance() {
        if (instance == null) {
            instance = new PlantNamesSingleton();
        }
        return instance;
    }

    // Used when the user needs to retrieve all the plants they have added
    public ArrayList<String> getPlantNames() {
        return plantNameList;
    }

    // Used when the user adds a new plant
    public void addPlantName(String plantName) {
        plantNameList.add(plantName);
    }

    // Should only be called once when the user signs in
    public void setPlantNames(ArrayList<String> plantNames) {
        this.plantNameList = plantNames;
    }
}
