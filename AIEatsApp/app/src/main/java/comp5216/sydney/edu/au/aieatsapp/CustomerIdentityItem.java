package comp5216.sydney.edu.au.aieatsapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "customerIdentity")
public class CustomerIdentityItem {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "customerID")
    private int customerID;

    @ColumnInfo(name = "customerUsername")
    private String customerUsername;

    @ColumnInfo(name = "customerPassword")
    private String customerPassword;

    @ColumnInfo(name = "customerEmail")
    private String customerEmail;

    @ColumnInfo(name = "customerIdentityName")
    private String customerIdentityName;

    @ColumnInfo(name = "customerIdentityNum")
    private String customerIdentityNum;

    public CustomerIdentityItem(String customerUsername, String customerPassword, String customerEmail, String customerIdentityName, String customerIdentityNum) {
        this.customerUsername = customerUsername;
        this.customerPassword = customerPassword;
        this.customerEmail = customerEmail;
        this.customerIdentityName = customerIdentityName;
        this.customerIdentityNum = customerIdentityNum;
    }

    @NonNull
    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(@NonNull int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    public String getCustomerPassword() {
        return customerPassword;
    }

    public void setCustomerPassword(String customerPassword) {
        this.customerPassword = customerPassword;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerIdentityName() {
        return customerIdentityName;
    }

    public void setCustomerIdentityName(String customerIdentityName) {
        this.customerIdentityName = customerIdentityName;
    }

    public String getCustomerIdentityNum() {
        return customerIdentityNum;
    }

    public void setCustomerIdentityNum(String customerIdentityNum) {
        this.customerIdentityNum = customerIdentityNum;
    }
}
