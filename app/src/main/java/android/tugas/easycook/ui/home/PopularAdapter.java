package android.tugas.easycook.ui.home;

import android.tugas.easycook.data.model.Recipe;
import android.tugas.easycook.databinding.HomeItemPopularRecipeBinding;
import android.tugas.easycook.databinding.SearchItemShowMoreBinding; // <-- Import binding untuk show more
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;
import java.util.Locale;

public class PopularAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Recipe> recipeList;
    private final OnItemClickListener listener;
    private final OnShowMoreClickListener showMoreListener;

    private static final int VIEW_TYPE_ITEM = 0;
    protected static final int VIEW_TYPE_FOOTER = 1;

    private boolean showFooter = false;

    public interface OnItemClickListener {
        void onItemClick(int recipeId);
    }

    public interface OnShowMoreClickListener {
        void onShowMoreClick();
    }
    public PopularAdapter(List<Recipe> recipeList, OnItemClickListener listener, OnShowMoreClickListener showMoreListener) {
        this.recipeList = recipeList;
        this.listener = listener;
        this.showMoreListener = showMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (showFooter && position == recipeList.size()) {
            return VIEW_TYPE_FOOTER;
        }
        return VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_FOOTER) {
            SearchItemShowMoreBinding binding = SearchItemShowMoreBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new FooterViewHolder(binding);
        } else {
            HomeItemPopularRecipeBinding binding = HomeItemPopularRecipeBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new RecipeViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecipeViewHolder) {
            ((RecipeViewHolder) holder).bind(recipeList.get(position), listener);
        } else if (holder instanceof FooterViewHolder) {
            ((FooterViewHolder) holder).bind(showMoreListener);
        }
    }

    @Override
    public int getItemCount() {
        return recipeList.size() + (showFooter ? 1 : 0);
    }

    public void addRecipes(List<Recipe> newRecipes) {
        int startPosition = recipeList.size();
        recipeList.addAll(newRecipes);
        notifyItemRangeInserted(startPosition, newRecipes.size());
    }

    public void showFooter(boolean show) {
        if (this.showFooter == show) return;
        this.showFooter = show;
        if (show) {
            notifyItemInserted(recipeList.size());
        } else {
            notifyItemRemoved(recipeList.size());
        }
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        private final HomeItemPopularRecipeBinding binding;

        public RecipeViewHolder(HomeItemPopularRecipeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final Recipe recipe, final OnItemClickListener listener) {
            binding.tvNamePopular.setText(recipe.getTitle());
            String timeText = recipe.getReadyInMinutes() + " minutes";
            binding.tvTimePopular.setText(timeText);

            Glide.with(itemView.getContext())
                    .load(recipe.getImageUrl())
                    .into(binding.ivRecipeImage);

            double rating = recipe.getSpoonacularScore() / 20.0;
            binding.tvRatingPopular.setText(String.format(Locale.US, "%.1f", rating));

            itemView.setOnClickListener(v -> listener.onItemClick(recipe.getId()));
        }
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {
        private final SearchItemShowMoreBinding binding;

        public FooterViewHolder(SearchItemShowMoreBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final OnShowMoreClickListener listener) {
            binding.btnShowMoreItem.setOnClickListener(v -> listener.onShowMoreClick());
        }
    }
}