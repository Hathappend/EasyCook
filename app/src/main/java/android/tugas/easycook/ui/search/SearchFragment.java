package android.tugas.easycook.ui.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.tugas.easycook.data.api.ApiClient;
import android.tugas.easycook.data.api.ApiService;
import android.tugas.easycook.data.model.Recipe;
import android.tugas.easycook.data.response.SearchApiResponse;
import android.tugas.easycook.databinding.FragmentSearchBinding;
import android.tugas.easycook.ui.detail.RecipeDetailActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;
import android.tugas.easycook.BuildConfig;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchDebug";
    private FragmentSearchBinding binding;
    private SearchAdapter searchAdapter;
    private ApiService apiService;
    private boolean isSelectingRecipe = false;
    private boolean isLoading = false;
    private String currentQuery = "";
    private int currentOffset = 0;
    private int totalResults = 0;
    private static final int PAGE_SIZE = 10;
    private final String API_KEY = BuildConfig.API_KEY;

    private final ActivityResultLauncher<Intent> recipeDetailLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // Jika RecipeDetailActivity mengembalikan hasil OK, teruskan hasilnya ke activity sebelumnya
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    if (getActivity() != null) {
                        getActivity().setResult(Activity.RESULT_OK, result.getData());
                        getActivity().finish();
                    }
                }
            }
    );

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiService = ApiClient.getClient().create(ApiService.class);

        // Cek apakah fragment dibuka dalam mode memilih
        if (getArguments() != null) {
            isSelectingRecipe = getArguments().getBoolean("IS_SELECTING_RECIPE", false);
        }

        setupRecyclerView();
        setupSearchListener();

        if (getArguments() != null) {
            String queryFromHome = getArguments().getString("search_query");
            if (queryFromHome != null && !queryFromHome.isEmpty()) {
                binding.etSearch.setText(queryFromHome);
                currentQuery = queryFromHome;
                currentOffset = 0;
                totalResults = 0;
                searchAdapter.clear();
                searchAdapter.showFooter(false);
                performSearch();
            }
        }
    }

    private void setupRecyclerView() {
        searchAdapter = new SearchAdapter(new ArrayList<>(),
                recipe -> {
                    Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
                    intent.putExtra("RECIPE_ID", recipe.getId());
                    // Teruskan flag 'isSelectingRecipe' ke halaman detail
                    intent.putExtra("IS_SELECTING_RECIPE", isSelectingRecipe);
                    if (isSelectingRecipe) {
                        recipeDetailLauncher.launch(intent);
                    } else {
                        startActivity(intent);
                    }
                },
                () -> {
                    if (!isLoading) {
                        performSearch();
                    }
                }
        );
        binding.rvSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvSearchResults.setAdapter(searchAdapter);
    }

    private void setupSearchListener() {
        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = binding.etSearch.getText().toString().trim();
                if (!query.isEmpty()) {
                    currentQuery = query;
                    currentOffset = 0;
                    totalResults = 0;
                    searchAdapter.showFooter(false);
                    searchAdapter.clear();
                    performSearch();
                }
                return true;
            }
            return false;
        });
    }

    private void performSearch() {
        if (currentQuery.isEmpty() || isLoading) return;

        isLoading = true;

        Log.d(TAG, "--> Melakukan pencarian untuk: '" + currentQuery + "', offset: " + currentOffset);

        Call<SearchApiResponse> call = apiService.searchRecipes(API_KEY, currentQuery, true, PAGE_SIZE, currentOffset);
        call.enqueue(new Callback<SearchApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<SearchApiResponse> call, @NonNull Response<SearchApiResponse> response) {
                isLoading = false;

                // Menampilkan status respons dari API
                Log.d(TAG, "Respons diterima. Kode: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    if (currentOffset == 0) {
                        totalResults = response.body().getTotalResults();
                        Log.d(TAG, "PANGGILAN PERTAMA. Total hasil dari API: " + totalResults);
                    }

                    List<Recipe> recipes = response.body().getResults();
                    int receivedCount = (recipes != null) ? recipes.size() : 0;
                    Log.d(TAG, "Jumlah resep yang diterima: " + receivedCount);

                    if (receivedCount > 0) {
                        searchAdapter.addRecipes(recipes);
                        currentOffset += receivedCount;
                        Log.d(TAG, "Offset sekarang menjadi: " + currentOffset);
                        Log.d(TAG, "Pengecekan: apakah offset (" + currentOffset + ") < totalResults (" + totalResults + ")?");

                        if (currentOffset < totalResults) {
                            searchAdapter.showFooter(true);
                            Log.d(TAG, "--> HASIL: Benar. Menampilkan footer 'Show More'.");
                        } else {
                            searchAdapter.showFooter(false);
                            Log.d(TAG, "--> HASIL: Salah. Menyembunyikan footer 'Show More'.");
                        }
                    } else {
                        searchAdapter.showFooter(false);
                        Log.d(TAG, "Tidak ada resep diterima, sembunyikan footer.");
                        if (currentOffset == 0) {
                            Toast.makeText(getContext(), "No recipes found", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    searchAdapter.showFooter(false);
                    Toast.makeText(getContext(), "API Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Respons API tidak sukses atau body null.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchApiResponse> call, @NonNull Throwable t) {
                isLoading = false;
                searchAdapter.showFooter(false);
                Log.e(TAG, "Panggilan API GAGAL: ", t);
                Toast.makeText(getContext(), "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}