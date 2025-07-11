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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
        // 1. Data palsu (dummy data)
        List<Recipe> recommendedList = new ArrayList<>();
//        recommendedList.add(new Recipe(654959, "Zucchini Noodles", "20 minutes", "180 kcal",R.drawable.ic_noodles, "Typically served with curly noodles, savory broth, chashu pork, nori, and a soft-boiled egg.\""));
//        recommendedList.add(new Recipe(639411, "Chickpea Curry", "15 minutes", "200 kcal", R.drawable.ic_salad, "Typically served with curly noodles, savory broth, chashu pork, nori, and a soft-boiled egg."));
//        recommendedList.add(new Recipe(663487,"Tiramisu", "10 minutes", "350 kcal", R.drawable.ic_dessert, "Typically served with curly noodles, savory broth, chashu pork, nori, and a soft-boiled egg."));

        // 2. Adapter dengan Click Listener
        RecommendedAdapter adapter = new RecommendedAdapter(recommendedList, recipeId -> {
            Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
            intent.putExtra("RECIPE_ID", recipeId);
            startActivity(intent);
        });

        // 3. LayoutManager menjadi HORIZONTAL
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        // 4. Hubungkan ke RecyclerView
        binding.rvRecommended.setLayoutManager(layoutManager);
        binding.rvRecommended.setAdapter(adapter);
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
