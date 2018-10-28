package comp5216.sydney.edu.au.aieatsapp;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {FoodItem.class}, version = 1, exportSchema = false)
public abstract class FoodDB extends RoomDatabase {
    private static final String DATABASE_NAME = "foodItem_db";
    private static FoodDB DBINSTANCE;

    public abstract foodDao foodDao();

    public static FoodDB getDatabase(Context context) {
        if (DBINSTANCE == null) {
            synchronized (FoodDB.class) {
                DBINSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        FoodDB.class, DATABASE_NAME).build();
            }
        }
        return DBINSTANCE;
    }
    public static void destroyInstance() {
        DBINSTANCE = null;
    }
}
