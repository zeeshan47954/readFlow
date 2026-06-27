package com.example.adhdapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class LevelActivity extends AppCompatActivity {
    public static final String wid="hello";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        int id=(int)getIntent().getExtras().get(wid);

        // Get the FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Create a FragmentTransaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Create a new instance of the corresponding level fragment
        Fragment levelFragment = null;
        switch (id) {
            case 0:
                levelFragment = new level1();
                break;
            case 1:
                levelFragment = new level2();
                break;
            case 2:
                levelFragment = new level3();
                break;
            case 3:
                levelFragment = new level4();
                break;
            case 4:
                levelFragment = new level5();
                break;



            // Add more cases for level 4 to 10
            default:
                // Handle the default case, e.g., show an error message
                break;
        }

        // Replace the fragment
        if (levelFragment != null) {
            fragmentTransaction.replace(R.id.fragment_container, levelFragment);
            fragmentTransaction.commit();
        }
    }
}