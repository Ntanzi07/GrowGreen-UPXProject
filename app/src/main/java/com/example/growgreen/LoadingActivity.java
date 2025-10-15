package com.example.growgreen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.growgreen.api.ApiResponse;
import com.example.growgreen.api.PlantApiService;
import com.example.growgreen.api.RetrofitClient;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class LoadingActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView loadingText;
    private Handler handler = new Handler();
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        progressBar = findViewById(R.id.progressBar);
        loadingText = findViewById(R.id.loadingText);

        // Recebe a imagem da tela anterior
        selectedImageUri = getIntent().getData();

        if (selectedImageUri != null) {
            startImageUpload();
        } else {
            handleError("Nenhuma imagem selecionada");
        }
    }

    private void startImageUpload() {
        updateProgress(10, "Preparando imagem...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Simula preparação da imagem
                    Thread.sleep(1000);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            uploadImageToApi();
                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    handleError("Erro ao preparar imagem");
                }
            }
        }).start();
    }

    private void uploadImageToApi() {
        updateProgress(30, "Enviando imagem para análise...");

        try {
            // Copia a imagem para um arquivo temporário
            File file = new File(getCacheDir(), "upload.jpg");
            try (InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                 OutputStream outputStream = new FileOutputStream(file)) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
            }

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

            PlantApiService service = RetrofitClient.getApiService();
            Call<ApiResponse> call = service.uploadImage(body);

            updateProgress(60, "Analisando imagem...");

            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        updateProgress(90, "Processando resultados...");

                        ApiResponse apiResponse = response.body();

                        // Aguarda um pouco para mostrar o progresso e vai para os detalhes
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateProgress(100, "Análise concluída!");
                                goToPlantDetailsActivity(apiResponse, selectedImageUri);
                            }
                        }, 1500);

                    } else {
                        Log.e("API", "Erro na resposta: " + response.code());
                        handleError("Erro ao analisar imagem. Código: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Log.e("API", "Falha na requisição: ", t);
                    handleError("Falha na conexão: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            handleError("Erro ao processar imagem");
        }
    }

    private void goToPlantDetailsActivity(ApiResponse apiResponse, Uri imageUri) {
        Intent intent = new Intent(this, PlantDetailsActivity.class);
        intent.setData(imageUri);

        // Use os dados da API
        intent.putExtra("plant_name", apiResponse.getPlant());
        intent.putExtra("disease_name", apiResponse.getDisease());
        intent.putExtra("description", apiResponse.getDescription());
        intent.putExtra("symptoms", apiResponse.getSymptomsFormatted()); // Lista formatada
        intent.putExtra("causes", apiResponse.getCauses()); // String direto
        intent.putExtra("treatment", apiResponse.getTreatmentFormatted()); // Lista formatada
        intent.putExtra("prevention", apiResponse.getPreventionFormatted()); // Lista formatada

        // Log para debug
        Log.d("API_RESPONSE", "Planta: " + apiResponse.getPlant());
        Log.d("API_RESPONSE", "Doença: " + apiResponse.getDisease());
        Log.d("API_RESPONSE", "Sintomas: " + apiResponse.getSymptoms());
        Log.d("API_RESPONSE", "Causas: " + apiResponse.getCauses());

        startActivity(intent);
        finish();
    }

    private String formatListToString(java.util.List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (String item : list) {
            sb.append("• ").append(item).append("\n");
        }
        return sb.toString().trim();
    }

    private void updateProgress(int progress, String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(progress);
                loadingText.setText(message);
            }
        });
    }

    private void handleError(String errorMessage) {
        updateProgress(0, errorMessage);

        // Volta para a tela inicial após 3 segundos
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent backIntent = new Intent(LoadingActivity.this, MainActivity.class);
                startActivity(backIntent);
                finish();
            }
        }, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}