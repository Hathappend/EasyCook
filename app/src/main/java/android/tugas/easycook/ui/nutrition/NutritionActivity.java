package android.tugas.easycook.ui.nutrition;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.tugas.easycook.R;
import android.tugas.easycook.data.api.ApiClient;
import android.tugas.easycook.data.api.ApiService;
import android.tugas.easycook.data.model.Nutrient;
import android.tugas.easycook.data.response.NutritionResponse;
import android.tugas.easycook.databinding.ActivityNutritionBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NutritionActivity extends AppCompatActivity {

    private ActivityNutritionBinding binding;
    private ApiService apiService;
    private int recipeId = -1;
    private String recipeTitle = "";
    private String recipeImageUrl = "";
    private boolean isSelectingRecipe = false;
    private final String API_KEY = "9135788718664371a9de785a0ed83a7d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNutritionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();

        apiService = ApiClient.getClient().create(ApiService.class);

        // Ambil data dari Intent
        recipeId = getIntent().getIntExtra("RECIPE_ID", -1);
        recipeTitle = getIntent().getStringExtra("RECIPE_TITLE");
        recipeImageUrl = getIntent().getStringExtra("RECIPE_IMAGE_URL");
        isSelectingRecipe = getIntent().getBooleanExtra("IS_SELECTING_RECIPE", false);

        if (recipeId != -1) {
            binding.collapsingToolbarNutrition.setTitle(recipeTitle);

            // Atur warna judul agar sesuai
            binding.collapsingToolbarNutrition.setCollapsedTitleTextColor(getResources().getColor(android.R.color.white));
            binding.collapsingToolbarNutrition.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

            setupUI();
            fetchNutritionData();
        } else {
            Toast.makeText(this, "Recipe not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbarNutrition);
        if (getSupportActionBar() != null) {
            // Menampilkan tombol kembali (panah)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // Anda bisa mengganti judul toolbar secara dinamis jika mau
            getSupportActionBar().setTitle(recipeTitle != null ? recipeTitle : "Nutrition Details");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupUI() {
        // Tampilkan gambar
        Glide.with(this).load(recipeImageUrl).into(binding.ivRecipeImageNutrition);

        // Tampilkan tombol "Pilih" jika dalam mode memilih
        if (isSelectingRecipe) {
            binding.btnSelectFromNutrition.setVisibility(View.VISIBLE);
            binding.btnSelectFromNutrition.setOnClickListener(v -> selectThisRecipe());
        }
    }

    private void fetchNutritionData() {
        apiService.getNutritionWidget(recipeId, API_KEY).enqueue(new Callback<NutritionResponse>() {
            @Override
            public void onResponse(Call<NutritionResponse> call, Response<NutritionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    populateNutritionData(response.body());
                } else {
                    Toast.makeText(NutritionActivity.this, "Failed to load nutrition data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NutritionResponse> call, Throwable t) {
                Toast.makeText(NutritionActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateNutritionData(NutritionResponse data) {
        // Set kalori utama
        binding.tvCaloriesMain.setText(data.getCalories());

        // Proses dan tampilkan progress bar (good & bad nutrients)
        populateProgressGrid(data.getBad(), 3);
        populateProgressGrid(data.getGood(), 3);

        // Tampilkan daftar rincian nutrisi
        populateDetailsList(data.getBad());
        populateDetailsList(data.getGood());
    }

    private void populateProgressGrid(List<Nutrient> nutrients, int limit) {
        if (nutrients == null) return;
        for (int i = 0; i < Math.min(nutrients.size(), limit); i++) {
            Nutrient nutrient = nutrients.get(i);

            // Inflate layout custom untuk setiap item progress
            View progressItemView = LayoutInflater.from(this).inflate(R.layout.nutrition_item_progress, binding.gridNutritionProgress, false);

            TextView tvPercent = progressItemView.findViewById(R.id.tv_nutrient_percent);
            TextView tvLabel = progressItemView.findViewById(R.id.tv_nutrient_label);
            ProgressBar progressBar = progressItemView.findViewById(R.id.progress_bar_nutrient);

            tvPercent.setText(String.format("%.0f%%", nutrient.getPercentOfDailyNeeds()));
            tvLabel.setText(nutrient.getTitle());
            progressBar.setProgress((int) nutrient.getPercentOfDailyNeeds());

            binding.gridNutritionProgress.addView(progressItemView);
        }
    }

    private void populateDetailsList(List<Nutrient> nutrients) {
        if (nutrients == null) return;
        for (Nutrient nutrient : nutrients) {
            // Inflate layout custom untuk setiap baris detail
            View detailItemView = LayoutInflater.from(this).inflate(R.layout.nutrition_item_detail, binding.layoutNutritionDetails, false);

            TextView tvTitle = detailItemView.findViewById(R.id.tv_detail_title);
            TextView tvAmount = detailItemView.findViewById(R.id.tv_detail_amount);

            tvTitle.setText(nutrient.getTitle());
            tvAmount.setText(nutrient.getAmount());

            binding.layoutNutritionDetails.addView(detailItemView);
        }
    }

    private void selectThisRecipe() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("SELECTED_RECIPE_ID", recipeId);
        resultIntent.putExtra("SELECTED_RECIPE_TITLE", recipeTitle);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}