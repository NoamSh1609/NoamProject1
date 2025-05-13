package com.noam.noamproject1.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class WeatherApiService {

    private static final String API_KEY = "e9a3f3651f8940458ba70137250905"; // 🔑 Replace with your WeatherAPI key
    private static final String BASE_URL = "https://api.weatherapi.com/v1";

    /**
     * Fetches current weather data for the given location.
     *
     * @param location City name or coordinates (e.g. "London" or "37.7749,-122.4194")
     * @return JSON response as a String
     * @throws Exception on HTTP or IO error
     */
    public String getCurrentWeather(String location) throws Exception {
        String endpoint = "/current.json";
        String query = String.format("key=%s&q=%s",
                URLEncoder.encode(API_KEY, "UTF-8"),
                URLEncoder.encode(location, "UTF-8"));

        URL url = new URL(BASE_URL + endpoint + "?" + query);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();

        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("HTTP GET Request Failed with Error code: " + responseCode + conn.getResponseMessage());
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
        return response.toString(); // raw JSON response
    }

}
