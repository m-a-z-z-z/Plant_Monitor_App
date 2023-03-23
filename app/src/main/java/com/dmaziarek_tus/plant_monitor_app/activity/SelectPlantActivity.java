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
    ArrayList<String> plantNameList = new ArrayList<>();
    TextView cardviewText1, cardviewText2, cardviewText3, cardviewText4, cardviewText5,cardviewText6,cardviewText7,cardviewText8,cardviewText9,cardviewText10;
    private StorageReference mStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectPlantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        allocateActivityTitle("Select Plant");

        // Support up to 10 cards/plants. Haven't found a way to do this dynamically yet
        cardviewText1 = (TextView) findViewById(R.id.cardview_text1);
        cardviewText2 = (TextView) findViewById(R.id.cardview_text2);
        cardviewText3 = (TextView) findViewById(R.id.cardview_text3);
        cardviewText4 = (TextView) findViewById(R.id.cardview_text4);
        cardviewText5 = (TextView) findViewById(R.id.cardview_text5);
        cardviewText6 = (TextView) findViewById(R.id.cardview_text6);
        cardviewText7 = (TextView) findViewById(R.id.cardview_text7);
        cardviewText8 = (TextView) findViewById(R.id.cardview_text8);
        cardviewText9 = (TextView) findViewById(R.id.cardview_text9);
        cardviewText10 = (TextView) findViewById(R.id.cardview_text10);

        userName = UserUtils.getDisplayNameFromFirebase();

        // Set the array list from the singleton
        plantNameList = PlantNamesSingleton.getInstance().getPlantNames();
        Log.d("SelectPlantActivity", "onCreate - Plant names: " + plantNameList);

        // I know this looks dumb but the app would crash every time if plantNameList == Null || plantNameList.isEmpty() was in one if statement
        if (plantNameList == null) {    // This should never be null after adding PlantUtils.retrievePlantNames() to splash screen, but just in case
            PlantUtils.noPlantsAdded(this);
            Log.d("SelectPlantActivity", "onCreate - No plants added, launching add plant activity");
        } else if (plantNameList.isEmpty()) {
            PlantUtils.noPlantsAdded(this);
            Log.d("SelectPlantActivity", "onCreate - No plants added, launching add plant activity");
        } else {
            for (int i = 1; i < plantNameList.size()+1; i++) {
                String cardviewName = "card_view" + i;
                String textviewName = "cardview_text" + i;
                String imageviewName = "img_plant" + i;
                int resID = getResources().getIdentifier(cardviewName, "id", getPackageName());
                int resID2 = getResources().getIdentifier(textviewName, "id", getPackageName());
                int resID3 = getResources().getIdentifier(imageviewName, "id", getPackageName());

                // Set the card to visible
                CardView cardView = (CardView) findViewById(resID);
                cardView.setVisibility(View.VISIBLE);

                // Set the text for each card
                TextView textView = (TextView) findViewById(resID2);
                textView.setText(plantNameList.get(i-1));

                // Set the image for each card
                // Get Firebase storage for plant photos
                String photoFileName = plantNameList.get(i-1) + "_" + userName + ".jpg";
                Log.d("SelectPlantActivity", "onCreate - Photo file name: " + photoFileName);
                mStorageReference = FirebaseStorage.getInstance().getReference().child("images/" + photoFileName);
                ImageView imageView = (ImageView) findViewById(resID3);
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
                PlantUtils.plantSelected(this, cardviewText1.getText().toString());
                break;
            case R.id.card_view2:
                PlantUtils.plantSelected(this, cardviewText2.getText().toString());
                break;
            case R.id.card_view3:
                PlantUtils.plantSelected(this, cardviewText3.getText().toString());
                break;
            case R.id.card_view4:
                PlantUtils.plantSelected(this, cardviewText4.getText().toString());
                break;
            case R.id.card_view5:
                PlantUtils.plantSelected(this, cardviewText5.getText().toString());
                break;
            case R.id.card_view6:
                PlantUtils.plantSelected(this, cardviewText6.getText().toString());
                break;
            case R.id.card_view7:
                PlantUtils.plantSelected(this, cardviewText7.getText().toString());
                break;
            case R.id.card_view8:
                PlantUtils.plantSelected(this, cardviewText8.getText().toString());
                break;
            case R.id.card_view9:
                PlantUtils.plantSelected(this, cardviewText9.getText().toString());
                break;
            case R.id.card_view10:
                PlantUtils.plantSelected(this, cardviewText10.getText().toString());
                break;
        }
        finish();
    }
}