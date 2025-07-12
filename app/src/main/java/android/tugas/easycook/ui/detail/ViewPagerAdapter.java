package android.tugas.easycook.ui.detail;

import android.tugas.easycook.data.response.ExtendedIngredient;
import android.tugas.easycook.data.response.Step;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private List<ExtendedIngredient> ingredients = new ArrayList<>();
    private List<Step> steps = new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    public void setIngredients(List<ExtendedIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setInstructions(List<Step> steps) {
        this.steps = steps;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                // Kirim data ke IngredientsFragment
                return IngredientsFragment.newInstance(ingredients);
            case 1:
                // Kirim data ke InstructionsFragment
                return InstructionsFragment.newInstance(steps);
            default:
                return IngredientsFragment.newInstance(ingredients);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
