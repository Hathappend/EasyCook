package android.tugas.easycook.data.api;

import android.tugas.easycook.data.model.Recipe;
import android.tugas.easycook.data.response.DayPlan;
import android.tugas.easycook.data.response.MealPlanResponse;
import android.tugas.easycook.data.response.NutritionResponse;
import android.tugas.easycook.data.response.RecipeListResponse;
import android.tugas.easycook.data.response.SearchApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("recipes/complexSearch")
    Call<SearchApiResponse> searchRecipes(
            @Query("apiKey") String apiKey,
            @Query("query") String query,
            @Query("addRecipeInformation") boolean addRecipeInformation,
            @Query("number") int number,
            @Query("offset") int offset
    );

    @GET("recipes/{id}/information")
    Call<Recipe> getRecipeInformation(
            @Path("id") int id,
            @Query("apiKey") String apiKey
    );

    @GET("recipes/random")
    Call<RecipeListResponse> getRandomRecipes(
            @Query("number") int number,
            @Query("apiKey") String apiKey
    );

    @GET("recipes/complexSearch")
    Call<RecipeListResponse> searchPopularRecipes(
            @Query("sort") String sortBy,
            @Query("number") int number,
            @Query("addRecipeInformation") boolean addInfo,
            @Query("apiKey") String apiKey,
            @Query("offset") int offset
    );

    @GET("mealplanner/generate")
    Call<MealPlanResponse> generateMealPlan(
            @Query("timeFrame") String timeFrame,
            @Query("targetCalories") String targetCalories,
            @Query("diet") String diet,
            @Query("apiKey") String apiKey
    );

    @GET("mealplanner/generate")
    Call<DayPlan> generateDailyPlan(
            @Query("timeFrame") String timeFrame,
            @Query("targetCalories") String targetCalories,
            @Query("diet") String diet,
            @Query("apiKey") String apiKey
    );

    @GET("recipes/{id}/nutritionWidget.json")
    Call<NutritionResponse> getNutritionWidget(
            @Path("id") int id,
            @Query("apiKey") String apiKey
    );
}
