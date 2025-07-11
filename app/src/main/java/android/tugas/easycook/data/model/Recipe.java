package android.tugas.easycook.data.model;

import com.google.gson.annotations.SerializedName;

public class Recipe {

    // Menggunakan @SerializedName agar nama field di Java bisa berbeda dari nama field di JSON
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title; // API mengembalikan 'title', bukan 'name'

    @SerializedName("image")
    private String imageUrl; // API mengembalikan URL gambar (String)

    @SerializedName("readyInMinutes")
    private int readyInMinutes; // API mengembalikan 'readyInMinutes'

    @SerializedName("summary")
    private String summary; // Deskripsi dari API

    // FIELD BARU DITAMBAHKAN
    @SerializedName("spoonacularScore")
    private double spoonacularScore;

    @SerializedName("healthScore")
    private double healthScore;


    // Getter methods
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public String getSummary() {
        return summary;
    }

    // GETTER BARU DITAMBAHKAN
    public double getSpoonacularScore() {
        return spoonacularScore;
    }

    public double getHealthScore() {
        return healthScore;
    }
}
