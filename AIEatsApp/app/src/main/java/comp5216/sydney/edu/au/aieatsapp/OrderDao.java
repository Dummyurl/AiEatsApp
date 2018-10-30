package comp5216.sydney.edu.au.aieatsapp;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface OrderDao {
    @Query("SELECT * FROM FoodOrder")
    List<OrderItem> listAll();

    @Query("UPDATE FoodOrder SET orderStatus = :status WHERE orderID =:id")
    void update(String status, int id);

    @Insert
    void insert(OrderItem orderItem);

    @Insert
    void insertAll(OrderItem... orderItems);

    @Update
    public void updateOrder(OrderItem orderItem);

    @Update
    public void updateOrders(OrderItem... orderItems);

    @Query("DELETE FROM FoodOrder")
    void deleteAll();
}
