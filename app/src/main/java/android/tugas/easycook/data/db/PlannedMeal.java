package android.tugas.easycook.data.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "planned_meals")
public class PlannedMeal {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    public int recipeId;
    public String recipeTitle;
    public int dayColumnIndex;
    public int mealRowIndex;
    public PlannedMeal(int recipeId, String recipeTitle, int dayColumnIndex, int mealRowIndex) {
        this.recipeId = recipeId;
        this.recipeTitle = recipeTitle;
        this.dayColumnIndex = dayColumnIndex;
        this.mealRowIndex = mealRowIndex;
    }
}