package com.example.hp.jbooks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewpdf extends AppCompatActivity {
String link="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            link = extras.getString("link");
            //The key argument here must match that used in the other activity
        }
        android.webkit.WebView mywebview = (android.webkit.WebView) findViewById(R.id.webView);
        mywebview.loadUrl(link);
    }
}
