package com.example.constraintspace;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 实验2-4 约束布局（二）：太空旅行界面。
 * 界面由 ConstraintLayout 搭建，此处仅负责加载布局。
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
