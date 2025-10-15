package com.example.growgreen.helpers;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.example.growgreen.AboutUsActivity;
import com.example.growgreen.MainActivity;
import com.example.growgreen.R;

public class BottomMenuHelper {

    public static void setupBottomMenu(Activity activity) {
        ImageView btnHome = activity.findViewById(R.id.btnHome);
        ImageView btnInfo = activity.findViewById(R.id.btnInfo);

        if (btnHome != null) {
            btnHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Se já está na MainActivity, não faz nada
                    if (!(activity instanceof MainActivity)) {
                        Intent intent = new Intent(activity, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        activity.startActivity(intent);
                        activity.finish();
                    }
                }
            });
        }

        if (btnInfo != null) {
            btnInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Se já está na AboutUsActivity, não faz nada
                    if (!(activity instanceof AboutUsActivity)) {
                        Intent intent = new Intent(activity, AboutUsActivity.class);
                        activity.startActivity(intent);
                    }
                }
            });
        }
    }

    public static void updateMenuColors(Activity activity) {
        ImageView btnHome = activity.findViewById(R.id.btnHome);
        ImageView btnInfo = activity.findViewById(R.id.btnInfo);

        if (btnHome != null && btnInfo != null) {
            if (activity instanceof MainActivity) {
                // Na MainActivity: home verde, info cinza
                btnHome.setColorFilter(activity.getResources().getColor(android.R.color.holo_green_dark));
                btnInfo.setColorFilter(activity.getResources().getColor(android.R.color.darker_gray));
            } else if (activity instanceof AboutUsActivity) {
                // Na AboutUsActivity: home cinza, info verde
                btnHome.setColorFilter(activity.getResources().getColor(android.R.color.darker_gray));
                btnInfo.setColorFilter(activity.getResources().getColor(android.R.color.holo_green_dark));
            } else {
                // Em outras activities: home verde, info cinza
                btnHome.setColorFilter(activity.getResources().getColor(android.R.color.holo_green_dark));
                btnInfo.setColorFilter(activity.getResources().getColor(android.R.color.darker_gray));
            }
        }
    }
}