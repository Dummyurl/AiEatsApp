package comp5216.sydney.edu.au.aieatsapp;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {CustomerIdentityItem.class}, version = 1, exportSchema = false)
public abstract class CustomerIdentityDB extends RoomDatabase {
    private static final String DATABASE_NAME = "customerIdentity_db";
    private static CustomerIdentityDB DBINSTANCE;

    public abstract CustomerIdentityDao customerIdentityDao();

    public static CustomerIdentityDB getDatabase(Context context) {
        if (DBINSTANCE == null) {
            synchronized (CustomerIdentityDB.class) {
                DBINSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        CustomerIdentityDB.class, DATABASE_NAME).build();
            }
        }
        return DBINSTANCE;
    }
    public static void destroyInstance() {
        DBINSTANCE = null;
    }
}
