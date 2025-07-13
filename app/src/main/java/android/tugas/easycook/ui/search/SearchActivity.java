package android.tugas.easycook.ui.search;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.tugas.easycook.R;

public class SearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (savedInstanceState == null) {
            SearchFragment searchFragment = new SearchFragment();
            // Teruskan argumen dari intent ke fragment
            searchFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, searchFragment)
                    .commit();
        }
    }
}