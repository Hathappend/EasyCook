package android.tugas.easycook.ui.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.tugas.easycook.BuildConfig;
import android.tugas.easycook.databinding.ActivityRecipeDetailBinding;
import android.tugas.easycook.ui.nutrition.NutritionActivity;
import android.view.View;
import android.widget.Toast;
import android.text.Html;
import android.tugas.easycook.data.api.ApiClient;
import android.tugas.easycook.data.api.ApiService;
import android.tugas.easycook.data.model.Recipe;
import android.tugas.easycook.data.response.ExtendedIngredient;
import android.tugas.easycook.data.response.Step;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayoutMediator;
import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    private ActivityRecipeDetailBinding binding;
    public static final String RECIPE_ID = "RECIPE_ID";
    private ApiService apiService;
    private final String API_KEY = BuildConfig.API_KEY;
    private static final int NUTRITION_REQUEST_CODE = 101;
    private boolean isDescriptionExpanded = false;
    private boolean isSelectingRecipe = false;
    private Recipe currentRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        apiService = ApiClient.getClient().create(ApiService.class);

        int recipeId = getIntent().getIntExtra(RECIPE_ID, -1);
        isSelectingRecipe = getIntent().getBooleanExtra("IS_SELECTING_RECIPE", false);

        if (recipeId != -1) {
            fetchRecipeDetails(recipeId);
        } else {
            Toast.makeText(this, "Recipe not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        setupToolbar();

    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void fetchRecipeDetails(int recipeId) {
        apiService.getRecipeInformation(recipeId, API_KEY).enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentRecipe = response.body();
                    Recipe data = response.body();

                    binding.toolbarLayout.setTitle(data.getTitle());

                    Glide.with(RecipeDetailActivity.this).load(data.getImageUrl()).into(binding.ivRecipeImageDetail);
                    binding.tvRecipeNameDetail.setText(data.getTitle());

                    double rating = data.getSpoonacularScore() / 20.0;
                    binding.tvRatingDetail.setText(String.format(java.util.Locale.US, "%.1f", rating));

                    binding.tvTimeDetail.setText(data.getReadyInMinutes() + " minutes");

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        binding.tvDescriptionDetail.setText(Html.fromHtml(data.getSummary(), Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        binding.tvDescriptionDetail.setText(Html.fromHtml(data.getSummary()));
                    }
                    binding.tvDescriptionDetail.setLinkTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                    binding.tvDescriptionDetail.setMovementMethod(LinkMovementMethod.getInstance());

                    setupExpandableDescription();

                    List<ExtendedIngredient> ingredients = data.getExtendedIngredients();
                    // tidak kosong sebelum mengambil steps
                    if (data.getAnalyzedInstructions() != null && !data.getAnalyzedInstructions().isEmpty()) {
                        List<Step> steps = data.getAnalyzedInstructions().get(0).getSteps();
                        setupTabs(ingredients, steps);
                    } else {
                        setupTabs(ingredients, new java.util.ArrayList<>());
                    }

                    if (isSelectingRecipe) {
                        // Mode Memilih: Tampilkan grup tombol planning
                        binding.fabCheckNutrition.setVisibility(View.GONE);
                        binding.planningActionLayout.setVisibility(View.VISIBLE);
                    } else {
                        // Mode Normal: Tampilkan tombol FAB biasa
                        binding.fabCheckNutrition.setVisibility(View.VISIBLE);
                        binding.planningActionLayout.setVisibility(View.GONE);
                    }

                    setupButtonListeners();

                } else {
                    Toast.makeText(RecipeDetailActivity.this, "Failed to load details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                Toast.makeText(RecipeDetailActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openNutritionPage() {
        if (currentRecipe != null) {
            Intent intent = new Intent(this, NutritionActivity.class);
            intent.putExtra("RECIPE_ID", currentRecipe.getId());
            intent.putExtra("RECIPE_TITLE", currentRecipe.getTitle());
            intent.putExtra("RECIPE_IMAGE_URL", currentRecipe.getImageUrl());
            intent.putExtra("IS_SELECTING_RECIPE", isSelectingRecipe);

            startActivityForResult(intent, NUTRITION_REQUEST_CODE);
        } else {
            Toast.makeText(this, "Please wait while the recipe data is loading", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Periksa apakah ini adalah hasil dari NutritionActivity dan hasilnya OK
        if (requestCode == NUTRITION_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Jika ya, maka kita teruskan hasilnya dan tutup halaman detail ini juga
            if (data != null) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("SELECTED_RECIPE_ID", data.getIntExtra("SELECTED_RECIPE_ID", -1));
                resultIntent.putExtra("SELECTED_RECIPE_TITLE", data.getStringExtra("SELECTED_RECIPE_TITLE"));
                setResult(Activity.RESULT_OK, resultIntent);
            }
            finish();
        }
    }

    private void setupButtonListeners() {
        // Listener untuk tombol FAB default
        binding.fabCheckNutrition.setOnClickListener(v -> {
            openNutritionPage();
        });

        // Listener untuk tombol "Cek Nutrisi" di dalam grup planning
        binding.btnCheckNutritionPlanning.setOnClickListener(v -> {
            openNutritionPage();
        });

        // Listener untuk tombol "Add Planning"
        binding.btnAddToPlan.setOnClickListener(v -> {
            selectThisRecipe();
        });
    }

    private void selectThisRecipe() {
        if (currentRecipe != null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("SELECTED_RECIPE_ID", currentRecipe.getId());
            resultIntent.putExtra("SELECTED_RECIPE_TITLE", currentRecipe.getTitle());
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }
    private void setupExpandableDescription() {
        binding.tvReadMore.setOnClickListener(v -> {
            isDescriptionExpanded = !isDescriptionExpanded;
            if (isDescriptionExpanded) {
                // Perluas deskripsi
                binding.tvDescriptionDetail.setMaxLines(Integer.MAX_VALUE);
                binding.tvDescriptionDetail.setEllipsize(null);
                binding.tvReadMore.setText("Less");
            } else {
                // Ciutkan deskripsi
                binding.tvDescriptionDetail.setMaxLines(3);
                binding.tvDescriptionDetail.setEllipsize(android.text.TextUtils.TruncateAt.END);
                binding.tvReadMore.setText("Read more");
            }

        });
    }

    private void setupTabs(List<ExtendedIngredient> ingredients, List<Step> steps) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);

        // Kirim data ke adapter
        viewPagerAdapter.setIngredients(ingredients);
        viewPagerAdapter.setInstructions(steps);

        binding.viewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> {
                    if (position == 0) {
                        tab.setText("Ingredients");
                    } else {
                        tab.setText("Instructions");
                    }
                }
        ).attach();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
