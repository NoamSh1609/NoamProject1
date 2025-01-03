package com.noam.noamproject1.models;

public class Attraction {
    protected String id,name, type, city,detail,area;
    protected int capcity;
    protected double rating;
    protected  double sumRate;
    protected  int numRate;

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


    public Attraction(String id, String name, String type, String city, String detail, String area, int capcity, double rating, double sumRate, int numRate) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.city = city;
        this.detail = detail;
        this.area = area;
        this.capcity = capcity;
        this.rating = rating;
        this.sumRate = sumRate;
        this.numRate = numRate;
    }

    public Attraction( int capcity) {
        this.capcity=capcity;

    }


    public Attraction() {
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    @Override
    public String toString() {
        return "Attraction{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", city='" + city + '\'' +
                ", detail='" + detail + '\'' +
                ", area='" + area + '\'' +
                ", capcity=" + capcity +
                ", rating=" + rating +
                ", sumRate=" + sumRate +
                ", numRate=" + numRate +
                '}';
    }
}
