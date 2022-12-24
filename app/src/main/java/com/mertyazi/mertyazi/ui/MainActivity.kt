package com.mertyazi.mertyazi.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mertyazi.mertyazi.R
import com.mertyazi.mertyazi.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}