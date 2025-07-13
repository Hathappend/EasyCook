package android.tugas.easycook.data.response;

import com.google.gson.annotations.SerializedName;

public class Meal {
    @SerializedName("id") private int id;
    @SerializedName("title") private String title;
    @SerializedName("readyInMinutes") private int readyInMinutes;
    @SerializedName("servings") private int servings;

    public int getId() { return id; }
    public String getTitle() { return title; }
}