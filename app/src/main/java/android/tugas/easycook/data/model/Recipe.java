package android.tugas.easycook.data.model;

public class Recipe {
    private int id;
    private String name;
    private String time;
    private String calories;
    private int image;
    private String description;

    public Recipe(int id, String name, String time, String calories, int image, String description) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.calories = calories;
        this.image = image;
        this.description = description;
    }

    public int getId() { return id; }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getCalories() {
        return calories;
    }

    public int getImage() {
        return image;
    }

    public String getDescription() { return description; }
}