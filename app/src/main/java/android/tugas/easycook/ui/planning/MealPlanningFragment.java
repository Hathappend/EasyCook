package android.tugas.easycook.ui.planning;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.tugas.easycook.R;
import android.tugas.easycook.data.api.ApiClient;
import android.tugas.easycook.data.api.ApiService;
import android.tugas.easycook.data.response.DayPlan;
import android.tugas.easycook.data.response.Meal;
import android.tugas.easycook.data.response.MealPlanResponse;
import android.tugas.easycook.data.response.WeekPlan;
import android.tugas.easycook.databinding.FragmentMealPlanningBinding;
import android.tugas.easycook.ui.detail.RecipeDetailActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.tugas.easycook.data.db.AppDatabase;
import android.tugas.easycook.data.db.PlannedMeal;
import android.tugas.easycook.data.db.PlannedMealDao;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealPlanningFragment extends Fragment {

    private PlannedMealDao plannedMealDao;
    private ExecutorService executorService;
    private FragmentMealPlanningBinding binding;
    private ApiService apiService;
    private final String API_KEY = "9135788718664371a9de785a0ed83a7d";

    private final ActivityResultLauncher<Intent> addPlanningLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    int recipeId = result.getData().getIntExtra("MANUAL_RECIPE_ID", -1);
                    String recipeTitle = result.getData().getStringExtra("MANUAL_RECIPE_TITLE");
                    int dayIndex = result.getData().getIntExtra("MANUAL_DAY_INDEX", -1);
                    int mealIndex = result.getData().getIntExtra("MANUAL_MEAL_INDEX", -1);

                    if (recipeId != -1 && dayIndex != -1 && mealIndex != -1) {
                        // Simpan ke DB dan update UI
                        executorService.execute(() -> {
                            PlannedMeal newMeal = new PlannedMeal(recipeId, recipeTitle, dayIndex, mealIndex);
                            plannedMealDao.insertMeal(newMeal);
                        });
                        updateTableCell(dayIndex, mealIndex, recipeTitle, recipeId);
                        Toast.makeText(getContext(), "The plan has been successfully added!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inisialisasi Database & Executor
        AppDatabase db = AppDatabase.getDatabase(getContext());
        plannedMealDao = db.plannedMealDao();
        executorService = Executors.newSingleThreadExecutor();
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMealPlanningBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupDietTypeSpinner();
        createMealScheduleTable();
        loadSavedPlan();

        // Listener untuk "Beri Saya Ide" dengan dialog konfirmasi
        binding.btnGeneratePlan.setOnClickListener(v -> showGenerateConfirmationDialog());
        binding.btnClearPlan.setOnClickListener(v -> showClearAllConfirmationDialog());

        // Listener untuk "Add Planning"
        binding.btnAddPlanning.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddPlanningActivity.class);
            addPlanningLauncher.launch(intent);
        });
    }

    private void loadSavedPlan() {
        executorService.execute(() -> {
            List<PlannedMeal> savedMeals = plannedMealDao.getAllPlannedMeals();
            getActivity().runOnUiThread(() -> {
                for (PlannedMeal meal : savedMeals) {
                    updateTableCell(meal.dayColumnIndex, meal.mealRowIndex, meal.recipeTitle, meal.recipeId);
                }
            });
        });
    }

    private void updateTableCell(int dayColumnIndex, int mealRowIndex, String title, int recipeId) {
        TableRow row = (TableRow) binding.tableMealSchedule.getChildAt(mealRowIndex + 1);
        if (row != null) {
            TextView cell = (TextView) row.getChildAt(dayColumnIndex);
            if (cell != null) {
                cell.setText(title);
                cell.setTag(R.id.tag_recipe_id, recipeId);
                cell.setOnClickListener(v -> showCellOptionsDialog(cell, dayColumnIndex, mealRowIndex));
            }
        }
    }

    private void showClearAllConfirmationDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Clear All Planning")
                .setMessage("Are you sure you want to delete all meal plans?")
                .setPositiveButton("Ya, Delete", (dialog, which) -> {
                    executorService.execute(() -> {
                        plannedMealDao.clearAll();
                        getActivity().runOnUiThread(this::createMealScheduleTable);
                    });
                    Toast.makeText(getContext(), "The plan has been cleared", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void setupDietTypeSpinner() {
        List<String> dietTypes = new ArrayList<>();
        dietTypes.add("None");
        dietTypes.add("Gluten Free");
        dietTypes.add("Vegetarian");
        dietTypes.add("Vegan");
        dietTypes.add("Lacto-Vegetarian");
        dietTypes.add("Ketogenic");
        dietTypes.add("Ovo-Vegetarian");
        dietTypes.add("Pescetarian");
        dietTypes.add("Paleo");
        dietTypes.add("Primal");
        dietTypes.add("Low FODMAP");
        dietTypes.add("Whole30");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, dietTypes);
        binding.actvDietType.setAdapter(adapter);
    }

    private void createMealScheduleTable() {
        binding.tableMealSchedule.removeAllViews();

        String[] days = {"", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        String[] mealTimes = {"Morning", "Afternoon", "Night"};

        TableRow headerRow = new TableRow(getContext());
        for (String day : days) {
            headerRow.addView(createTableCell(day, true, -1, -1));
        }
        binding.tableMealSchedule.addView(headerRow);

        for (int i = 0; i < mealTimes.length; i++) {
            TableRow mealRow = new TableRow(getContext());
            mealRow.addView(createTableCell(mealTimes[i], true, -1, -1));
            for (int j = 1; j < days.length; j++) {
                TextView cell = createTableCell("", false, j, i);
                mealRow.addView(cell);
            }
            binding.tableMealSchedule.addView(mealRow);
        }
    }

    private TextView createTableCell(String text, boolean isHeader, int dayIndex, int rowIndex) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setPadding(16, 16, 16, 16);
        textView.setGravity(Gravity.CENTER);
        textView.setMinWidth(200);

        if (isHeader) {
            androidx.core.widget.TextViewCompat.setTextAppearance(textView, androidx.appcompat.R.style.TextAppearance_AppCompat_Body2);
        } else {
            textView.setBackgroundResource(R.drawable.cell_border);
            androidx.core.widget.TextViewCompat.setTextAppearance(textView, androidx.appcompat.R.style.TextAppearance_AppCompat_Caption);
        }
        return textView;
    }

    private void showGenerateConfirmationDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Generate New Plan")
                .setMessage("This will delete and replace the existing schedule. Continue?")
                .setPositiveButton("Ya, Continue", (dialog, which) -> generatePlanFromApi())
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void showCellOptionsDialog(TextView cell, int dayIndex, int rowIndex) {
        Integer recipeId = (Integer) cell.getTag(R.id.tag_recipe_id);
        if (recipeId == null) return;

        final CharSequence[] options = {"View Detail", "Change Recipe", "Remove from Schedule"};

        new AlertDialog.Builder(getContext())
                .setTitle("Recipe Options")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        openRecipeDetail(recipeId);
                    } else if (which == 1) {
                        Toast.makeText(getContext(), "The recipe swapping feature is coming soon!", Toast.LENGTH_SHORT).show();
                    } else if (which == 2) {
                        executorService.execute(() -> plannedMealDao.deleteMealAt(dayIndex, rowIndex));
                        cell.setText("");
                        cell.setTag(R.id.tag_recipe_id, null);
                        cell.setOnClickListener(null);
                        cell.setOnLongClickListener(null);
                    }
                })
                .show();
    }

    private void openRecipeDetail(int recipeId) {
        Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
        intent.putExtra("RECIPE_ID", recipeId);
        startActivity(intent);
    }

    private void generatePlanFromApi() {
        String targetCalories = binding.etDietGoal.getText().toString();
        String dietType = binding.actvDietType.getText().toString();

        if (targetCalories.isEmpty()) {
            Toast.makeText(getContext(), "Please enter your calorie target.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (binding.btnWeekly.isChecked()) {
            // Langsung panggil API untuk mingguan
            Toast.makeText(getContext(), "Creating weekly plan...", Toast.LENGTH_SHORT).show();
            createMealScheduleTable();
            callApiForWeeklyPlan(targetCalories, dietType);
        } else {
            // Tampilkan dialog pilihan hari untuk harian
            showDaySelectorDialog(targetCalories, dietType);
        }
    }
    private void showDaySelectorDialog(String targetCalories, String dietType) {
        final String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        new AlertDialog.Builder(getContext())
                .setTitle("Select Day")
                .setItems(days, (dialog, which) -> {
                    int dayColumnIndex = which + 1;
                    Toast.makeText(getContext(), "Creating plan for the day... " + days[which] + "...", Toast.LENGTH_SHORT).show();
                    callApiForDailyPlan(targetCalories, dietType, dayColumnIndex);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void callApiForWeeklyPlan(String targetCalories, String dietType) {
        apiService.generateMealPlan("week", targetCalories, dietType.equalsIgnoreCase("None") ? "" : dietType, API_KEY)
                .enqueue(new Callback<MealPlanResponse>() {
                    @Override
                    public void onResponse(Call<MealPlanResponse> call, Response<MealPlanResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            populateTableWithPlan(response.body());
                        } else {
                            Toast.makeText(getContext(), "Gagal membuat rencana mingguan: " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<MealPlanResponse> call, Throwable t) {
                        Toast.makeText(getContext(), "Error jaringan: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void callApiForDailyPlan(String targetCalories, String dietType, int dayColumnIndex) {
        apiService.generateDailyPlan("day", targetCalories, dietType.equalsIgnoreCase("None") ? "" : dietType, API_KEY)
                .enqueue(new Callback<DayPlan>() {
                    @Override
                    public void onResponse(Call<DayPlan> call, Response<DayPlan> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            clearTableColumn(dayColumnIndex);
                            updateDayPlan(response.body(), dayColumnIndex);
                        } else {
                            Toast.makeText(getContext(), "Gagal membuat rencana harian: " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<DayPlan> call, Throwable t) {
                        Toast.makeText(getContext(), "Error jaringan: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void clearTableColumn(int columnIndex) {

        for (int i = 1; i < binding.tableMealSchedule.getChildCount(); i++) {
            TableRow row = (TableRow) binding.tableMealSchedule.getChildAt(i);
            if(row != null && columnIndex < row.getChildCount()){
                TextView cell = (TextView) row.getChildAt(columnIndex);
                if (cell != null) {
                    cell.setText("");
                    cell.setTag(R.id.tag_recipe_id, null);
                    cell.setOnClickListener(null);
                    cell.setOnLongClickListener(null);
                }
            }
        }

        executorService.execute(() -> {
            for (int i = 0; i < 3; i++) {
                plannedMealDao.deleteMealAt(columnIndex, i);
            }
        });
    }

    private void populateTableWithPlan(MealPlanResponse mealPlan) {
        WeekPlan week = mealPlan.getWeek();
        if (week == null) return;

        updateDayPlan(week.getMonday(), 1);
        updateDayPlan(week.getTuesday(), 2);
        updateDayPlan(week.getWednesday(), 3);
        updateDayPlan(week.getThursday(), 4);
        updateDayPlan(week.getFriday(), 5);
        updateDayPlan(week.getSaturday(), 6);
        updateDayPlan(week.getSunday(), 7);
    }

    private void updateDayPlan(DayPlan dayPlan, int dayColumnIndex) {
        if (dayPlan == null || dayPlan.getMeals() == null) return;

        for (int i = 0; i < dayPlan.getMeals().size(); i++) {
            if (i < 3) {
                Meal meal = dayPlan.getMeals().get(i);
                final int mealRowIndex = i;

                // Simpan ke database
                executorService.execute(() -> {
                    PlannedMeal newMeal = new PlannedMeal(meal.getId(), meal.getTitle(), dayColumnIndex, mealRowIndex);
                    plannedMealDao.insertMeal(newMeal);
                });

                // Update UI di Main Thread
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> updateTableCell(dayColumnIndex, mealRowIndex, meal.getTitle(), meal.getId()));
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}