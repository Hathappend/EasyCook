package android.tugas.easycook.ui.home;

import android.os.Bundle;
import android.tugas.easycook.R;
import android.tugas.easycook.data.model.Recipe;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.tugas.easycook.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

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
         setupPopularRecyclerView();
    }

    private void setupRecommendedRecyclerView() {
        // 1. data palsu (dummy data)
        List<Recipe> recommendedList = new ArrayList<>();
        recommendedList.add(new Recipe("Zucchini Noodles", "20 minutes", "180 kcal", R.drawable.ic_beverages, "Typically served with curly noodles, savory broth, chashu pork, nori, and a soft-boiled egg."));
        recommendedList.add(new Recipe("Chickpea Curry", "15 minutes", "200 kcal", R.drawable.ic_beverages, "Typically served with curly noodles, savory broth, chashu pork, nori, and a soft-boiled egg."));
        recommendedList.add(new Recipe("Tiramisu", "10 minutes", "350 kcal", R.drawable.ic_beverages, "Typically served with curly noodles, savory broth, chashu pork, nori, and a soft-boiled egg."));

        // 2. Adapter
        RecommendedAdapter adapter = new RecommendedAdapter(recommendedList);

        // 3. LayoutManager menjadi HORIZONTAL
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        // 4. Hubungkan ke RecyclerView
        binding.rvRecommended.setLayoutManager(layoutManager);
        binding.rvRecommended.setAdapter(adapter);
    }

    private void setupPopularRecyclerView() {
        // 1. data palsu
        List<Recipe> popularList = new ArrayList<>();
        popularList.add(new Recipe("Mocktail", "5 minutes", "10 kcal", R.drawable.ic_beverages, "It's savory, hearty, and often served with toppings like sweet corn, bean sprouts, ground meat, and butter.\""));
        popularList.add(new Recipe("Lasagna", "45 minutes", "74 kcal", R.drawable.ic_beverages, "It's savory, hearty, and often served with toppings like sweet corn, bean sprouts, ground meat, and butter."));
        popularList.add(new Recipe("Risotto", "8 minutes", "90 kcal", R.drawable.ic_beverages, "It's savory, hearty, and often served with toppings like sweet corn, bean sprouts, ground meat, and butter."));
        popularList.add(new Recipe("Pancake", "12 minutes", "150 kcal", R.drawable.ic_beverages, "It's savory, hearty, and often served with toppings like sweet corn, bean sprouts, ground meat, and butter."));

        // 2.  Adapter
        PopularAdapter adapter = new PopularAdapter(popularList);

        // 3. Atur LayoutManager menjadi GRID
        int spanCount = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);

        // 4. Buat dan terapkan ItemDecoration
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
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