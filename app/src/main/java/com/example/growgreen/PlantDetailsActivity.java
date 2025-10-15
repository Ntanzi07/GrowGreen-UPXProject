package com.example.growgreen;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PlantDetailsActivity extends AppCompatActivity {

    private ImageView imgPlant;
    private TextView tvTitle, tvPlant, tvDescription, tvSymptoms, tvCauses, tvTreatment, tvPrevention;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_details); // Seu layout XML

        // Inicializar views
        initializeViews();

        // Receber e exibir os dados
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

    private String formatWithBullets(String text) {
        if (text == null || text.isEmpty()) {
            return "Informação não disponível";
        }

        // Se já tem bullets, mantém como está
        if (text.contains("•") || text.contains("-")) {
            return text;
        }

        // Se não tem bullets, adiciona
        // Assume que cada linha é separada por \n
        String[] lines = text.split("\n");
        StringBuilder formatted = new StringBuilder();

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                formatted.append("• ").append(line.trim()).append("\n");
            }
        }

        return formatted.toString().trim();
    }

    private String getSafeText(String text, String defaultText) {
        return (text != null && !text.isEmpty()) ? text : defaultText;
    }

}