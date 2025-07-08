package android.tugas.easycook.ui.search;

import android.tugas.easycook.R;
import android.tugas.easycook.data.model.Recipe;
import android.tugas.easycook.databinding.SearchItemResultBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.RecipeViewHolder> {

    protected final List<Recipe> recipeList;

    public SearchAdapter(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Gunakan ViewBinding untuk membuat ViewHolder
        SearchItemResultBinding binding = SearchItemResultBinding.inflate(
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
        private final SearchItemResultBinding binding;

        public RecipeViewHolder(SearchItemResultBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Recipe recipe) {
            binding.tvNameSearch.setText(recipe.getName());
            binding.tvDescriptionSearch.setText(recipe.getDescription());
            binding.tvTimeSearch.setText(recipe.getTime());
            binding.tvCaloriesSearch.setText(recipe.getCalories());
            binding.ivRecipeSearch.setImageResource(recipe.getImage());
        }
    }
}
