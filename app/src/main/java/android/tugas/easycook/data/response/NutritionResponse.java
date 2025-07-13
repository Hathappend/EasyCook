package android.tugas.easycook.data.response;

import android.tugas.easycook.data.model.Nutrient;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class NutritionResponse {
    @SerializedName("calories")
    private String calories;

    @SerializedName("carbs")
    private String carbs;

    @SerializedName("fat")
    private String fat;

    @SerializedName("protein")
    private String protein;

    @SerializedName("good")
    private List<Nutrient> good;

    @SerializedName("bad")
    private List<Nutrient> bad;

    // Getter
    public String getCalories() { return calories; }
    public String getCarbs() { return carbs; }
    public String getFat() { return fat; }
    public String getProtein() { return protein; }
    public List<Nutrient> getGood() { return good; }
    public List<Nutrient> getBad() { return bad; }
}