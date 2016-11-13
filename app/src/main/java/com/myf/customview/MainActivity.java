package com.myf.customview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private LoadingView load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        load= (LoadingView) findViewById(R.id.loading);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
