package com.iit.pab.newsaggregator.utils;

import com.iit.pab.newsaggregator.MainActivity;
import com.iit.pab.newsaggregator.R;
import com.iit.pab.newsaggregator.dto.CountryDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CountriesNamesLoader implements Runnable {

    private final MainActivity activity;

    public CountriesNamesLoader(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void run() {
        InputStream is = activity.getResources().openRawResource(R.raw.country_codes);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        try {
            StringBuilder result = new StringBuilder();
            for (String line; (line = reader.readLine()) != null; ) {
                result.append(line);
            }

            List<CountryDTO> countryList = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(result.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                countryList.add(new CountryDTO(jsonObject.getString("code"),
                        jsonObject.getString("name")));
            }
            activity.runOnUiThread(() -> activity.receiveFullCountries(countryList));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            activity.runOnUiThread(() -> activity.receiveFullCountries(null));
        }
    }
}
