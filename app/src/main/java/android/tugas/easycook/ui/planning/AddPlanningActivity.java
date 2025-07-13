package android.tugas.easycook.ui.planning;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.tugas.easycook.databinding.ActivityAddPlanningBinding;
import android.tugas.easycook.ui.search.SearchActivity;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class AddPlanningActivity extends AppCompatActivity {

    private ActivityAddPlanningBinding binding;
    private int selectedRecipeId = -1;
    private String selectedRecipeTitle = "";

    private final ActivityResultLauncher<Intent> selectRecipeLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedRecipeId = result.getData().getIntExtra("SELECTED_RECIPE_ID", -1);
                    selectedRecipeTitle = result.getData().getStringExtra("SELECTED_RECIPE_TITLE");
                    if (selectedRecipeId != -1) {
                        binding.tvSelectedRecipe.setText(selectedRecipeTitle);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPlanningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupDropdowns();

        binding.btnSelectRecipe.setOnClickListener(v -> {
            // Buka SearchActivity untuk memilih resep
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra("IS_SELECTING_RECIPE", true);
            selectRecipeLauncher.launch(intent);
        });

        binding.btnSaveManualPlan.setOnClickListener(v -> savePlan());
    }

    private void setupDropdowns() {
        // Setup dropdown hari
        String[] days = {"Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"};
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, days);
        binding.actvSelectDay.setAdapter(dayAdapter);

        // Setup dropdown waktu makan
        String[] mealTimes = {"Pagi (Morning)", "Siang (Afternoon)", "Malam (Night)"};
        ArrayAdapter<String> mealTimeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, mealTimes);
        binding.actvSelectMealTime.setAdapter(mealTimeAdapter);
    }

    private void savePlan() {
        String selectedDay = binding.actvSelectDay.getText().toString();
        String selectedMealTime = binding.actvSelectMealTime.getText().toString();

        if (selectedDay.isEmpty() || selectedMealTime.isEmpty() || selectedRecipeId == -1) {
            Toast.makeText(this, "Harap lengkapi semua pilihan", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kirim data kembali ke MealPlanningFragment
        Intent resultIntent = new Intent();
        resultIntent.putExtra("MANUAL_RECIPE_ID", selectedRecipeId);
        resultIntent.putExtra("MANUAL_RECIPE_TITLE", selectedRecipeTitle);
        resultIntent.putExtra("MANUAL_DAY_INDEX", getDayIndex(selectedDay));
        resultIntent.putExtra("MANUAL_MEAL_INDEX", getMealIndex(selectedMealTime));
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private int getDayIndex(String day) {
        switch (day) {
            case "Senin": return 1;
            case "Selasa": return 2;
            case "Rabu": return 3;
            case "Kamis": return 4;
            case "Jumat": return 5;
            case "Sabtu": return 6;
            case "Minggu": return 7;
            default: return -1;
        }
    }

    private int getMealIndex(String mealTime) {
        if (mealTime.startsWith("Pagi")) return 0;
        if (mealTime.startsWith("Siang")) return 1;
        if (mealTime.startsWith("Malam")) return 2;
        return -1;
    }
}