package android.tugas.easycook.ui.search;

import android.text.Html;
import android.tugas.easycook.R;
import android.tugas.easycook.data.model.Recipe;
import android.tugas.easycook.databinding.SearchItemShowMoreBinding;
import android.tugas.easycook.databinding.SearchItemResultBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Recipe> recipeList;
    private final OnRecipeClickListener listener;
    private final OnShowMoreClickListener showMoreListener;

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_FOOTER = 1;

    private boolean showFooter = false;

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    public interface OnShowMoreClickListener {
        void onShowMoreClick();
    }

    public SearchAdapter(List<Recipe> recipeList, OnRecipeClickListener listener, OnShowMoreClickListener showMoreListener) {
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
            SearchItemResultBinding binding = SearchItemResultBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new RecipeViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecipeViewHolder) {
            Recipe recipe = recipeList.get(position);
            ((RecipeViewHolder) holder).bind(recipe, listener);
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

    public void clear() {
        int size = recipeList.size();
        if (size > 0) {
            recipeList.clear();
            notifyItemRangeRemoved(0, size);
        }
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
        private final SearchItemResultBinding binding;

        public RecipeViewHolder(SearchItemResultBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final Recipe recipe, final OnRecipeClickListener  listener) {
            binding.tvNameSearch.setText(recipe.getTitle());
            if (recipe.getSummary() != null) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    binding.tvDescriptionSearch.setText(Html.fromHtml(recipe.getSummary(), Html.FROM_HTML_MODE_LEGACY));
                } else {
                    binding.tvDescriptionSearch.setText(Html.fromHtml(recipe.getSummary()));
                }
            } else {
                binding.tvDescriptionSearch.setText("");
            }
            String time = recipe.getReadyInMinutes() + " minutes";
            binding.tvTimeSearch.setText(time);
            binding.ivFireSearch.setVisibility(View.GONE);
            binding.tvCaloriesSearch.setVisibility(View.GONE);
            Glide.with(itemView.getContext())
                    .load(recipe.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(binding.ivRecipeSearch);
            itemView.setOnClickListener(v -> listener.onRecipeClick(recipe));
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