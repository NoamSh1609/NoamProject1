package com.noam.noamproject1.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Attraction implements Serializable {
    protected String id, name, type, city, detail, area;
    protected int capacity;
    protected int currentVisitors; // מספר המבקרים הנוכחי
    protected double rating;
    protected double sumRate;
    protected int numRate;

    Map<String, Review> reviews;

    String pic;

    // קונסטרוקטור שמקבל את כל הפרמטרים

    public Attraction(String id, String name, String type, String city, String detail, String area, int capacity, double rating, double sumRate, int numRate, String pic, Map<String, Review> reviews) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.city = city;
        this.detail = detail;
        this.area = area;
        this.capacity = capacity;
        this.currentVisitors = 0; // מאתחל את מספר המבקרים ל-0
        this.rating = rating;
        this.sumRate = sumRate;
        this.numRate = numRate;
        this.pic = pic;
        this.reviews = reviews;
    }


    // קונסטרוקטור ריק
    public Attraction() {
        this.currentVisitors = 0;
        this.reviews = new HashMap<>();
    }

    // Getter ו- Setter לכל שדה
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getSumRate() {
        return sumRate;
    }

    public void setSumRate(double sumRate) {
        this.sumRate = sumRate;
    }

    public int getNumRate() {
        return numRate;
    }

    public void setNumRate(int numRate) {
        this.numRate = numRate;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getCurrentVisitors() {
        return currentVisitors;
    }

    public void setCurrentVisitors(int currentVisitors) {
        this.currentVisitors = currentVisitors;
    }

    public Map<String, Review> getReviews() {
        return reviews;
    }

    public void setReviews(Map<String, Review> reviews) {
        this.reviews = reviews;
    }

    // מתודה להוספת מבקר
    public boolean addVisitor() {
        if (currentVisitors < capacity) {
            currentVisitors++;
            return true;
        }
        return false;
    }

    // מתודה להורדת מבקר
    public boolean removeVisitor() {
        if (currentVisitors > 0) {
            currentVisitors--;
            return true;
        }
        return false;
    }

    // מתודה לבדיקה האם יש מקום לעוד מבקרים
    public boolean hasAvailableSpace() {
        return currentVisitors < capacity;
    }

    // מתודה לקבלת אחוז התפוסה
    public double getOccupancyPercentage() {
        if (capacity == 0) return 0;
        return (currentVisitors * 100.0) / capacity;
    }

    @Override
    public String toString() {
        return "Attraction{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", city='" + city + '\'' +
                ", detail='" + detail + '\'' +
                ", area='" + area + '\'' +
                ", capacity=" + capacity +
                ", currentVisitors=" + currentVisitors +
                ", rating=" + rating +
                ", sumRate=" + sumRate +
                ", numRate=" + numRate +
                '}';
    }
}
