package android.tugas.easycook.ui.home;

// File: PopularAdapter.java
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.tugas.easycook.data.model.Recipe;
import android.tugas.easycook.databinding.HomeItemPopularRecipeBinding;
import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.RecipeViewHolder> {

    private final List<Recipe> recipeList;

    public PopularAdapter(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HomeItemPopularRecipeBinding binding = HomeItemPopularRecipeBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new RecipeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.bind(recipe);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        private final HomeItemPopularRecipeBinding binding;

        public RecipeViewHolder(HomeItemPopularRecipeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Recipe recipe) {
            binding.tvNamePopular.setText(recipe.getName());
            binding.tvTimePopular.setText(recipe.getTime());
//            binding.tvCaloriesPopular.setText(recipe.getCalories());
//            binding.ivRecipePopular.setImageResource(recipe.getImage());
        }
    }
}
