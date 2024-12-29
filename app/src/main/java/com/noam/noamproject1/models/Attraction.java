package com.noam.noamproject1.models;

public class Attraction {
    protected String id,name, type, city,detail,area;
    protected int capcity;
    protected double rating;

    public String getdetail() {
        return detail;
    }

    public void setdetail(String detail) {
        this.detail = detail;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Attraction(String id, String name, String type, String city, int capcity, double rating, String detail, String area) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.city = city;
        this.capcity = capcity;
        this.rating = rating;
        this.detail=detail;
        this.area=area;
    }

    public Attraction(int capcity) {

    }

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

    public int getCapcity() {
        return capcity;
    }

    public void setCapcity(int capcity) {
        this.capcity = capcity;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Attraction{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", city='" + city + '\'' +
                ", capcity=" + capcity +
                ", rating=" + rating +
                ",detail="+detail+
                ",detail="+area+

                '}';
    }
}
