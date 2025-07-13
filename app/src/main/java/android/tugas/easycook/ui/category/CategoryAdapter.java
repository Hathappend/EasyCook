package android.tugas.easycook.ui.category;

// File: CategoryAdapter.java
import android.tugas.easycook.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.tugas.easycook.data.model.Category;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categoryList;

    // Constructor untuk menerima data yang akan ditampilkan
    public CategoryAdapter(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    /**
     * ViewHolder bertugas untuk menyimpan referensi view dari layout item_category.xml
     */
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCategoryIcon;
        TextView tvCategoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCategoryIcon = itemView.findViewById(R.id.iv_category_icon);
            tvCategoryName = itemView.findViewById(R.id.tv_category_name);
        }
    }

    /**
     * Metode ini dipanggil saat ViewHolder baru perlu dibuat.
     * Di sinilah kita meng-inflate (mengubah XML menjadi View) layout item kita.
     */
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Membuat view baru dari layout item_category.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(view);
    }

    /**
     * Metode ini dipanggil untuk mengisi data ke dalam ViewHolder.
     * Di sinilah kita mengambil data dari list dan menampilkannya di view.
     */
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        // Ambil data kategori berdasarkan posisi
        Category currentCategory = categoryList.get(position);

        // Set data ke view di dalam ViewHolder
        holder.ivCategoryIcon.setImageResource(currentCategory.getIconResId());
        holder.tvCategoryName.setText(currentCategory.getName());

        // Menambahkan OnClickListener pada setiap item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Contoh aksi saat item di-klik
                Toast.makeText(v.getContext(), "Anda memilih: " + currentCategory.getName(), Toast.LENGTH_SHORT).show();
                // Anda bisa menambahkan logika lain di sini, seperti membuka halaman baru
            }
        });
    }

    /**
     * Metode ini mengembalikan jumlah total item dalam list.
     */
    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
