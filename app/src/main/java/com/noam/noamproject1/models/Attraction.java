package com.noam.noamproject1.models;

import java.util.List;

public class Attraction {
    protected String id, name, type, city, detail, area;
    protected int capacity;
    protected double rating;
    protected double sumRate;
    protected int numRate;

    List<Review> reviews;

    String pic;

    // קונסטרוקטור שמקבל את כל הפרמטרים

    public Attraction(String id, String name, String type, String city, String detail, String area, int capacity, double rating, double sumRate, int numRate, String pic) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.city = city;
        this.detail = detail;
        this.area = area;
        this.capacity = capacity;
        this.rating = rating;
        this.sumRate = sumRate;
        this.numRate = numRate;
        this.pic = pic;
    }

    // קונסטרוקטור שמקבל רק את המידע החיוני (כמו בקוד שלך)
    public Attraction(String name, String detail, int capacity, String city, String type, String area) {
        this.name = name;
        this.detail = detail;
        this.capacity = capacity;
        this.city = city;
        this.type = type;
        this.area = area;
        this.rating = 0.0; // Default value
        this.sumRate = 0.0; // Default value
        this.numRate = 0; // Default value
    }

    // קונסטרוקטור ריק
    public Attraction() {
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
                ", rating=" + rating +
                ", sumRate=" + sumRate +
                ", numRate=" + numRate +
                '}';
    }
}
