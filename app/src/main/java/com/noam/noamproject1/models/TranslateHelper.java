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

    public static String translateToEnglish(String hebrewText) {
        try {
            // מאפשר ריצה ברשת בת'רד הראשי (רק לבדיקה)
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("q", hebrewText);      // הטקסט לתרגום
            jsonBody.put("source", "he");       // שפת מקור - עברית
            jsonBody.put("target", "en");       // שפת יעד - אנגלית
            jsonBody.put("format", "text");

            RequestBody body = RequestBody.create(
                    jsonBody.toString(),
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url("https://libretranslate.de/translate")
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String responseStr = response.body().string();
                JSONObject jsonResponse = new JSONObject(responseStr);
                return jsonResponse.getString("translatedText");
            } else {
                Log.e("TranslateError", "Response code: " + response.code());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // אם התרגום נכשל, מחזיר את הטקסט המקורי
        return hebrewText;
    }
}
