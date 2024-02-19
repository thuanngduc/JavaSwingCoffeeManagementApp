/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author ADMIN
 */
public class Product {
    private Long id;
    private String itemName;
    private String description;
    private Double price;
    private ProductType productType;
    private String imageUrl;

    public Product() {
    }

    public Product(Long id, String itemName, String description, Double price, ProductType productType, String imageUrl) {
        this.id = id;
        this.itemName = itemName;
        this.description = description;
        this.price = price;
        this.productType = productType;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", itemName=" + itemName + ", description=" + description + ", price=" + price + ", productType=" + productType + ", imageUrl=" + imageUrl + '}';
    }
    
    
}
