package com.ayeshaazeema.newsapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ayeshaazeema.newsapp.R

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportActionBar?.hide()
    }
}