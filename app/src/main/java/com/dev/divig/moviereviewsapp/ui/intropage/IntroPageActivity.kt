package com.dev.divig.moviereviewsapp.ui.intropage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dev.divig.moviereviewsapp.R

class IntroPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_page)
        supportActionBar?.hide()
    }
}