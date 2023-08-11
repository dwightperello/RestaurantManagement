package com.example.restaurantmanagement.presentation.activity.Menu

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.restaurantmanagement.R
import com.example.restaurantmanagement.Util.DataTempMngr
import com.example.restaurantmanagement.Util.ResultState
import com.example.restaurantmanagement.Util.showCustomToast
import com.example.restaurantmanagement.Util.startActivityWithAnimation
import com.example.restaurantmanagement.databinding.ActivityEditMenuBinding
import com.example.restaurantmanagement.databinding.ActivityMenuBinding
import com.example.restaurantmanagement.domain.model.request.NewMenu
import com.example.restaurantmanagement.domain.model.response.AllMenuModelItem
import com.example.restaurantmanagement.presentation.activity.BaseActivity
import com.example.restaurantmanagement.presentation.activity.Home.HomeActivity
import com.example.restaurantmanagement.presentation.adapter.AllMenuAdapter
import com.thecode.aestheticdialogs.*
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.ResponseBody

@AndroidEntryPoint
class EditMenuActivity : BaseActivity() {
    lateinit var _binding:ActivityEditMenuBinding
    private val viewModel: MenuViewModel by viewModels()
    private lateinit var allmenuitemadapater: AllMenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityEditMenuBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        initialized()
    }

    val initialized={
        viewModel.GetAllMenu()
        _binding.btnPreview.setOnClickListener {
            if (_binding.etUrl.text.isNotEmpty()) {
                val url=_binding.etUrl.text.toString()
                Glide.with(this)
                    .load(url)
                    .centerInside()
                    .circleCrop()
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
            if(validatefieldsresult) showAlertDialogUpdate(menu!!.id)
            else Toast(this).showCustomToast("Check entry",this)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.allmenu.observe(this, Observer {
            result-> ProcessMenuResult(result)
        })

        viewModel.delete.observe(this   , Observer {
            state-> ProcessDeleteState(state)
        })

        viewModel.update.observe(this, Observer {
            state-> ProcesUpdateState(state)
        })
    }

    private fun ProcessMenuResult(state: ResultState<ArrayList<AllMenuModelItem>>){
        when(state){
            is ResultState.Loading ->{
                showCustomProgressDialog()
            }
            is ResultState.Success->{
                hideProgressDialog()
                allmenuitemadapater= AllMenuAdapter(this)
                _binding!!.rvMenuList.adapter = allmenuitemadapater
                _binding!!.rvMenuList.layoutManager =   GridLayoutManager(this, 2)
                state.data?.let {
                    allmenuitemadapater.populateMenu(it)
                }
            }
            is ResultState.Error->{
                hideProgressDialog()
                Toast(this).showCustomToast(state.exception.toString(),this)
            }
            else -> {}
        }
    }

    private fun ProcessDeleteState(state:ResultState<ResponseBody>){
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

    private fun ProcesUpdateState(state:ResultState<ResponseBody>){
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

    override fun onBackPressed() {
        super.onBackPressed()
        startActivityWithAnimation<HomeActivity>(R.anim.screenslideleft,R.anim.screen_slide_out_right)
        finish()
    }

    var menu:AllMenuModelItem?= null
    fun showmenu(menus: AllMenuModelItem) {
         menu= menus
        _binding.etMenuname.setText(menus.name)
        _binding.etDescription.setText(menus.description)
        _binding.etTagid.setText(menus.tag.toString())
        _binding.etUrl.setText(menus.imageURL)

    }

    fun confirmdelete(menus: AllMenuModelItem) {
        showAlertDialog(menus.id)
    }
    private fun showAlertDialog(id:Int) {
        val alertDialogBuilder = AlertDialog.Builder(this)

        alertDialogBuilder.setTitle("Confirmation")
        alertDialogBuilder.setMessage("ARE YOU SURE YOU WANT TO DELETE? THIS WILL DELETE AS WELL SUBMENU ITEMS")

        alertDialogBuilder.setPositiveButton("Yes") { dialog, which ->
            viewModel.DeleteMenu(id)
        }

        alertDialogBuilder.setNegativeButton("No") { dialog, which ->
            // Handle the "No" button click
            // You can add your code here
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showAlertDialogUpdate(id:Int) {
        val alertDialogBuilder = AlertDialog.Builder(this)

        alertDialogBuilder.setTitle("Confirmation")
        alertDialogBuilder.setMessage("You are about to update menu item, continue?")

        alertDialogBuilder.setPositiveButton("Yes") { dialog, which ->
            viewModel.UpdateMenu(id,convertJson())
        }

        alertDialogBuilder.setNegativeButton("No") { dialog, which ->
            // Handle the "No" button click
            // You can add your code here
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    val showdialog={
        AestheticDialog.Builder(this, DialogStyle.FLASH, DialogType.SUCCESS)
            .setTitle("Success")
            .setMessage("Update Success")
            .setCancelable(false)
            .setDarkMode(false)
            .setGravity(Gravity.CENTER)
            .setAnimation(DialogAnimation.SHRINK)
            .setOnClickListener(object : OnDialogClickListener {
                override fun onClick(dialog: AestheticDialog.Builder) {
                    dialog.dismiss()
                    viewModel.GetAllMenu()
                    _binding.etUrl.setText("")
                    _binding.etUrl.setText("")
                    _binding.etMenuname.setText("")
                    _binding.etDescription.setText("")
                    _binding.etTagid.setText("")
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
            id = menu!!.id

        )
        method
    }
}