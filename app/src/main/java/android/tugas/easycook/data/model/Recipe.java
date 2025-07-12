package android.tugas.easycook.data.model;

import android.tugas.easycook.data.response.AnalyzedInstruction;
import android.tugas.easycook.data.response.ExtendedIngredient;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recipe {

    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("image")
    private String imageUrl;

    @SerializedName("readyInMinutes")
    private int readyInMinutes;

    @SerializedName("summary")
    private String summary;

    // FIELD BARU DITAMBAHKAN
    @SerializedName("spoonacularScore")
    private double spoonacularScore;

    @SerializedName("healthScore")
    private double healthScore;

    @SerializedName("extendedIngredients")
    private List<ExtendedIngredient> extendedIngredients;
    @SerializedName("analyzedInstructions")
    private List<AnalyzedInstruction> analyzedInstructions;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public String getSummary() {
        return summary;
    }

    public double getSpoonacularScore() {
        return spoonacularScore;
    }

    public double getHealthScore() {
        return healthScore;
    }

    public List<ExtendedIngredient> getExtendedIngredients() { return extendedIngredients; }
    public List<AnalyzedInstruction> getAnalyzedInstructions() { return analyzedInstructions; }
}
