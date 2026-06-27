package com.example.adhdapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class PuzzleActivity extends AppCompatActivity implements levels.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        Toolbar tb=findViewById(R.id.tb);
        setSupportActionBar(tb);
    }

    @Override
    public void itemClicked(long id)
    {
        Intent intent=new Intent(this,LevelActivity.class);

intent.putExtra(LevelActivity.wid,(int)id);
startActivity(intent);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

}