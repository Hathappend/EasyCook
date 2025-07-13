package android.tugas.easycook.data.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DayPlan {
    @SerializedName("meals")
    private List<Meal> meals;

    public List<Meal> getMeals() {
        return meals;
    }
}