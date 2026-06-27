package com.example.adhdapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AudioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

}

    public void adhd1(View view)
    {
Intent I=new Intent(this,listenToaudios.class);
I.putExtra(listenToaudios.message,"1");
startActivity(I);

    }
    public void adhd2(View view)
    {
        Intent I=new Intent(this,listenToaudios.class);
        I.putExtra(listenToaudios.message,"2");
        startActivity(I);

    }

    public void adhd3(View view)
    {Intent I=new Intent(this,listenToaudios.class);
        I.putExtra(listenToaudios.message,"3");
        startActivity(I);


    }


}



