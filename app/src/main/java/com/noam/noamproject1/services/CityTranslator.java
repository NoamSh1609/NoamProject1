package com.noam.noamproject1.services;

import android.content.Context;

import com.noam.noamproject1.R;

import java.util.Arrays;

public class CityTranslator {

    private Context context;
    private String[] hebrewCities;
    private String[] englishCities;

    public CityTranslator(Context context) {
        this.context = context;
        initializeArrays();
    }

    private void initializeArrays() {
        hebrewCities = context.getResources().getStringArray(R.array.city_names);
        englishCities = context.getResources().getStringArray(R.array.city_values);
    }

    /**
     * Converts Hebrew city name to English
     * @param hebrewCityName The city name in Hebrew
     * @return English city name, or null if not found
     */
    public String getEnglishCityName(String hebrewCityName) {
        if (hebrewCityName == null) {
            return null;
        }

        for (int i = 0; i < hebrewCities.length; i++) {
            if (hebrewCities[i].equals(hebrewCityName)) {
                if (i < englishCities.length) {
                    return englishCities[i];
                }
                break;
            }
        }
        return null;
    }

    /**
     * Converts Hebrew city name to English with fallback
     * @param hebrewCityName The city name in Hebrew
     * @param defaultValue Default value if city not found
     * @return English city name or default value
     */
    public String getEnglishCityName(String hebrewCityName, String defaultValue) {
        String result = getEnglishCityName(hebrewCityName);
        return result != null ? result : defaultValue;
    }

    /**
     * Converts English city name back to Hebrew
     * @param englishCityName The city name in English
     * @return Hebrew city name, or null if not found
     */
    public String getHebrewCityName(String englishCityName) {
        if (englishCityName == null) {
            return null;
        }

        for (int i = 0; i < englishCities.length; i++) {
            if (englishCities[i].equals(englishCityName)) {
                if (i < hebrewCities.length) {
                    return hebrewCities[i];
                }
                break;
            }
        }
        return null;
    }

    /**
     * Check if a Hebrew city name exists in our database
     * @param hebrewCityName The city name in Hebrew
     * @return true if city exists, false otherwise
     */
    public boolean isValidHebrewCity(String hebrewCityName) {
        if (hebrewCityName == null) {
            return false;
        }

        for (String city : hebrewCities) {
            if (city.equals(hebrewCityName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if an English city name exists in our database
     * @param englishCityName The city name in English
     * @return true if city exists, false otherwise
     */
    public boolean isValidEnglishCity(String englishCityName) {
        if (englishCityName == null) {
            return false;
        }

        for (String city : englishCities) {
            if (city.equals(englishCityName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get all Hebrew city names
     * @return Array of all Hebrew city names
     */
    public String[] getAllHebrewCities() {
        return Arrays.copyOf(hebrewCities, hebrewCities.length);
    }

    /**
     * Get all English city names
     * @return Array of all English city names
     */
    public String[] getAllEnglishCities() {
        return Arrays.copyOf(englishCities, englishCities.length);
    }

    /**
     * Get the total number of cities
     * @return Number of cities in the database
     */
    public int getCityCount() {
        return hebrewCities.length;
    }

    /**
     * Static helper method for quick translation without creating an instance
     * @param context The application context
     * @param hebrewCityName The city name in Hebrew
     * @return English city name, or null if not found
     */
    public static String translateToEnglish(Context context, String hebrewCityName) {
        CityTranslator translator = new CityTranslator(context);
        return translator.getEnglishCityName(hebrewCityName);
    }

    /**
     * Static helper method for quick translation with default value
     * @param context The application context
     * @param hebrewCityName The city name in Hebrew
     * @param defaultValue Default value if city not found
     * @return English city name or default value
     */
    public static String translateToEnglish(Context context, String hebrewCityName, String defaultValue) {
        CityTranslator translator = new CityTranslator(context);
        return translator.getEnglishCityName(hebrewCityName, defaultValue);
    }
}