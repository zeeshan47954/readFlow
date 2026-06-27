package com.example.adhdapp;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Locale;

public class ReadActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String message = "hello";
    private boolean isReadingAloudEnabled = false;
    ViewPager2 vw;
    TextToSpeech ttp;
    private OnBackPressedCallback onBackPressedCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        Toolbar tb = findViewById(R.id.tb);
        setSupportActionBar(tb);
        DrawerLayout drawer = findViewById(R.id.dl);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, tb, R.string.nav_open_drawer, R.string.nav_close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.navigationview);
        navigationView.setNavigationItemSelectedListener(this);
        SectionPagerAdapter pager = new SectionPagerAdapter(this);
        vw = findViewById(R.id.viewpager);
        vw.setAdapter(pager);

        TabLayout tl = findViewById(R.id.tl);

        new TabLayoutMediator(tl, vw,
                (tab, position) -> tab.setText(pager.getPageTitle(position))
        ).attach();
        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                DrawerLayout drawer = findViewById(R.id.dl);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    finish();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

        // Initialize TextToSpeech
        ttp = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    ttp.setLanguage(Locale.getDefault());
                }
            }
        });
    }

    class SectionPagerAdapter extends FragmentStateAdapter {
        public SectionPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public int getItemCount() {
            return 2;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    String j = getIntent().getStringExtra(message);
                    return new CurrentReadingfragment(j);
                case 1:
                    String w = getIntent().getStringExtra(message);
                    return new AiSummaryFragment(w);

                default:
                    return null;
            }
        }

        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "current";
                case 1:
                    return "Ai sum";

                default:
                    return null;
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intent = null;
        switch (id) {
            case R.id.home:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.audiobook:
                intent = new Intent(this, AudioActivity.class);
                startActivity(intent);
                break;
            case R.id.search:
                intent = new Intent(this, EditActivity.class);
                startActivity(intent);
                break;
            case R.id.puzzles:
                intent = new Intent(this, PuzzleActivity.class);
                startActivity(intent);
                break;
            case R.id.help:
                intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                break;
        }

        DrawerLayout drawer = findViewById(R.id.dl);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tbmenu2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id;
        switch (item.getItemId()) {
            case R.id.speak:
                String s = getIntent().getStringExtra(message);
                ttp.speak(s, TextToSpeech.QUEUE_FLUSH, null, null);
                break;
            case R.id.speaklinebyline:

                int currentPosition = vw.getCurrentItem(); // Get the current tab position
                Fragment currentFragment = getSupportFragmentManager().getFragments().get(currentPosition);
                if (currentFragment instanceof CurrentReadingfragment) {
                    CurrentReadingfragment crf = (CurrentReadingfragment) currentFragment;
                    crf.toggleSpeech();
                } else if (currentFragment instanceof AiSummaryFragment) {
                    AiSummaryFragment asf = (AiSummaryFragment) currentFragment;
                    asf.toggleSpeech();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (ttp!=null)
            ttp.stop();

    }
}
