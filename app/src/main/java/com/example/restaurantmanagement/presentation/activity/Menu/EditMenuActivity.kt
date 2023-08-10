package com.example.restaurantmanagement.presentation.activity.Menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import com.example.restaurantmanagement.R
import com.example.restaurantmanagement.databinding.ActivityEditMenuBinding
import com.example.restaurantmanagement.databinding.ActivityMenuBinding
import com.example.restaurantmanagement.presentation.activity.Home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditMenuActivity : AppCompatActivity() {
    lateinit var _binding:ActivityEditMenuBinding
    private val viewModel: MenuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityEditMenuBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        overridePendingTransition(
            R.anim.screenslideleft, R.anim.screen_slide_out_right
        );
        finish()
    }
}