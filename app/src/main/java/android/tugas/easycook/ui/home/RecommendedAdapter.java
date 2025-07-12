package android.tugas.easycook.ui.home;

import android.tugas.easycook.data.model.Recipe;
import android.tugas.easycook.databinding.HomeItemRecommendedRecipeBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Locale;

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

    public void updateRecipes(List<Recipe> newRecipes) {
        recipeList.clear();
        recipeList.addAll(newRecipes);
        notifyDataSetChanged();
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
            // Set data dari API ke View
            binding.tvNameRecommended.setText(recipe.getTitle());
            binding.tvTimeRecommended.setText(recipe.getReadyInMinutes() + " minutes");

            // Gunakan Glide untuk memuat gambar dari URL
            Glide.with(itemView.getContext())
                    .load(recipe.getImageUrl())
                    .into(binding.recipeImage);

            // Konversi spoonacularScore (0-100) menjadi rating bintang (0-5.0)
            double rating = recipe.getSpoonacularScore() / 20.0;
            binding.tvRatingRecommended.setText(String.format(Locale.US, "%.1f", rating));

            itemView.setOnClickListener(v -> listener.onItemClick(recipe.getId()));
        }
    }
}
