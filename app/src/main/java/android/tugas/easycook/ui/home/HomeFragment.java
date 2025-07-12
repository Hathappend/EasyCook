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
import android.tugas.easycook.data.response.RecipeListResponse;
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
    private PopularAdapter popularAdapter;
    private int popularCurrentPage = 0;
    private final int RECIPES_PER_PAGE = 10;
    private final String API_KEY = "9135788718664371a9de785a0ed83a7d";
    private final ApiService apiService = ApiClient.getClient().create(ApiService.class);

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
        fetchPopularRecipes(popularCurrentPage);
        setupSearchListener();
    }

    private void setupSearchListener() {
        binding.etSearchHome.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = binding.etSearchHome.getText().toString().trim();
                if (!query.isEmpty()) {
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

        apiService.getRandomRecipes(10, API_KEY).enqueue(new Callback<RecipeListResponse>() {
            @Override
            public void onResponse(Call<RecipeListResponse> call, Response<RecipeListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recommendedAdapter.updateRecipes(response.body().getRecipes());
                } else {
                    Toast.makeText(getContext(), "Failed to load recommended recipes.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecipeListResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupPopularRecyclerView() {
        popularAdapter = new PopularAdapter(
                new ArrayList<>(),
                recipeId -> {
                    Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
                    intent.putExtra("RECIPE_ID", recipeId);
                    startActivity(intent);
                },
                () -> {
                    popularCurrentPage++;
                    fetchPopularRecipes(popularCurrentPage);
                }
        );

        // Atur LayoutManager menjadi GRID
        int spanCount = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                // Cek tipe item pada posisi tertentu dari adapter
                if (popularAdapter.getItemViewType(position) == PopularAdapter.VIEW_TYPE_FOOTER) {
                    return spanCount;
                } else {
                    return 1;
                }
            }
        });

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        if (binding.rvPopular.getItemDecorationCount() > 0) {
            binding.rvPopular.removeItemDecorationAt(0);
        }
        binding.rvPopular.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacingInPixels, false));

        // Hubungkan ke RecyclerView
        binding.rvPopular.setLayoutManager(layoutManager);
        binding.rvPopular.setAdapter(popularAdapter);
    }

    private void fetchPopularRecipes(int page) {
        int offset = page * RECIPES_PER_PAGE;

        // Panggil API dengan offset
        apiService.searchPopularRecipes("popularity", RECIPES_PER_PAGE, true, API_KEY, offset)
                .enqueue(new Callback<RecipeListResponse>() {
                    @Override
                    public void onResponse(Call<RecipeListResponse> call, Response<RecipeListResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Recipe> newRecipes = response.body().getRecipes();

                            popularAdapter.addRecipes(newRecipes);

                            popularAdapter.showFooter(!newRecipes.isEmpty());
                        } else {
                            Toast.makeText(getContext(), "Failed to load popular recipes.", Toast.LENGTH_SHORT).show();
                            popularAdapter.showFooter(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<RecipeListResponse> call, Throwable t) {
                        Toast.makeText(getContext(), "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        popularAdapter.showFooter(false);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
