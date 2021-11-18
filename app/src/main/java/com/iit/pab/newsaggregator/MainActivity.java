package com.iit.pab.newsaggregator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.iit.pab.newsaggregator.dto.SourceDTO;
import com.iit.pab.newsaggregator.utils.ConnectionUtils;
import com.iit.pab.newsaggregator.utils.SourcesLoaderRunnable;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Menu menu;
    private List<SourceDTO> sources = new ArrayList<>();
    private List<String> languages = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    private List<String> countries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadSources();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    public void updatingSourcesFailed() {
        // TODO do something
    }

    private void loadSources() {
        if (ConnectionUtils.hasNetworkConnection(this)) {
            new Thread(new SourcesLoaderRunnable(this, sources, categories, languages, countries)).start();
        } else {
            Toast.makeText(this, getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }
    }
}
