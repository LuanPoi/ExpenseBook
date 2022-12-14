package com.example.expensebook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.expensebook.data.LocalDatabase
import com.example.expensebook.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {



    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}