package android.tugas.easycook.ui.detail;

import android.os.Bundle;
import android.tugas.easycook.data.response.ExtendedIngredient;
import android.tugas.easycook.databinding.FragmentIngredientsBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class IngredientsFragment extends Fragment {

    private FragmentIngredientsBinding binding;

    private static final String ARG_INGREDIENTS = "ingredients_list";

    public static IngredientsFragment newInstance(List<ExtendedIngredient> ingredients) {
        IngredientsFragment fragment = new IngredientsFragment();
        Bundle args = new Bundle();
        ArrayList<String> ingredientStrings = new ArrayList<>();
        for (ExtendedIngredient ingredient : ingredients) {
            ingredientStrings.add(ingredient.getOriginal());
        }
        args.putStringArrayList(ARG_INGREDIENTS, ingredientStrings);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentIngredientsBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            List<String> ingredientStrings = getArguments().getStringArrayList(ARG_INGREDIENTS);
            if (ingredientStrings != null) {
                StringBuilder sb = new StringBuilder();
                for (String ingredient : ingredientStrings) {
                    sb.append("• ").append(ingredient).append("\n\n");
                }
                binding.tvIngredientsContent.setText(sb.toString());
            }
        }
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
