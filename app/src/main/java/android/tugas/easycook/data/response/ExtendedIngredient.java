package android.tugas.easycook.data.response;

import com.google.gson.annotations.SerializedName;

public class ExtendedIngredient {
    @SerializedName("original")
    private String original;

    public String getOriginal() { return original; }
}