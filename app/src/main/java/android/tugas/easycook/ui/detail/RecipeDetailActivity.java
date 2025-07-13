package android.tugas.easycook.ui.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.tugas.easycook.databinding.ActivityRecipeDetailBinding;
import android.view.View;
import android.widget.Toast;
import android.text.Html;
import android.tugas.easycook.data.api.ApiClient;
import android.tugas.easycook.data.api.ApiService;
import android.tugas.easycook.data.model.Recipe;
import android.tugas.easycook.data.response.ExtendedIngredient;
import android.tugas.easycook.data.response.Step;

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
    private final String API_KEY = "9135788718664371a9de785a0ed83a7d";
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
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        binding.toolbarLayout.setTitle("Recipe Details");
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
        Toast.makeText(this, "Membuka halaman cek nutrisi...", Toast.LENGTH_SHORT).show();
        // TODO: Ganti Toast ini dengan Intent ke NutritionActivity Anda nanti
        // Intent intent = new Intent(this, NutritionActivity.class);
        // intent.putExtra("RECIPE_ID", currentRecipe.getId());
        // startActivity(intent);
    }

    private void setupButtonListeners() {
        // Listener untuk tombol FAB default
        binding.fabCheckNutrition.setOnClickListener(v -> openNutritionPage());

        // Listener untuk tombol di dalam grup planning
        binding.btnCheckNutritionPlanning.setOnClickListener(v -> openNutritionPage());

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
