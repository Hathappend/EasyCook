package android.tugas.easycook.data.api;

import android.tugas.easycook.data.response.RecipeListResponse;
import android.tugas.easycook.data.response.RecipeInformationResponse;
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
    Call<RecipeInformationResponse> getRecipeInformation(
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
}
