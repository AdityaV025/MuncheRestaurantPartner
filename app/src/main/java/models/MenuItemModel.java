package models;


public class MenuItemModel {

    private String name;
    private String price;
    private String specification;
    private String is_active;
    private String category;

    public MenuItemModel() {
    }

    public MenuItemModel(String name, String price, String specification, String is_active, String category) {
        this.name = name;
        this.price = price;
        this.specification = specification;
        this.is_active = is_active;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
