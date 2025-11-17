package com.example.growgreen.helpers;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import androidx.core.content.ContextCompat;

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

        // Atualiza as cores baseado na tela atual
        updateMenuColors(activity);
    }

    public static void updateMenuColors(Activity activity) {
        ImageView btnHome = activity.findViewById(R.id.btnHome);
        ImageView btnInfo = activity.findViewById(R.id.btnInfo);

        if (btnHome != null && btnInfo != null) {
            // Cores
            int selectedColor = 0xFF6BBE4B;  // Verde #6BBE4B
            int unselectedColor = 0xFFC0C0C0; // Cinza #C0C0C0

            if (activity instanceof MainActivity) {
                // Na MainActivity: home verde (selecionado), info cinza
                btnHome.setColorFilter(selectedColor);
                btnInfo.setColorFilter(unselectedColor);
            } else if (activity instanceof AboutUsActivity) {
                // Na AboutUsActivity: home cinza, info verde (selecionado)
                btnHome.setColorFilter(unselectedColor);
                btnInfo.setColorFilter(selectedColor);
            } else {
                // Em outras activities (PlantDetails, Loading): home verde, info cinza
                btnHome.setColorFilter(selectedColor);
                btnInfo.setColorFilter(unselectedColor);
            }
        }
    }
}