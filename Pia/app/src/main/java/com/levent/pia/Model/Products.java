package com.levent.pia.Model;

public class Products {

    private String description, image, name, price,open;
    Long order;


    public Products()
    {

    }

    public Products(String description, String image, String name, String price, String open, Long order) {
        this.description = description;
        this.image = image;
        this.name = name;
        this.price = price;
        this.open = open;
        this.order = order;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }
}
