package com.example.adhdapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class HelpActivity extends AppCompatActivity implements Showcardview2.Listener1{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar tb=findViewById(R.id.tb);
        setSupportActionBar(tb);


    }
    @Override
    public  void itemclicked(int position)
    {
Intent i=new Intent(this,getrealhelp.class);
        i.putExtra(getrealhelp.message,position);
        startActivity(i);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainbar,menu);
        return super.onCreateOptionsMenu(menu);
    }
}