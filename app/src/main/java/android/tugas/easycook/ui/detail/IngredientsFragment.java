package android.tugas.easycook.ui.detail;

import android.os.Bundle;
import android.tugas.easycook.databinding.FragmentIngredientsBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class IngredientsFragment extends Fragment {

    private FragmentIngredientsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentIngredientsBinding.inflate(inflater, container, false);
        
        String ingredientsText = "For the broth:\n" +
                "- 2 tablespoons miso paste (white or red)\n" +
                "- 2 cups chicken broth or vegetable broth\n" +
                "- 1 teaspoon soy sauce\n" +
                "- 1 teaspoon sesame oil\n" +
                "- 2 cloves garlic, minced\n" +
                "- 1 teaspoon grated ginger\n" +
                "- 1 tablespoon mirin (optional, for sweetness)\n" +
                "- Chili oil (optional, for a spicy kick)\n\n" +
                "For the Noodles:\n" +
                "- 150-200 grams ramen noodles (fresh or dried)\n\n" +
                "Toppings (Optional but Recommended):\n" +
                "- 1 soft-boiled egg (ajitama)\n" +
                "- 2-3 slices chashu pork or grilled chicken\n" +
                "- ¼ cup corn kernels\n" +
                "- ¼ cup bean sprouts\n" +
                "- Green onions, chopped\n" +
                "- Nori (seaweed sheet)\n" +
                "- Sesame seeds";

        binding.tvIngredientsContent.setText(ingredientsText);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
