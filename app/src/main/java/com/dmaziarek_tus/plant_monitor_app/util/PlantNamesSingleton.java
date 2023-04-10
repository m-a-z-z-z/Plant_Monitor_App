package com.dmaziarek_tus.plant_monitor_app.util;

import com.dmaziarek_tus.plant_monitor_app.model.Plant;

import java.util.ArrayList;

public class PlantNamesSingleton {
    private static PlantNamesSingleton instance = null;
    private ArrayList<String> plantNameList;
    private ArrayList<Plant> plantList;

    private PlantNamesSingleton() {}

    public static PlantNamesSingleton getInstance() {
        if (instance == null) {
            instance = new PlantNamesSingleton();
        }
        return instance;
    }

    // Used when the user needs to retrieve all the plants they've added and associated plant details
    public ArrayList<Plant> getPlantList() { return plantList; }

    // Used when the user adds a new plant
    public void addPlant(Plant plant) { plantList.add(plant); }

    public void setPlantList(ArrayList<Plant> plantList) { this.plantList = plantList; }

//    // Used when the user needs to retrieve all the plants they have added
//    public ArrayList<String> getPlantNames() {
//        return plantNameList;
//    }
//
//    // Used when the user adds a new plant
//    public void addPlantName(String plantName) {
//        plantNameList.add(plantName);
//    }
//
//    // Should only be called once when the user signs in
//    public void setPlantNames(ArrayList<String> plantNames) {
//        this.plantNameList = plantNames;
//    }
}
