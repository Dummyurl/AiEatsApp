package comp5216.sydney.edu.au.aieatsapp;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CustomerIdentityDao {
    @Query("SELECT * FROM customerIdentity")
    List<CustomerIdentityItem> listAll();

    @Insert
    void insert(CustomerIdentityItem customerIdentityItem);

    @Insert
    void insertAll(CustomerIdentityItem... customerIdentityItems);

    @Query("DELETE FROM customerIdentity")
    void deleteAll();
}
