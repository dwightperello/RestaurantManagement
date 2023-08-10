package com.example.restaurantmanagement.presentation.activity.MainActivity

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.restaurantmanagement.R
import com.example.restaurantmanagement.Util.DataTempMngr
import com.example.restaurantmanagement.Util.ResultState
import com.example.restaurantmanagement.Util.showCustomToast
import com.example.restaurantmanagement.databinding.ActivityMainBinding
import com.example.restaurantmanagement.domain.model.request.request_login
import com.example.restaurantmanagement.domain.model.response.response_login
import com.example.restaurantmanagement.presentation.activity.BaseActivity
import com.example.restaurantmanagement.presentation.activity.Home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private val viewModel: MainViewModel by viewModels()
    lateinit var _binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        _binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.btnLogin.setOnClickListener {
            val method= request_login(
                email = _binding.etEmail.text.toString(),
                password = _binding.etPassword.text.toString()
            )
            viewModel.InitLogin(method)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.login.observe(this, Observer {
            state -> ProcessLoginResponse(state)
        })
    }

    private fun ProcessLoginResponse(state: ResultState<response_login>){
        when(state){
            is ResultState.Loading ->{
                showCustomProgressDialog()
            }
            is ResultState.Success->{
                hideProgressDialog()
                DataTempMngr.loginresponse= state.data
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                overridePendingTransition(
                    R.anim.screenslideright,
                    R.anim.screen_slide_out_left);
            }
            is ResultState.Error->{
                hideProgressDialog()
                Toast(this).showCustomToast(state.exception.toString(),this)
            }
            else -> {}
        }
    }
}