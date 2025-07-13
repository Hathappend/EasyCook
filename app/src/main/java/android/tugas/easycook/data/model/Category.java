package android.tugas.easycook.data.model;

public class Category {
    private String name;
    private int iconResId; // Tipe data int untuk menyimpan ID drawable (misal: R.drawable.ic_breakfast)

    public Category(String name, int iconResId) {
        this.name = name;
        this.iconResId = iconResId;
    }

    public String getName() {
        return name;
    }

    public int getIconResId() {
        return iconResId;
    }
}