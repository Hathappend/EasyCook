package android.tugas.easycook.data.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {PlannedMeal.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PlannedMealDao plannedMealDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "easycook_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}