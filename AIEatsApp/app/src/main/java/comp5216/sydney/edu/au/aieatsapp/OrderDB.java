package comp5216.sydney.edu.au.aieatsapp;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {OrderItem.class}, version = 1, exportSchema = false)
public abstract class OrderDB extends RoomDatabase {
    private static final String DATABASE_NAME = "foodOrder_db";
    private static OrderDB DBINSTANCE;

    public abstract OrderDao foodOrderDao();

    public static OrderDB getDatabase(Context context) {
        if (DBINSTANCE == null) {
            synchronized (OrderDB.class) {
                DBINSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        OrderDB.class, DATABASE_NAME).build();
            }
        }
        return DBINSTANCE;
    }
    public static void destroyInstance() {
        DBINSTANCE = null;
    }
}
