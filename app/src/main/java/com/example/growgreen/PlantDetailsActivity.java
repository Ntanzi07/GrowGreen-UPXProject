package com.example.growgreen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.growgreen.helpers.BottomMenuHelper;

public class PlantDetailsActivity extends AppCompatActivity {

    private ImageView imgPlant, btnBack;
    private TextView tvTitle, tvPlant, tvDescription, tvSymptoms, tvCauses, tvTreatment, tvPrevention;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_details);

        initializeViews();

        setupBackButton();

        BottomMenuHelper.setupBottomMenu(this);
        BottomMenuHelper.updateMenuColors(this);

        receiveAndDisplayData();
    }

    private void initializeViews() {
        imgPlant = findViewById(R.id.img);
        tvTitle = findViewById(R.id.tv_title);
        tvPlant = findViewById(R.id.tv_plant);
        tvDescription = findViewById(R.id.tv_description);
        tvSymptoms = findViewById(R.id.tv_symptoms);
        tvCauses = findViewById(R.id.tv_causes);
        tvTreatment = findViewById(R.id.tv_treatment);
        tvPrevention = findViewById(R.id.tv_prevention);
    }

    private void setupBackButton() {
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Volta para a MainActivity
                Intent intent = new Intent(PlantDetailsActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void receiveAndDisplayData() {
        try {
            // Recebe a imagem
            Uri imageUri = getIntent().getData();
            if (imageUri != null) {
                imgPlant.setImageURI(imageUri);
            }

            // Recebe os dados da API
            String plantName = getIntent().getStringExtra("plant_name");
            String diseaseName = getIntent().getStringExtra("disease_name");
            String description = getIntent().getStringExtra("description");
            String symptoms = getIntent().getStringExtra("symptoms");
            String causes = getIntent().getStringExtra("causes");
            String treatment = getIntent().getStringExtra("treatment");
            String prevention = getIntent().getStringExtra("prevention");

            // Exibe os dados
            tvTitle.setText(diseaseName != null ? diseaseName : "Doença Detectada");
            tvPlant.setText(plantName != null ? plantName : "Planta");
            tvDescription.setText(description != null ? description : "Descrição não disponível.");
            tvSymptoms.setText(symptoms != null ? symptoms : "Sintomas não identificados.");
            tvCauses.setText(causes != null ? causes : "Causas não identificadas.");
            tvTreatment.setText(treatment != null ? treatment : "Tratamento não disponível.");
            tvPrevention.setText(prevention != null ? prevention : "Prevenção não disponível.");

        } catch (Exception e) {
            e.printStackTrace();
            tvTitle.setText("Erro");
            tvPlant.setText("Ocorreu um erro ao carregar os dados");
        }
    }

    // Também pode usar o botão físico voltar
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}