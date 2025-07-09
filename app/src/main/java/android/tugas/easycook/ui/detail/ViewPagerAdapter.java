package android.tugas.easycook.ui.detail;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Mengembalikan fragment yang sesuai berdasarkan posisi tab
        switch (position) {
            case 0: // Posisi 0 untuk tab pertama (Ingredients)
                return new IngredientsFragment();
            case 1: // Posisi 1 untuk tab kedua (Instructions)
                return new InstructionsFragment();
            default:
                return new IngredientsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
