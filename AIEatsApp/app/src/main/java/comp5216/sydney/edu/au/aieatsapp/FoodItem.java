package comp5216.sydney.edu.au.aieatsapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "foodList")
public class FoodItem {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "foodID")
    private int foodID;

    @ColumnInfo(name = "foodName")
    private String foodName;

    @ColumnInfo(name = "foodPrice")
    private String foodPrice;

    @ColumnInfo(name = "foodDescription")
    private String foodDescription;

    @ColumnInfo(name = "foodProvider")
    private String foodProvider;

    public FoodItem(@NonNull String foodName, String foodPrice, String foodDescription, String foodProvider) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodDescription = foodDescription;
        this.foodProvider = foodProvider;
    }

    @NonNull
    public int getFoodID() {
        return foodID;
    }

    public void setFoodID(@NonNull int foodID) {
        this.foodID = foodID;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public String getFoodProvider() {
        return foodProvider;
    }

    public void setFoodProvider(String foodProvider) {
        this.foodProvider = foodProvider;
    }
}
