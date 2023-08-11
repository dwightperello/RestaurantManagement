package com.example.restaurantmanagement.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.restaurantmanagement.R
import com.example.restaurantmanagement.databinding.ActivityPreviewBinding

class PreviewActivity : AppCompatActivity() {
    lateinit var _binding:ActivityPreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityPreviewBinding.inflate(layoutInflater)
        setContentView(_binding!!.root)
        computeWindowSizeClasses()
        loadIMage()
    }

    private fun computeWindowSizeClasses() {
        try {
            val layout: LinearLayout = findViewById(R.id.mainLinear)
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            var width = displayMetrics.widthPixels
            var height = displayMetrics.heightPixels
            val autoScreenSize= when{
                width > 900f -> {
                    val params: ViewGroup.LayoutParams = layout.layoutParams
                    params.width = 800
                    layout.layoutParams = params
                }
                else -> {}
            }
        }catch (e:java.lang.Exception){
            Log.d("ta",e.localizedMessage)
        }
    }

    val loadIMage={
        var url =intent.getStringExtra("key")
        Glide.with(this)
            .load(url)
            .centerInside()
            .circleCrop()
            .apply(RequestOptions.centerCropTransform())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(_binding.ivPreview)
    }
}