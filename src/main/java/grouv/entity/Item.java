/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grouv.entity;

/**
 *
 * @author Geo
 */
public class Item {
    
    private Product product;
    private int quantity;
    private float quality;

    public Item(Product product, int quantity, float quality) {
        this.product = product;
        this.quantity = quantity;
        this.quality = quality;
    }

    public Item() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getQuality() {
        return quality;
    }

    public void setQuality(float quality) {
        this.quality = quality;
    }
    
    
}
