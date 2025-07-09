package android.tugas.easycook.ui.detail;

import android.os.Bundle;
import android.tugas.easycook.R;
import android.tugas.easycook.databinding.ActivityRecipeDetailBinding;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.tabs.TabLayoutMediator;

public class RecipeDetailActivity extends AppCompatActivity {

    private ActivityRecipeDetailBinding binding;
    public static final String RECIPE_ID = "RECIPE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int recipeId = getIntent().getIntExtra(RECIPE_ID, -1);

        if (recipeId != -1) {
            Toast.makeText(this, "Received Recipe ID: " + recipeId, Toast.LENGTH_SHORT).show();
            loadDummyData();
        } else {
            Toast.makeText(this, "Recipe not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        setupToolbar();
        setupTabs();
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

    private void loadDummyData() {
        binding.ivRecipeImageDetail.setImageResource(R.drawable.ic_noodles);
        binding.tvRecipeNameDetail.setText("Miso Ramen");
        binding.tvRatingDetail.setText("4.8");
        binding.tvTimeDetail.setText("10 minutes");
        binding.tvDescriptionDetail.setText("Miso Ramen features a rich, slightly creamy broth made with fermented soybean paste (miso). It's savory, hearty, and often served with toppings like sweet corn, bean sprouts, ground meat, and butter.");
    }

    private void setupTabs() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
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
