package android.tugas.easycook.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.tugas.easycook.R;
import android.tugas.easycook.data.model.Recipe;
import android.tugas.easycook.databinding.FragmentHomeBinding;
import android.tugas.easycook.ui.detail.RecipeDetailActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.tugas.easycook.data.api.ApiClient;
import android.tugas.easycook.data.api.ApiService;
import android.tugas.easycook.data.response.RandomRecipeResponse;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecommendedAdapter recommendedAdapter;

    private final String API_KEY = "9135788718664371a9de785a0ed83a7d";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecommendedRecyclerView();
        fetchRecommendedRecipes();

        setupPopularRecyclerView();
        setupSearchListener();
    }

    private void setupSearchListener() {
        binding.etSearchHome.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // 1. Ambil teks pencarian
                String query = binding.etSearchHome.getText().toString().trim();
                if (!query.isEmpty()) {
                    // 2. Siapkan bundle untuk mengirim data
                    Bundle bundle = new Bundle();
                    bundle.putString("search_query", query);

                    Navigation.findNavController(v).navigate(R.id.action_home_to_search, bundle);
                }
                return true;
            }
            return false;
        });
    }

    private void setupRecommendedRecyclerView() {
        // Inisialisasi adapter dengan list kosong
        recommendedAdapter = new RecommendedAdapter(new ArrayList<>(), recipeId -> {
            Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
            intent.putExtra("RECIPE_ID", recipeId);
            startActivity(intent);
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvRecommended.setLayoutManager(layoutManager);
        binding.rvRecommended.setAdapter(recommendedAdapter);
    }

    private void fetchRecommendedRecipes() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        apiService.getRandomRecipes(10, API_KEY).enqueue(new Callback<RandomRecipeResponse>() {
            @Override
            public void onResponse(Call<RandomRecipeResponse> call, Response<RandomRecipeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Update adapter dengan data dari API
                    recommendedAdapter.updateRecipes(response.body().getRecipes());
                } else {
                    Toast.makeText(getContext(), "Failed to load recommended recipes.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RandomRecipeResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupPopularRecyclerView() {
        // 1. Data palsu
        List<Recipe> popularList = new ArrayList<>();
//        popularList.add(new Recipe(111111,"Mocktail", "5 minutes", "10 kcal", R.drawable.ic_beverages, "It's savory, hearty, and often served with toppings like sweet corn, bean sprouts, ground meat, and butter."));
//        popularList.add(new Recipe(222222, "Lasagna", "45 minutes", "74 kcal", R.drawable.ic_noodles, "It's savory, hearty, and often served with toppings like sweet corn, bean sprouts, ground meat, and butter."));
//        popularList.add(new Recipe(333333, "Risotto", "8 minutes", "90 kcal", R.drawable.ic_salad, "It's savory, hearty, and often served with toppings like sweet corn, bean sprouts, ground meat, and butter."));
//        popularList.add(new Recipe(444444, "Pancake", "12 minutes", "150 kcal", R.drawable.ic_breakfast, "It's savory, hearty, and often served with toppings like sweet corn, bean sprouts, ground meat, and butter."));

        // 2.  Adapter dengan Click Listener
        PopularAdapter adapter = new PopularAdapter(popularList, recipeId -> {
            // Logika yang sama seperti di Recommended RecyclerView
            Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
            intent.putExtra("RECIPE_ID", recipeId);
            startActivity(intent);
        });

        // 3. Atur LayoutManager menjadi GRID
        int spanCount = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);

        // 4. Buat dan terapkan ItemDecoration
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        // Hapus dekorasi lama jika ada untuk menghindari duplikasi
        if (binding.rvPopular.getItemDecorationCount() > 0) {
            binding.rvPopular.removeItemDecorationAt(0);
        }
        binding.rvPopular.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacingInPixels, false));

        // 5. Hubungkan ke RecyclerView
        binding.rvPopular.setLayoutManager(layoutManager);
        binding.rvPopular.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
