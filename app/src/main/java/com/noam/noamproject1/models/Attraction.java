package com.noam.noamproject1.models;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.noam.noamproject1.screens.AttractionListActivity;
import com.noam.noamproject1.services.CityTranslator;
import com.noam.noamproject1.services.WeatherApiService;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Attraction implements Serializable {
    protected String id, name, type, city, detail, area;
    protected int capacity;
    protected int currentVisitors; // מספר המבקרים הנוכחי
    protected String temp; // Temperature field

    List<Comment> comments;

    String pic;


    public Attraction() {
        comments = new ArrayList<>();
    }

    public Attraction(String id, String name, String type, String city, String detail,
                      String area, int capacity, int currentVisitors,
                      String temp, List<Comment> comments, String pic) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.city = city;
        this.detail = detail;
        this.area = area;
        this.capacity = capacity;
        this.currentVisitors = currentVisitors;
        this.temp = temp;
        this.comments = comments;
        this.pic = pic;
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

    public int getCurrentVisitors() {
        return currentVisitors;
    }

    public void setCurrentVisitors(int currentVisitors) {
        this.currentVisitors = currentVisitors;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public List<Comment> getComments() {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    @NonNull
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
                ", temp='" + temp + '\'' +
                ", comments=" + comments +
//                ", pic='" + pic + '\'' +
                '}';
    }


    public double getAvgRating() {
        if (this.comments.isEmpty()) return 0;

        int count = this.comments.size();
        double total = 0;
        for (Comment comment : this.comments) {
            total += comment.getRating();
        }
        return total / count;
    }


    private static WeatherApiService weatherApiService = new WeatherApiService();
    private static ExecutorService executorService = Executors.newFixedThreadPool(4);
    public void getWeatherTemp(Context context, Consumer<Double> runnable) {
        executorService.execute(() -> {
            try {
                String cityName = this.getCity();
                Log.d("ShowAttractionsActivity", "Original city name: " + cityName);

                // Translate city name to English
                String englishCityName = CityTranslator.translateToEnglish(context, cityName);
                Log.d("ShowAttractionsActivity", "Translated city name: " + englishCityName);

                if (englishCityName.equals(cityName)) {
                    Log.w("ShowAttractionsActivity", "Translation might have failed - original and translated names are identical");
                }

                String weatherData = weatherApiService.getCurrentWeather(englishCityName);
                Log.d("ShowAttractionsActivity", "Weather data received: " + weatherData);

                JSONObject json = new JSONObject(weatherData);
                JSONObject current = json.getJSONObject("current");
                Log.d("ShowAttractionsActivity", "Weather data received current: " + current);
                double tempC = current.getDouble("temp_c");

                runnable.accept(tempC);
            } catch (Exception e) {
                Log.e("ShowAttractionsActivity", "Error fetching weather for " + this.getCity(), e);
                runnable.accept(null);
            }
        });
    }

}
