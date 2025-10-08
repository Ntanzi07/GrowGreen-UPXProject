package com.example.growgreen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.growgreen.api.ApiResponse;
import com.example.growgreen.api.PlantApiService;
import com.example.growgreen.api.RetrofitClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView txtResult;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 ou superior
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 1);
            }
        } else {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }

        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        txtResult = findViewById(R.id.txtResult);
        Button btnSelectImage = findViewById(R.id.btnSelectImage);

        btnSelectImage.setOnClickListener(v -> openGallery());

    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    imageView.setImageURI(selectedImageUri);
                    uploadImage(selectedImageUri);
                }
            });

    private void uploadImage(Uri imageUri) {
        try {
            // Copia a imagem escolhida para um arquivo temporário
            File file = new File(getCacheDir(), "upload.jpg");
            try (InputStream inputStream = getContentResolver().openInputStream(imageUri);
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

            txtResult.setText("Analisando imagem...");

            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse apiResponse = response.body();

                        StringBuilder result = new StringBuilder();
                        result.append("🌿 Planta: ").append(apiResponse.getPlant()).append("\n\n");
                        result.append("🦠 Doença: ").append(apiResponse.getDisease()).append("\n");
                        result.append("📊 Severidade: ").append(apiResponse.getSeverity()).append("\n\n");
                        result.append("📖 Descrição:\n").append(apiResponse.getDescription()).append("\n\n");

                        if (apiResponse.getSymptoms() != null && !apiResponse.getSymptoms().isEmpty()) {
                            result.append("⚠️ Sintomas:\n");
                            for (String s : apiResponse.getSymptoms()) {
                                result.append("• ").append(s).append("\n");
                            }
                            result.append("\n");
                        }

                        if (apiResponse.getTreatment() != null && !apiResponse.getTreatment().isEmpty()) {
                            result.append("💊 Tratamento:\n");
                            for (String t : apiResponse.getTreatment()) {
                                result.append("• ").append(t).append("\n");
                            }
                            result.append("\n");
                        }

                        if (apiResponse.getPrevention() != null && !apiResponse.getPrevention().isEmpty()) {
                            result.append("🛡️ Prevenção:\n");
                            for (String p : apiResponse.getPrevention()) {
                                result.append("• ").append(p).append("\n");
                            }
                        }

                        txtResult.setText(result.toString());
                    } else {
                        txtResult.setText("Erro ao analisar imagem.");
                        Log.e("API", "Erro: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    txtResult.setText("Falha na conexão: " + t.getMessage());
                    Log.e("API", "Falha: ", t);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao processar imagem", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissão concedida!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permissão negada. Não será possível selecionar imagens.", Toast.LENGTH_LONG).show();
            }
        }
    }

}