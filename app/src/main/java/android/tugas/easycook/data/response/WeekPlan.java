package android.tugas.easycook.data.response;

import com.google.gson.annotations.SerializedName;

public class WeekPlan {
    @SerializedName("monday") private DayPlan monday;
    @SerializedName("tuesday") private DayPlan tuesday;
    @SerializedName("wednesday") private DayPlan wednesday;
    @SerializedName("thursday") private DayPlan thursday;
    @SerializedName("friday") private DayPlan friday;
    @SerializedName("saturday") private DayPlan saturday;
    @SerializedName("sunday") private DayPlan sunday;

    public DayPlan getMonday() { return monday; }
    public DayPlan getTuesday() { return tuesday; }
    public DayPlan getWednesday() { return wednesday; }
    public DayPlan getThursday() { return thursday; }
    public DayPlan getFriday() { return friday; }
    public DayPlan getSaturday() { return saturday; }
    public DayPlan getSunday() { return sunday; }


}