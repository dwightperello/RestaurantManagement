package com.example.restaurantmanagement.presentation.activity.Menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.restaurantmanagement.R
import com.example.restaurantmanagement.Util.DataTempMngr
import com.example.restaurantmanagement.Util.ResultState
import com.example.restaurantmanagement.Util.showCustomToast
import com.example.restaurantmanagement.Util.startActivityWithAnimation
import com.example.restaurantmanagement.databinding.ActivityMenuBinding
import com.example.restaurantmanagement.domain.model.request.NewMenu
import com.example.restaurantmanagement.presentation.activity.BaseActivity
import com.example.restaurantmanagement.presentation.activity.Home.HomeActivity
import com.example.restaurantmanagement.presentation.activity.MainActivity.MainViewModel
import com.thecode.aestheticdialogs.*
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.ResponseBody

@AndroidEntryPoint
class MenuActivity : BaseActivity() {
    lateinit var _binding:ActivityMenuBinding
    private val viewModel: MenuViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityMenuBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        _binding.btnPreview.setOnClickListener {
            if (_binding.etUrl.text.isNotEmpty()) {
                val url=_binding.etUrl.text.toString()
                Glide.with(this)
                    .load(url)
                    .centerInside()
                    .apply(RequestOptions.centerCropTransform())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(_binding.ivImage)
            }
        }
        _binding.btnCancel.setOnClickListener {
            startActivityWithAnimation<HomeActivity>(R.anim.screenslideleft,R.anim.screen_slide_out_right)
            finish()
        }
        _binding.btnSave.setOnClickListener {
            val validatefieldsresult= DataTempMngr.validAllFields(_binding.etUrl,_binding.etTagid,_binding.etMenuname)
            if(validatefieldsresult)viewModel.AddNewMenu(convertJson())
            else Toast(this).showCustomToast("Check entry",this)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        overridePendingTransition(
            R.anim.screenslideleft,R.anim.screen_slide_out_right
        );
        finish()
    }

    val showdialog={
        AestheticDialog.Builder(this, DialogStyle.FLASH, DialogType.SUCCESS)
            .setTitle("Success")
            .setMessage("New menu saved")
            .setCancelable(false)
            .setDarkMode(false)
            .setGravity(Gravity.CENTER)
            .setAnimation(DialogAnimation.SHRINK)
            .setOnClickListener(object : OnDialogClickListener {
                override fun onClick(dialog: AestheticDialog.Builder) {
                    dialog.dismiss()
                    startActivityWithAnimation<HomeActivity>(R.anim.screenslideleft,R.anim.screen_slide_out_right)
                    finish()
                }
            })
            .show()
    }

    val convertJson:()->NewMenu={
        val convertoint= _binding.etTagid.text.toString()
        val method= NewMenu(
            name = _binding.etMenuname.text.toString(),
            description = _binding.etDescription.text.toString(),
            tag = convertoint.toInt(),
            imageURL = _binding.etUrl.text.toString(),
            id = 0
        )
        method
    }

    override fun onStart() {
        super.onStart()
        viewModel.newmenu.observe(this, Observer {
            state -> ProcessResult(state)
        })
    }

    private fun ProcessResult(state: ResultState<ResponseBody>?){
        when(state){
            is ResultState.Loading ->{
                showCustomProgressDialog()
            }
            is ResultState.Success->{
                hideProgressDialog()
                    showdialog()
                }
            is ResultState.Error->{
                hideProgressDialog()
                Toast(this).showCustomToast(state.exception.toString(),this)

            }
            else -> {}
        }

    }


}