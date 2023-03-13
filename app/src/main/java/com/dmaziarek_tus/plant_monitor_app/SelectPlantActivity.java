package com.dmaziarek_tus.plant_monitor_app;

import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dmaziarek_tus.plant_monitor_app.databinding.ActivitySelectPlantBinding;
import com.dmaziarek_tus.plant_monitor_app.model.PlantNamesSingleton;
import com.dmaziarek_tus.plant_monitor_app.model.User;

import java.util.ArrayList;

public class SelectPlantActivity extends DrawerBaseActivity {
    ActivitySelectPlantBinding binding;
    String userName;
    ArrayList<String> plantNames = new ArrayList<>();
    CardView cardView1, cardview2, cardview3, cardView4;
    TextView cardviewText1, cardviewText2, cardviewText3, cardviewText4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectPlantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        allocateActivityTitle("Select Plant");

        cardView1 = (CardView) findViewById(R.id.card_view1);
        cardview2 = (CardView) findViewById(R.id.card_view2);
        cardview3 = (CardView) findViewById(R.id.card_view3);
        cardView4 = (CardView) findViewById(R.id.card_view4);
        cardviewText1 = (TextView) findViewById(R.id.cardview_text1);
        cardviewText2 = (TextView) findViewById(R.id.cardview_text2);
        cardviewText3 = (TextView) findViewById(R.id.cardview_text3);
        cardviewText4 = (TextView) findViewById(R.id.cardview_text4);

        User user = new User();
        userName = user.getUserName().trim();

        // Set the array list from the singleton
        plantNames = PlantNamesSingleton.getInstance().getPlantNames();
        Log.d("SelectPlantActivity", "onCreate - Plant names: " + plantNames);

        for (int i = 1; i < plantNames.size()+1; i++) {
            String cardviewName = "card_view" + i;
            String textviewName = "cardview_text" + i;
            int resID = getResources().getIdentifier(cardviewName, "id", getPackageName());
            int resID2 = getResources().getIdentifier(textviewName, "id", getPackageName());

            CardView cardView = (CardView) findViewById(resID);
            cardView.setVisibility(View.VISIBLE);
            TextView textView = (TextView) findViewById(resID2);
            textView.setText(plantNames.get(i-1));
        }
    }

    public void cardView1Clicked(View view) {
        String plantName = cardviewText1.getText().toString();
        Log.d("SelectPlantActivity", "cardView1Clicked - Plant name: " + plantName);
        Intent intent = new Intent(this, PlantHealthActivity.class);
        intent.putExtra("plantName", plantName);
        startActivity(intent);
    }

    public void cardView2Clicked(View view) {
        String plantName = cardviewText2.getText().toString();
        Log.d("SelectPlantActivity", "cardView2Clicked - Plant name: " + plantName);
        Intent intent = new Intent(this, PlantHealthActivity.class);
        intent.putExtra("plantName", plantName);
        startActivity(intent);
    }

    public void cardView3Clicked(View view) {
        String plantName = cardviewText3.getText().toString();
        Log.d("SelectPlantActivity", "cardView3Clicked - Plant name: " + plantName);
        Intent intent = new Intent(this, PlantHealthActivity.class);
        intent.putExtra("plantName", plantName);
        startActivity(intent);
    }

    public void cardView4Clicked(View view) {
        String plantName = cardviewText4.getText().toString();
        Log.d("SelectPlantActivity", "cardView4Clicked - Plant name: " + plantName);
        Intent intent = new Intent(this, PlantHealthActivity.class);
        intent.putExtra("plantName", plantName);
        startActivity(intent);
    }
}