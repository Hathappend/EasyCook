package android.tugas.easycook.ui.home;

import android.tugas.easycook.data.model.Recipe;
import android.tugas.easycook.databinding.HomeItemPopularRecipeBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.RecipeViewHolder> {

    private final List<Recipe> recipeList;
    private final OnItemClickListener listener;

    // 1. Definisikan Interface untuk click listener
    public interface OnItemClickListener {
        void onItemClick(int recipeId);
    }

    // 2. Perbarui Constructor untuk menerima listener
    public PopularAdapter(List<Recipe> recipeList, OnItemClickListener listener) {
        this.recipeList = recipeList;
        this.listener = listener;
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
        // 3. Kirim resep dan listener ke ViewHolder
        holder.bind(recipe, listener);
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

        // 4. Method bind sekarang juga menerima listener
        public void bind(final Recipe recipe, final OnItemClickListener listener) {
            binding.tvNamePopular.setText(recipe.getName());
            binding.tvTimePopular.setText(recipe.getTime());
//            binding.tvCaloriesPopular.setText(recipe.getCalories());
//            binding.ivRecipeImage.setImageResource(recipe.getImage());

            // 5. Set OnClickListener pada seluruh item view
            itemView.setOnClickListener(v -> listener.onItemClick(recipe.getId()));
        }
    }
}
