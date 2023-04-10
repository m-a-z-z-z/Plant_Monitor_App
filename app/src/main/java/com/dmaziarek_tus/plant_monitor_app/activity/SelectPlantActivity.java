package com.dmaziarek_tus.plant_monitor_app.activity;

import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dmaziarek_tus.plant_monitor_app.R;
import com.dmaziarek_tus.plant_monitor_app.databinding.ActivitySelectPlantBinding;
import com.dmaziarek_tus.plant_monitor_app.model.Plant;
import com.dmaziarek_tus.plant_monitor_app.util.PlantNamesSingleton;
import com.dmaziarek_tus.plant_monitor_app.model.User;
import com.dmaziarek_tus.plant_monitor_app.util.PlantUtils;
import com.dmaziarek_tus.plant_monitor_app.util.UserUtils;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.ArrayList;

public class SelectPlantActivity extends DrawerBaseActivity {
    ActivitySelectPlantBinding binding;
    String userName;
    ArrayList<Plant> plantList = new ArrayList<>();
    TextView cardviewidText1, cardviewidText2, cardviewidText3, cardviewidText4, cardviewidText5,cardviewidText6,cardviewidText7,cardviewidText8,cardviewidText9,cardviewidText10;
    private StorageReference mStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectPlantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        allocateActivityTitle("Select Plant");

        // Gets hidden text with plant ID to pass to next activity
        // Support up to 10 cards/plants. Haven't found a way to do this dynamically yet
        cardviewidText1 = (TextView) findViewById(R.id.cardview_idText1);
        cardviewidText2 = (TextView) findViewById(R.id.cardview_idText2);
        cardviewidText3 = (TextView) findViewById(R.id.cardview_idText3);
        cardviewidText4 = (TextView) findViewById(R.id.cardview_idText4);
        cardviewidText5 = (TextView) findViewById(R.id.cardview_idText5);
        cardviewidText6 = (TextView) findViewById(R.id.cardview_idText6);
        cardviewidText7 = (TextView) findViewById(R.id.cardview_idText7);
        cardviewidText8 = (TextView) findViewById(R.id.cardview_idText8);
        cardviewidText9 = (TextView) findViewById(R.id.cardview_idText9);
        cardviewidText10 = (TextView) findViewById(R.id.cardview_idText10);

        userName = UserUtils.getDisplayNameFromFirebase();

        // Set the array list from the singleton
        plantList = PlantNamesSingleton.getInstance().getPlantList();
        Log.d("SelectPlantActivity", "onCreate - Plants: " + plantList);

        // I know this looks dumb but the app would crash every time if plantNameList == Null || plantNameList.isEmpty() was in one if statement
        if (plantList == null) {    // Shouldn't be necessary anymore but keeping it just in case
            PlantUtils.noPlantsAdded(this);
            Log.d("SelectPlantActivity", "onCreate - No plants added, launching add plant activity");
        } else if (plantList.isEmpty()) {
            PlantUtils.noPlantsAdded(this);
            Log.d("SelectPlantActivity", "onCreate - No plants added, launching add plant activity");
        } else {
            for (int i = 1; i < plantList.size()+1; i++) {
                String cardviewName = "card_view" + i;
                String textviewName = "cardview_text" + i;
                String textviewName2 = "cardview_idText" + i;
                String imageviewName = "img_plant" + i;
                int resID = getResources().getIdentifier(cardviewName, "id", getPackageName());
                int resID2 = getResources().getIdentifier(textviewName, "id", getPackageName());
                int resID3 = getResources().getIdentifier(textviewName2, "id", getPackageName());
                int resID4 = getResources().getIdentifier(imageviewName, "id", getPackageName());

                // Set the card to visible
                CardView cardView = (CardView) findViewById(resID);
                cardView.setVisibility(View.VISIBLE);

                // Set the text for each card
                TextView textView = (TextView) findViewById(resID2);
                textView.setText(plantList.get(i-1).getPlantName());

                TextView textView2 = (TextView) findViewById(resID3);
                textView2.setText(plantList.get(i-1).getPlantID());

                // Set the image for each card
                // Get Firebase storage for plant photos
                String photoFileName = plantList.get(i-1).getPhotoUrl();
                Log.d("SelectPlantActivity", "onCreate - Photo file name: " + photoFileName);
                mStorageReference = FirebaseStorage.getInstance().getReference().child("images/" + photoFileName);
                ImageView imageView = (ImageView) findViewById(resID4);
                Glide.get(this).getRegistry().append(StorageReference.class, InputStream.class, new FirebaseImageLoader.Factory());
                Glide.with(this)
                        .load(mStorageReference)
                        .into(imageView);
            }
        }
    }

    public void onPlantCardViewClicked(View view) {
        switch (view.getId()) {
            case R.id.card_view1:
                PlantUtils.plantSelected(this, cardviewidText1.getText().toString());
                break;
            case R.id.card_view2:
                PlantUtils.plantSelected(this, cardviewidText2.getText().toString());
                break;
            case R.id.card_view3:
                PlantUtils.plantSelected(this, cardviewidText3.getText().toString());
                break;
            case R.id.card_view4:
                PlantUtils.plantSelected(this, cardviewidText4.getText().toString());
                break;
            case R.id.card_view5:
                PlantUtils.plantSelected(this, cardviewidText5.getText().toString());
                break;
            case R.id.card_view6:
                PlantUtils.plantSelected(this, cardviewidText6.getText().toString());
                break;
            case R.id.card_view7:
                PlantUtils.plantSelected(this, cardviewidText7.getText().toString());
                break;
            case R.id.card_view8:
                PlantUtils.plantSelected(this, cardviewidText8.getText().toString());
                break;
            case R.id.card_view9:
                PlantUtils.plantSelected(this, cardviewidText9.getText().toString());
                break;
            case R.id.card_view10:
                PlantUtils.plantSelected(this, cardviewidText10.getText().toString());
                break;
        }
        finish();
    }
}