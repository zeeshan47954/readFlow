package com.example.adhdapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;
import android.widget.VideoView;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity implements cardviewfragment.Listener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
Toolbar tb=findViewById(R.id.tb);
setSupportActionBar(tb);

           }

@Override
public void itemclicked(int id)
{
//
    switch(id)
    {
        case 0:
            Intent i=new Intent(this,EditActivity.class);
            startActivity(i);
break;
        case 1:
            Intent b=new Intent(this,PuzzleActivity.class);
            startActivity(b);
            break;

        case 2:
            Intent intent1=new Intent(this,AudioActivity.class);
            startActivity(intent1);
            break;


        case 3:
            Intent intent=new Intent(this,HelpActivity.class);
            startActivity(intent);
            break;


    }


}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.mainbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onResume()
{
    super.onResume();


}


}