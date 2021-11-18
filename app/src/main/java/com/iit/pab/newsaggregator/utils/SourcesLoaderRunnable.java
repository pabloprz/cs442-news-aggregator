package com.iit.pab.newsaggregator.utils;

import android.net.Uri;

import com.iit.pab.newsaggregator.MainActivity;
import com.iit.pab.newsaggregator.dto.SourceDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

public class SourcesLoaderRunnable implements Runnable {

    private final MainActivity mainActivity;
    private static final String DATA_URL = "https://newsapi.org/v2/sources";
    private static final String API_KEY = "284d18a74ba74f28bea570f2a5c05e66";

    public SourcesLoaderRunnable(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        Uri.Builder builder = Uri.parse(DATA_URL).buildUpon();

        builder.appendQueryParameter("apiKey", API_KEY);

        String urlToUse = builder.build().toString();
        StringBuilder sb = new StringBuilder();

        try {
            URL url = new URL(urlToUse);

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.addRequestProperty("User-Agent", "");

            connection.connect();

            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                handleResults(null);
            }

            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            mainActivity.runOnUiThread(mainActivity::updatingSourcesFailed);
        }

        handleResults(sb.toString());
    }

    private void handleResults(final String json) {
        if (json != null) {
            try {
                JSONObject jObjMain = new JSONObject(json);
                JSONArray sourceList = jObjMain.getJSONArray("sources");
                List<SourceDTO> sources = new ArrayList<>();
                Set<String> languages = new HashSet<>();
                Set<String> categories = new HashSet<>();
                Set<String> countries = new HashSet<>();

                for (int i = 0; i < sourceList.length(); i++) {
                    JSONObject s = sourceList.getJSONObject(i);
                    String language = s.getString("language");
                    String category = s.getString("category");
                    String country = s.getString("country");
                    SourceDTO dto = new SourceDTO(s.getString("id"), s.getString("name"),
                            language, category, country);
                    languages.add(language);
                    categories.add(category);
                    countries.add(country);
                    sources.add(dto);
                }

                mainActivity.runOnUiThread(() -> mainActivity.updatingSourcesSuccess(sources,
                        languages, categories, countries));
            } catch (JSONException e) {
                e.printStackTrace();
                mainActivity.runOnUiThread(mainActivity::updatingSourcesFailed);
            }
        } else {
            mainActivity.runOnUiThread(mainActivity::updatingSourcesFailed);
        }
    }
}
