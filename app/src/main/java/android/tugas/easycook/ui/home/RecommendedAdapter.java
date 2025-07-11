package android.tugas.easycook.ui.home;

import android.tugas.easycook.data.model.Recipe;
import android.tugas.easycook.databinding.HomeItemRecommendedRecipeBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecommendedAdapter extends RecyclerView.Adapter<RecommendedAdapter.RecipeViewHolder> {

    private final List<Recipe> recipeList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int recipeId);
    }

    public RecommendedAdapter(List<Recipe> recipeList, OnItemClickListener listener) {
        this.recipeList = recipeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HomeItemRecommendedRecipeBinding binding = HomeItemRecommendedRecipeBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new RecipeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.bind(recipe, listener);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        private final HomeItemRecommendedRecipeBinding binding;

        public RecipeViewHolder(HomeItemRecommendedRecipeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final Recipe recipe, final OnItemClickListener listener) {
//            binding.tvNameRecommended.setText(recipe.getName());
//            binding.tvTimeRecommended.setText(recipe.getTime());

//            binding.tvCaloriesRecommended.setText(recipe.getCalories());
//            binding.recipeImage.setImageResource(recipe.getImage());

            itemView.setOnClickListener(v -> listener.onItemClick(recipe.getId()));
        }
    }
}
