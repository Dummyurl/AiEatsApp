package comp5216.sydney.edu.au.aieatsapp;

public class foodFormat {
    public String foodName;
    public String foodPrice;
    public String foodDescription;
    public String foodProvider;

    public foodFormat(String foodName, String foodPrice, String foodDescription) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodDescription = foodDescription;
    }

    public foodFormat(String foodName, String foodPrice, String foodDescription, String foodProvider) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodDescription = foodDescription;
        this.foodProvider = foodProvider;
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
