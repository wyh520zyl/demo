package com.example.viewpager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        ViewPager viewPager=findViewById(R.id.viewpager);
        FragmentManager messageFragmentManager = getSupportFragmentManager();
        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(this,viewPager, messageFragmentManager);
        viewPager.setAdapter(viewPageAdapter);
    }
}