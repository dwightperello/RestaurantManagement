package com.example.restaurantmanagement.presentation.activity

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import com.example.restaurantmanagement.R

open class BaseActivity : AppCompatActivity() {

    private var mProgressDialog: Dialog? = null

    fun showCustomProgressDialog() {
        mProgressDialog = Dialog(this)
        mProgressDialog?.let {
            it.setContentView(R.layout.dialog_custom_progress)
            it.show()
        }
    }
    fun hideProgressDialog() {
        mProgressDialog?.let {
            it.dismiss()
        }
    }
}