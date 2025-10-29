package edu.infosys.lostAndFoundApplication.bean;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class LostItem {

    @Id
    private String lostItemId;
    private String username;
    private String userEmail;
    private String itemName;
    private String category;
    private String color;
    private String brand;
    private String location;
    private String lostDate;

    @Column(length = 512)
    private String imageUrl;

    private boolean status; // Added status field

    public LostItem() {
        super();
        this.status = false; // Default status to false (not found)
    }

    public LostItem(String lostItemId, String username, String userEmail, String itemName, String category, String color, String brand, String location, String lostDate, String imageUrl, boolean status) {
        this.lostItemId = lostItemId;
        this.username = username;
        this.userEmail = userEmail;
        this.itemName = itemName;
        this.category = category;
        this.color = color;
        this.brand = brand;
        this.location = location;
        this.lostDate = lostDate;
        this.imageUrl = imageUrl;
        this.status = status; // Initialize status
    }

    // Getters and Setters
    public String getLostItemId() { return lostItemId; }
    public void setLostItemId(String lostItemId) { this.lostItemId = lostItemId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getLostDate() { return lostDate; }
    public void setLostDate(String lostDate) { this.lostDate = lostDate; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    // Getter and Setter for status
    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
}