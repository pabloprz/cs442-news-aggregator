package com.iit.pab.newsaggregator;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.iit.pab.newsaggregator.dto.ArticleDTO;
import com.iit.pab.newsaggregator.dto.CountryDTO;
import com.iit.pab.newsaggregator.dto.LanguageDTO;
import com.iit.pab.newsaggregator.dto.SourceDTO;
import com.iit.pab.newsaggregator.utils.ArticlesLoaderRunnable;
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
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private ArrayAdapter<String> drawerAdapter;
    private ArticleAdapter articleAdapter;
    private ViewPager2 viewPager;

    private ArrayList<SourceDTO> sources = new ArrayList<>();
    private List<SourceDTO> filteredSources = new ArrayList<>();
    private ArrayList<LanguageDTO> languages = new ArrayList<>();
    private ArrayList<String> categories = new ArrayList<>();
    private ArrayList<CountryDTO> countries = new ArrayList<>();
    private Map<String, String> fullCountries = new HashMap<>();
    private Map<String, String> fullLanguages = new HashMap<>();
    private ArrayList<ArticleDTO> articles = new ArrayList<>();

    private String selectedLanguage;
    private String selectedCategory;
    private String selectedCountry;
    private SourceDTO selectedSource;

    private static final int CATEGORIES_ID = 0;
    private static final int LANGUAGES_ID = 1;
    private static final int COUNTRIES_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerList = findViewById(R.id.left_drawer);
        drawerList.setOnItemClickListener((parent, view, position, id) -> topicSelected(position));
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer,
                R.string.close_drawer);

        articleAdapter = new ArticleAdapter(this, articles);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(articleAdapter);

        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Drawer logic
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

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

    public void updatingSourcesSuccess(ArrayList<SourceDTO> sources, Set<String> languages,
                                       Set<String> categories, Set<String> countries) {
        this.sources = sources;
        this.categories =
                categories.stream().sorted().collect(Collectors.toCollection(ArrayList::new));
        this.languages =
                languages.stream().map(l -> new LanguageDTO(l, fullLanguages.get(l.toUpperCase())))
                        .sorted().collect(Collectors.toCollection(ArrayList::new));
        this.countries =
                countries.stream().map(c -> new CountryDTO(c, fullCountries.get(c.toUpperCase())))
                        .sorted().collect(Collectors.toCollection(ArrayList::new));

        createSubMenu(CATEGORIES_ID, getString(R.string.topics), this.categories);
        createSubMenu(LANGUAGES_ID, getString(R.string.languages),
                this.languages.stream().map(LanguageDTO::getName).collect(Collectors.toList()));
        createSubMenu(COUNTRIES_ID, getString(R.string.countries),
                this.countries.stream().map(CountryDTO::getName).collect(Collectors.toList()));

        filterSources();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void fetchingArticlesSuccess(List<ArticleDTO> articles) {
        this.articles.clear();
        this.articles.addAll(articles);
        articleAdapter.notifyDataSetChanged();

        if (this.articles.isEmpty()) {
            Toast.makeText(this, String.format(Locale.getDefault(), "No articles found for %s",
                    selectedSource.getName()), Toast.LENGTH_SHORT).show();
            return;
        }

        viewPager.setBackground(null);
        viewPager.setCurrentItem(0);

        setTitle(selectedSource.getName());
    }

    public void receiveFullCountries(Map<String, String> countries) {
        fullCountries = countries;

        if (fullCountries == null) {
            // TODO Show error
        }
    }

    public void receiveFullLanguages(Map<String, String> languages) {
        fullLanguages = languages;

        if (fullLanguages == null) {
            // TODO Show error
        }
    }

    public void updatingSourcesFailed() {
        // TODO do something
    }

    public void fetchingArticlesFailed() {
        // TODO do something
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(getString(R.string.selected_category), selectedCategory);
        outState.putString(getString(R.string.selected_language), selectedLanguage);
        outState.putString(getString(R.string.selected_country), selectedCountry);

        outState.putStringArrayList(getString(R.string.categories), categories);
        outState.putParcelableArrayList(getString(R.string.languages), languages);
        outState.putParcelableArrayList(getString(R.string.countries), countries);
        outState.putParcelableArrayList(getString(R.string.sources), sources);

        outState.putParcelableArrayList("articles", articles);
        outState.putInt("currentArticle", viewPager.getCurrentItem());

        super.onSaveInstanceState(outState);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        selectedCategory = savedInstanceState.getString(getString(R.string.selected_category));
        selectedLanguage = savedInstanceState.getString(getString(R.string.selected_language));
        selectedCountry = savedInstanceState.getString(getString(R.string.selected_country));

        categories = savedInstanceState.getStringArrayList(getString(R.string.categories));
        languages = savedInstanceState.getParcelableArrayList(getString(R.string.languages));
        countries = savedInstanceState.getParcelableArrayList(getString(R.string.countries));
        sources = savedInstanceState.getParcelableArrayList(getString(R.string.sources));

        articles.addAll(savedInstanceState.getParcelableArrayList("articles"));
        articleAdapter.notifyDataSetChanged();

        viewPager.setBackground(null);
        viewPager.setCurrentItem(savedInstanceState.getInt("currentArticle"));

        // TODO
//        setTitle(selectedSource.getName());

        filterSources();
    }

    private void topicSelected(int position) {
        selectedSource = filteredSources.get(position);
        if (this.selectedSource != null) {
            new Thread(new ArticlesLoaderRunnable(selectedSource.getId(), this)).start();
            drawerLayout.closeDrawer(drawerList);
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
            showNoSourcesDialog();
        }

        drawerAdapter = new ArrayAdapter<>(this, R.layout.drawer_list_item,
                filteredSources.stream().map(SourceDTO::getName).collect(Collectors.toList()));
        drawerList.setAdapter(drawerAdapter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void showNoSourcesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("No sources available");
        builder.setMessage(
                "No sources exist matching the specified Topic, Language and/or Country");

        AlertDialog dialog = builder.create();
        dialog.show();
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
