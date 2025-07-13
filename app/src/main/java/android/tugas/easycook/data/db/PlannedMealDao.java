package android.tugas.easycook.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface PlannedMealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMeal(PlannedMeal meal);

    @Query("SELECT * FROM planned_meals")
    List<PlannedMeal> getAllPlannedMeals();

    @Query("DELETE FROM planned_meals WHERE dayColumnIndex = :dayIndex AND mealRowIndex = :rowIndex")
    void deleteMealAt(int dayIndex, int rowIndex);

    @Query("DELETE FROM planned_meals")
    void clearAll();
}