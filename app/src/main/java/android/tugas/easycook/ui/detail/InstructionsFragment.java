package android.tugas.easycook.ui.detail;

import android.os.Bundle;
import android.tugas.easycook.databinding.FragmentInstructionsBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class InstructionsFragment extends Fragment {

    private FragmentInstructionsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInstructionsBinding.inflate(inflater, container, false);

        String instructionsText = "1. Prepare the broth: In a medium pot, heat the sesame oil over medium heat. Add the minced garlic and grated ginger, and sauté for about 30 seconds until fragrant.\n\n" +
                "2. Add liquids: Pour in the chicken or vegetable broth and bring to a gentle simmer. Reduce the heat to low.\n\n" +
                "3. Mix in miso: In a small bowl, take a ladleful of the hot broth and whisk it with the miso paste until smooth. This prevents clumps. Pour the miso mixture back into the pot. Add the soy sauce and mirin (if using). Keep the broth warm but do not let it boil.\n\n" +
                "4. Cook the noodles: While the broth is simmering, cook the ramen noodles according to package directions. Drain well.\n\n" +
                "5. Assemble the bowl: Place the cooked noodles into a large bowl. Pour the hot miso broth over the noodles. Arrange your desired toppings over the noodles. Add a dash of chili oil if you like it spicy.\n\n" +
                "6. Serve immediately and enjoy!";

        binding.tvInstructionsContent.setText(instructionsText);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
