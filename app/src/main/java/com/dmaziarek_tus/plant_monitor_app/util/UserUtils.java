package com.dmaziarek_tus.plant_monitor_app.util;

import android.view.View;
import android.widget.TextView;

import com.dmaziarek_tus.plant_monitor_app.R;
import com.dmaziarek_tus.plant_monitor_app.activity.DrawerBaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class UserUtils {

    public static String getDisplayNameFromFirebase() {
        String fbDisplayName = null;

        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fbUser != null) {
            fbDisplayName = fbUser.getDisplayName();
            // do something with the user's information
        }
        return fbDisplayName;
    }
}
