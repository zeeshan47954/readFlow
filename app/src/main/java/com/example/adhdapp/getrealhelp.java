package com.example.adhdapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class getrealhelp extends AppCompatActivity {
    public static final String message="hello";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getrealhelp);
        int s=(int)getIntent().getExtras().get(message);
        WebView webView = findViewById(R.id.webView);

        // Enable JavaScript (if needed)
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Set WebViewClient to ensure links open within the WebView
        webView.setWebViewClient(new WebViewClient());
switch(s)
        {
            case 0:
            webView.loadUrl("https://www.talkspace.com/mental-health/conditions/attention-deficit-hyperactivity-disorder/");
break;
            case 1:
                webView.loadUrl("https://www.betterhelp.com/advice/adhd/");
                break;
            case 2:
                webView.loadUrl("https://www.samhsa.gov/mental-health/attention-deficit-hyperactivity-disorder");
                break;
            case 3:
                webView.loadUrl("https://www.verywellmind.com/adhd-overview-4581801");
                break;
            case 4:
                webView.loadUrl("https://psychcentral.com/adhd/adhd-overview");
                break;
            case 5:
                webView.loadUrl("https://www.helpguide.org/home-pages/add-adhd.htm");
                break;
        }
    }
}