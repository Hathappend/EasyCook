package android.tugas.easycook.data.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RecipeInformationResponse {
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
    @SerializedName("extendedIngredients")
    private List<ExtendedIngredient> extendedIngredients;
    @SerializedName("analyzedInstructions")
    private List<AnalyzedInstruction> analyzedInstructions;

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getImageUrl() { return imageUrl; }
    public int getReadyInMinutes() { return readyInMinutes; }
    public String getSummary() { return summary; }
    public List<ExtendedIngredient> getExtendedIngredients() { return extendedIngredients; }
    public List<AnalyzedInstruction> getAnalyzedInstructions() { return analyzedInstructions; }
}