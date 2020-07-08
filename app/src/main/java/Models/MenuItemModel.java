package Models;

public class MenuItemModel {

    private String ItemName;
    private String ItemPrice;
    private String ItemSpecification;

    public MenuItemModel(String itemName, String itemPrice, String itemSpecification) {
        ItemName = itemName;
        ItemPrice = itemPrice;
        ItemSpecification = itemSpecification;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemPrice() {
        return ItemPrice;
    }

    public void setItemPrice(String itemPrice) {
        ItemPrice = itemPrice;
    }

    public String getItemSpecification() {
        return ItemSpecification;
    }

    public void setItemSpecification(String itemSpecification) {
        ItemSpecification = itemSpecification;
    }

}
