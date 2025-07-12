package android.tugas.easycook.data.response;

import android.tugas.easycook.data.model.Recipe;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SearchApiResponse {

    @SerializedName("results")
    private List<Recipe> results;

    @SerializedName("totalResults")
    private int totalResults;

    public List<Recipe> getResults() {
        return results;
    }

    public int getTotalResults() {
        return totalResults;
    }
}
