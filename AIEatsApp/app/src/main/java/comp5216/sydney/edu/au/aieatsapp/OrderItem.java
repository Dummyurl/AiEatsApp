package comp5216.sydney.edu.au.aieatsapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "FoodOrder")
public class OrderItem {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "orderID")
    private int orderID;

    @ColumnInfo(name = "orderFoodName")
    private String orderFoodName;

    @ColumnInfo(name = "orderCustomer")
    private String orderCustomer;

    @ColumnInfo(name = "orderPhone")
    private String orderPhone;

    @ColumnInfo(name = "orderAddress")
    private String orderAddress;

    @ColumnInfo(name = "orderStatus")
    private String orderStatus;

    @ColumnInfo(name = "orderProvider")
    private String orderProvider;

    public OrderItem(@NonNull String orderFoodName, String orderCustomer,
                     String orderPhone, String orderAddress, String orderStatus, String orderProvider) {
        this.orderFoodName = orderFoodName;
        this.orderCustomer = orderCustomer;
        this.orderPhone = orderPhone;
        this.orderAddress = orderAddress;
        this.orderStatus = orderStatus;
        this.orderProvider = orderProvider;
    }

    @NonNull
    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(@NonNull int orderID) {
        this.orderID = orderID;
    }

    public String getOrderFoodName() {
        return orderFoodName;
    }

    public void setOrderFoodName(String orderFoodName) {
        this.orderFoodName = orderFoodName;
    }

    public String getOrderCustomer() {
        return orderCustomer;
    }

    public void setOrderCustomer(String orderCustomer) {
        this.orderCustomer = orderCustomer;
    }

    public String getOrderPhone() {
        return orderPhone;
    }

    public void setOrderPhone(String orderPhone) {
        this.orderPhone = orderPhone;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderProvider() {
        return orderProvider;
    }

    public void setOrderProvider(String orderProvider) {
        this.orderProvider = orderProvider;
    }
}
