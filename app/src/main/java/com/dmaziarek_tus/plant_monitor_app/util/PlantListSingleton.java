package com.dmaziarek_tus.plant_monitor_app.util;

import com.dmaziarek_tus.plant_monitor_app.model.Plant;

import java.util.ArrayList;

public class PlantListSingleton {
    private static PlantListSingleton instance = null;
    private ArrayList<Plant> plantList;

    private PlantListSingleton() {}

    public static PlantListSingleton getInstance() {
        if (instance == null) {
            instance = new PlantListSingleton();
        }
        return instance;
    }

    // Used when the user needs to retrieve all the plants they've added and associated plant details
    public ArrayList<Plant> getPlantList() { return plantList; }

    // Used when the user adds a new plant
    public void addPlant(Plant plant) { plantList.add(plant); }

    public void setPlantList(ArrayList<Plant> plantList) { this.plantList = plantList; }

    public void removePlant(String plantID) {
        for (int i = 0; i < plantList.size(); i++) {
            if (plantList.get(i).getPlantID().equals(plantID)) {
                plantList.remove(i);
            }
        }
    }

    public void emptyList() {
        plantList.clear();
    }

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
