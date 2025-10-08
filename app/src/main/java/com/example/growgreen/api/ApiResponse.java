package com.example.growgreen.api;

import java.util.List;

public class ApiResponse {
    private String prediction;
    private String plant;
    private String disease;
    private String description;
    private String severity;
    private List<String> symptoms;
    private String causes;
    private List<String> treatment;
    private List<String> prevention;

    // Getters (necessários pro Retrofit/Gson)
    public String getPrediction() {
        return prediction;
    }

    public String getPlant() {
        return plant;
    }

    public String getDisease() {
        return disease;
    }

    public String getDescription() {
        return description;
    }

    public String getSeverity() {
        return severity;
    }

    public List<String> getSymptoms() {
        return symptoms;
    }

    public String getCauses() {
        return causes;
    }

    public List<String> getTreatment() {
        return treatment;
    }

    public List<String> getPrevention() {
        return prevention;
    }
}
