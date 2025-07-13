package android.tugas.easycook.data.model;

import com.google.gson.annotations.SerializedName;

public class Nutrient {
    @SerializedName("title")
    private String title;

    @SerializedName("amount")
    private String amount;

    @SerializedName("percentOfDailyNeeds")
    private double percentOfDailyNeeds;

    public String getTitle() { return title; }
    public String getAmount() { return amount; }
    public double getPercentOfDailyNeeds() { return percentOfDailyNeeds; }
}