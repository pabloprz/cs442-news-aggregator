package com.iit.pab.newsaggregator.utils;

import com.iit.pab.newsaggregator.MainActivity;
import com.iit.pab.newsaggregator.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ColorsLoader implements Runnable {

    private final MainActivity activity;

    public ColorsLoader(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void run() {
        InputStream is = activity.getResources().openRawResource(R.raw.colors);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        try {
            StringBuilder result = new StringBuilder();
            for (String line; (line = reader.readLine()) != null; ) {
                result.append(line);
            }

            ArrayList<String> colors = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(result.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                colors.add(jsonObject.getString("color"));
            }
            activity.runOnUiThread(() -> activity.receiveColors(colors));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            activity.runOnUiThread(() -> activity.receiveColors(null));
        }
    }
}
