package com.iit.pab.newsaggregator;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.iit.pab.newsaggregator.dto.CountryDTO;
import com.iit.pab.newsaggregator.dto.LanguageDTO;
import com.iit.pab.newsaggregator.dto.SourceDTO;
import com.iit.pab.newsaggregator.utils.ConnectionUtils;
import com.iit.pab.newsaggregator.utils.CountriesLoader;
import com.iit.pab.newsaggregator.utils.LanguagesLoader;
import com.iit.pab.newsaggregator.utils.SourcesLoaderRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private Menu menu;
    private List<SourceDTO> sources = new ArrayList<>();
    private List<SourceDTO> filteredSources = new ArrayList<>();
    private List<LanguageDTO> languages = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    private List<CountryDTO> countries = new ArrayList<>();
    private Map<String, String> fullCountries = new HashMap<>();
    private Map<String, String> fullLanguages = new HashMap<>();

    private String selectedLanguage;
    private String selectedCategory;
    private String selectedCountry;

    private static final int CATEGORIES_ID = 0;
    private static final int LANGUAGES_ID = 1;
    private static final int COUNTRIES_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.hasSubMenu()) {
            return true;
        }

        if (item.getGroupId() == CATEGORIES_ID) {
            selectedCategory = item.getItemId() > 0 ? categories.get(item.getItemId() - 1) : null;
        } else if (item.getGroupId() == LANGUAGES_ID) {
            selectedLanguage =
                    item.getItemId() > 0 ? languages.get(item.getItemId() - 1).getCode() : null;
        } else {
            selectedCountry =
                    item.getItemId() > 0 ? countries.get(item.getItemId() - 1).getCode() : null;
        }

        filterSources();

        return super.onOptionsItemSelected(item);
    }

    public void updatingSourcesSuccess(List<SourceDTO> sources, Set<String> languages,
                                       Set<String> categories, Set<String> countries) {
        this.sources = sources;
        this.categories = categories.stream().sorted().collect(Collectors.toList());
        this.languages =
                languages.stream().map(l -> new LanguageDTO(l, fullLanguages.get(l.toUpperCase())))
                        .sorted().collect(Collectors.toList());
        this.countries =
                countries.stream().map(c -> new CountryDTO(c, fullCountries.get(c.toUpperCase())))
                        .sorted().collect(Collectors.toList());

        createSubMenu(CATEGORIES_ID, getString(R.string.topics), this.categories);
        createSubMenu(LANGUAGES_ID, getString(R.string.languages),
                this.languages.stream().map(LanguageDTO::getName).collect(Collectors.toList()));
        createSubMenu(COUNTRIES_ID, getString(R.string.countries),
                this.countries.stream().map(CountryDTO::getName).collect(Collectors.toList()));
        filterSources();
    }

    public void updatingSourcesFailed() {
        // TODO do something
    }

    public void receiveFullCountries(Map<String, String> countries) {
        fullCountries = countries;

        if (fullCountries == null) {
            // Show error
        }
    }

    public void receiveFullLanguages(Map<String, String> languages) {
        fullLanguages = languages;

        if (fullLanguages == null) {
            // Show error
        }
    }

    private void filterSources() {
        filteredSources = sources.stream()
                .filter(s -> selectedLanguage == null || s.getLanguage().equals(selectedLanguage))
                .filter(s -> selectedCategory == null || s.getCategory().equals(selectedCategory))
                .filter(s -> selectedCountry == null || s.getCountry().equals(selectedCountry))
                .collect(Collectors.toList());
        setTitle(String.format(Locale.getDefault(), "%s (%d)", getString(R.string.app_name),
                filteredSources.size()));

        if (filteredSources.size() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("No sources available");
            builder.setMessage(
                    "No sources exist matching the specified Topic, Language and/or Country");

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void loadData() {
        new Thread(new LanguagesLoader(this)).start();
        new Thread(new CountriesLoader(this)).start();

        if (ConnectionUtils.hasNetworkConnection(this)) {
            new Thread(new SourcesLoaderRunnable(this)).start();
        } else {
            Toast.makeText(this, getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }
    }

    private void createSubMenu(int groupId, String groupName, List<String> values) {
        SubMenu subMenu = menu.addSubMenu(groupName);
        subMenu.add(groupId, 0, 0, getString(R.string.all));
        for (int i = 1; i <= values.size(); i++) {
            subMenu.add(groupId, i, i, values.get(i - 1));
        }
    }
}
