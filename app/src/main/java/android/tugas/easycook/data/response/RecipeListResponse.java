package android.tugas.easycook.data.response;

import android.tugas.easycook.data.model.Recipe;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RecipeListResponse {

    @SerializedName(value="recipes", alternate={"results"})
    private List<Recipe> recipes;

    public List<Recipe> getRecipes() {
        return recipes;
    }
}