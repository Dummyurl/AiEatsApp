package comp5216.sydney.edu.au.aieatsapp;

public class OrderFormat {
    public String orderID,orderCustomer,orderFoodName,orderPhone,orderAddress,orderStatus,orderProvider;

    public OrderFormat (String orderCustomer, String orderFoodName, String orderPhone,
                       String orderAddress, String orderStatus, String orderProvider) {
        this.orderCustomer = orderCustomer;
        this.orderFoodName = orderFoodName;
        this.orderPhone = orderPhone;
        this.orderAddress = orderAddress;
        this.orderStatus = orderStatus;
        this.orderProvider = orderProvider;
    }

    public OrderFormat (String orderID, String orderCustomer, String orderFoodName, String orderPhone,
                        String orderAddress, String orderStatus, String orderProvider) {
        this.orderID = orderID;
        this.orderCustomer = orderCustomer;
        this.orderFoodName = orderFoodName;
        this.orderPhone = orderPhone;
        this.orderAddress = orderAddress;
        this.orderStatus = orderStatus;
        this.orderProvider = orderProvider;
    }

    public OrderFormat(String orderID, String orderFoodName, String orderStatus) {
        this.orderID = orderID;
        this.orderFoodName = orderFoodName;
        this.orderStatus = orderStatus;
    }

    public OrderFormat(String orderID, String orderFoodName, String orderPhone, String orderAddress, String orderStatus) {
        this.orderID = orderID;
        this.orderFoodName = orderFoodName;
        this.orderPhone = orderPhone;
        this.orderAddress = orderAddress;
        this.orderStatus = orderStatus;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderCustomer() {
        return orderCustomer;
    }

    public void setOrderCustomer(String orderCustomer) {
        this.orderCustomer = orderCustomer;
    }

    public String getOrderFoodName() {
        return orderFoodName;
    }

    public void setOrderFoodName(String orderFoodName) {
        this.orderFoodName = orderFoodName;
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
