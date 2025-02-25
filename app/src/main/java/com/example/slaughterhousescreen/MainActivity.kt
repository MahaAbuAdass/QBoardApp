package com.example.slaughterhousescreen

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DisplayMetricsHelper.enforceDisplayMetrics(this)

        setContentView(R.layout.activity_main)


    }



}