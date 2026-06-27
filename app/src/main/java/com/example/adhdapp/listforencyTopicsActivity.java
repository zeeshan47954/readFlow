package com.example.adhdapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class listforencyTopicsActivity extends AppCompatActivity implements  listOfencyTopics.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listforency_topics);
    }

    @Override
   public void itemClicked(long id)
    {
        String str = String.valueOf(id);
        Intent i=new Intent(this,ReadActivityforonlineone.class);
        i.putExtra(ReadActivityforonlineone.message,str);
        startActivity(i);

    }
}