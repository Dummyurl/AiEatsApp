package comp5216.sydney.edu.au.aieatsapp;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface foodDao {
    @Query("SELECT * FROM foodList")
    List<FoodItem> listAll();

    @Insert
    void insert(FoodItem foodItem);

    @Insert
    void insertAll(FoodItem... foodItems);

    @Query("DELETE FROM foodList")
    void deleteAll();
}
