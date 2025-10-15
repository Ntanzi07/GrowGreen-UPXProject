package com.example.growgreen.api;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ApiResponse implements Parcelable {

    @SerializedName("prediction")
    private String prediction;

    @SerializedName("plant")
    private String plant;

    @SerializedName("disease")
    private String disease;

    @SerializedName("description")
    private String description;

    @SerializedName("severity")
    private String severity;

    @SerializedName("symptoms")
    private List<String> symptoms;

    @SerializedName("causes")
    private String causes; // ← AGORA É String, não List!

    @SerializedName("treatment")
    private List<String> treatment;

    @SerializedName("prevention")
    private List<String> prevention;

    // Construtor vazio
    public ApiResponse() {}

    // Construtor Parcelable
    protected ApiResponse(Parcel in) {
        prediction = in.readString();
        plant = in.readString();
        disease = in.readString();
        description = in.readString();
        severity = in.readString();
        symptoms = in.createStringArrayList();
        causes = in.readString(); // ← AGORA É readString()
        treatment = in.createStringArrayList();
        prevention = in.createStringArrayList();
    }

    // Getters
    public String getPrediction() {
        return prediction != null ? prediction : "";
    }

    public String getPlant() {
        return plant != null ? plant : "Planta não identificada";
    }

    public String getDisease() {
        return disease != null ? disease : "Doença não identificada";
    }

    public String getDescription() {
        return description != null ? description : "Descrição não disponível.";
    }

    public String getSeverity() {
        return severity != null ? severity : "Não especificada";
    }

    public List<String> getSymptoms() {
        return symptoms;
    }

    public String getCauses() {
        return causes != null ? causes : "Causas não identificadas";
    }

    public List<String> getTreatment() {
        return treatment;
    }

    public List<String> getPrevention() {
        return prevention;
    }

    // Setters
    public void setPrediction(String prediction) { this.prediction = prediction; }
    public void setPlant(String plant) { this.plant = plant; }
    public void setDisease(String disease) { this.disease = disease; }
    public void setDescription(String description) { this.description = description; }
    public void setSeverity(String severity) { this.severity = severity; }
    public void setSymptoms(List<String> symptoms) { this.symptoms = symptoms; }
    public void setCauses(String causes) { this.causes = causes; }
    public void setTreatment(List<String> treatment) { this.treatment = treatment; }
    public void setPrevention(List<String> prevention) { this.prevention = prevention; }

    // Métodos para formatar como string (para a tela)
    public String getSymptomsFormatted() {
        return formatList(symptoms);
    }

    public String getTreatmentFormatted() {
        return formatList(treatment);
    }

    public String getPreventionFormatted() {
        return formatList(prevention);
    }

    private String formatList(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "Informação não disponível";
        }

        StringBuilder sb = new StringBuilder();
        for (String item : list) {
            sb.append("• ").append(item).append("\n");
        }
        return sb.toString().trim();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(prediction);
        dest.writeString(plant);
        dest.writeString(disease);
        dest.writeString(description);
        dest.writeString(severity);
        dest.writeStringList(symptoms);
        dest.writeString(causes); // ← AGORA É writeString()
        dest.writeStringList(treatment);
        dest.writeStringList(prevention);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ApiResponse> CREATOR = new Creator<ApiResponse>() {
        @Override
        public ApiResponse createFromParcel(Parcel in) {
            return new ApiResponse(in);
        }

        @Override
        public ApiResponse[] newArray(int size) {
            return new ApiResponse[size];
        }
    };
}