package com.iit.pab.newsaggregator.utils;

import android.net.Uri;

import com.iit.pab.newsaggregator.MainActivity;
import com.iit.pab.newsaggregator.dto.ArticleDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class ArticlesLoaderRunnable implements Runnable {

    private final String sourceId;
    private final MainActivity mainActivity;
    private static final String DATA_URL = "https://newsapi.org/v2/top-headlines";
    private static final String API_KEY = "284d18a74ba74f28bea570f2a5c05e66";

    public ArticlesLoaderRunnable(String sourceId, MainActivity mainActivity) {
        this.sourceId = sourceId;
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        Uri.Builder builder = Uri.parse(DATA_URL).buildUpon();

        builder.appendQueryParameter("apiKey", API_KEY);
        builder.appendQueryParameter("sources", sourceId);

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
                JSONArray articleList = jObjMain.getJSONArray("articles");
                List<ArticleDTO> articles = new ArrayList<>();

                for (int i = 0; i < articleList.length(); i++) {
                    JSONObject a = articleList.getJSONObject(i);

                    String publishedAtStr = a.has("publishedAt") && !a.isNull("publishedAt") ? a
                            .getString("publishedAt") : null;

                    ArticleDTO article =
                            new ArticleDTO(getStringField(a, "author"), getStringField(a, "title"),
                                    getStringField(a, "description") + getStringField(a,
                                            "description") + getStringField(a,
                                            "description") + getStringField(a, "description"),
                                    getStringField(a, "url"), getStringField(a, "urlToImage"),
                                    DateTimeUtils.parseDate(publishedAtStr));
                    articles.add(article);
                }

                mainActivity.runOnUiThread(() -> mainActivity.fetchingArticlesSuccess(articles));
            } catch (JSONException e) {
                e.printStackTrace();
                mainActivity.runOnUiThread(mainActivity::updatingSourcesFailed);
            }
        } else {
            mainActivity.runOnUiThread(mainActivity::fetchingArticlesFailed);
        }
    }

    private String getStringField(JSONObject obj, String field) throws JSONException {
        return obj.has(field) && !obj.isNull(field) ? obj.getString(field) : null;
    }
}
