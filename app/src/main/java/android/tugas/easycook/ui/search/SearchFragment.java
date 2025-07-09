package android.tugas.easycook.ui.search;

import android.os.Bundle;
import android.tugas.easycook.R;
import android.tugas.easycook.data.model.Recipe;
import android.tugas.easycook.databinding.FragmentSearchBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
        setupSearchListener();
    }

    private void setupRecyclerView() {
        // 1. Buat Adapter dengan list kosong
        SearchAdapter adapter = new SearchAdapter(new ArrayList<>());
        binding.rvSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvSearchResults.setAdapter(adapter);

        // 2. Muat data dummy untuk tampilan awal
        loadDummyData();
    }

    private void loadDummyData() {
        // Buat daftar resep palsu
        List<Recipe> dummyList = new ArrayList<>();
        dummyList.add(new Recipe(555555,"Shoyu Ramen", "9 minutes", "90 kcal", R.drawable.ic_beverages, "Typically served with curly noodles, savory broth, chashu pork, nori, and a soft-boiled egg."));
        dummyList.add(new Recipe(666666,"Tonkotsu Ramen", "7 minutes", "60 kcal", R.drawable.ic_beverages, "It's deeply flavorful, thick, and often served with thin straight noodles, sesame pork, black garlic oil, and a soft-boiled egg."));
        dummyList.add(new Recipe(777777,"Shio Ramen", "8 minutes", "90 kcal", R.drawable.ic_beverages, "It has a delicate, clean flavor and is usually paired with thin straight noodles, chashu pork, green onions, boiled egg, and seaweed."));
        dummyList.add(new Recipe(888888,"Miso Ramen", "10 minutes", "100 kcal", R.drawable.ic_beverages, "It's savory, hearty, and often served with toppings like sweet corn, bean sprouts, ground meat, and butter."));

        // Perbarui adapter dengan data baru
        SearchAdapter adapter = (SearchAdapter) binding.rvSearchResults.getAdapter();
        if (adapter != null) {
            adapter.recipeList.clear();
            adapter.recipeList.addAll(dummyList);
            adapter.notifyDataSetChanged();
        }
    }

    private void setupSearchListener() {
        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // Ambil teks pencarian
                String query = binding.etSearch.getText().toString().trim();
                if (!query.isEmpty()) {
                    // Tampilkan Toast sebagai placeholder panggilan API
                    Toast.makeText(getContext(), "Searching for: " + query, Toast.LENGTH_SHORT).show();

                }
                return true;
            }
            return false;
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
