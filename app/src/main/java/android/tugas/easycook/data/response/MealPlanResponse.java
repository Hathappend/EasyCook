package android.tugas.easycook.data.response;

import com.google.gson.annotations.SerializedName;

public class MealPlanResponse {
    @SerializedName("week")
    private WeekPlan week;

    public WeekPlan getWeek() {
        return week;
    }
}