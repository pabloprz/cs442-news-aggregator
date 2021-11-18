package com.iit.pab.newsaggregator;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.iit.pab.newsaggregator.dto.CountryDTO;
import com.iit.pab.newsaggregator.dto.SourceDTO;
import com.iit.pab.newsaggregator.utils.ConnectionUtils;
import com.iit.pab.newsaggregator.utils.CountriesNamesLoader;
import com.iit.pab.newsaggregator.utils.SourcesLoaderRunnable;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Menu menu;
    private List<SourceDTO> sources = new ArrayList<>();
    private List<String> languages = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    private List<String> countries = new ArrayList<>();
    private List<CountryDTO> fullCountries = new ArrayList<>();

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

        Toast.makeText(this, String.format("Parent %d and child %s selected", item.getGroupId(),
                item.getItemId()), Toast.LENGTH_SHORT).show();

        return super.onOptionsItemSelected(item);
    }

    public void updatingSourcesSuccess() {
        menu.clear();
        createSubMenu(CATEGORIES_ID, getString(R.string.topics), categories);
        createSubMenu(LANGUAGES_ID, getString(R.string.languages), languages);
        createSubMenu(COUNTRIES_ID, getString(R.string.countries), countries);
    }

    public void updatingSourcesFailed() {
        // TODO do something
    }

    public void receiveFullCountries(List<CountryDTO> countries) {
        this.fullCountries = countries;
    }

    private void loadData() {
        new Thread(new CountriesNamesLoader(this)).start();

        if (ConnectionUtils.hasNetworkConnection(this)) {
            new Thread(new SourcesLoaderRunnable(this, sources, categories, languages, countries)).start();
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
