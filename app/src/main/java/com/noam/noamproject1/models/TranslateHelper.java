package com.noam.noamproject1.models;

import android.os.StrictMode;
import android.util.Log;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TranslateHelper {

    private static final String API_URL = "https://translate.argosopentech.com/translate";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static String translateToEnglish(String hebrewText) {
        if (hebrewText == null || hebrewText.trim().isEmpty()) {
            return hebrewText;
        }

        try {
            // Configure network policy
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            // Create JSON request body
            JSONObject jsonBody = new JSONObject()
                .put("q", hebrewText)
                .put("source", "iw")       // Hebrew language code
                .put("target", "en")       // English language code
                .put("format", "text");

            // Create request
            RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
            Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

            // Execute request
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();

            if (response.isSuccessful() && response.body() != null) {
                String responseStr = response.body().string();
                Log.d("TranslateHelper", "Response: " + responseStr); // Debug log
                JSONObject jsonResponse = new JSONObject(responseStr);
                String translatedText = jsonResponse.getString("translatedText");
                Log.d("TranslateHelper", "Translated text: " + translatedText); // Debug log
                return translatedText;
            } else {
                Log.e("TranslateHelper", "Translation failed. Response code: " + response.code());
                if (response.body() != null) {
                    Log.e("TranslateHelper", "Error body: " + response.body().string());
                }
            }
        } catch (Exception e) {
            Log.e("TranslateHelper", "Translation error", e);
        }

        return hebrewText;
    }
}
