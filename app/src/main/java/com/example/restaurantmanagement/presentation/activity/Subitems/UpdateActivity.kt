package com.example.restaurantmanagement.presentation.activity.Subitems

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
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
import com.example.restaurantmanagement.databinding.ActivityEditSubMenuitemBinding
import com.example.restaurantmanagement.databinding.ActivityUpdateBinding
import com.example.restaurantmanagement.domain.model.request.UpdateMenuSubitem
import com.example.restaurantmanagement.domain.model.response.Submenu
import com.example.restaurantmanagement.presentation.activity.BaseActivity
import com.thecode.aestheticdialogs.*
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.ResponseBody

@AndroidEntryPoint
class UpdateActivity : BaseActivity() {

    lateinit var _binding:ActivityUpdateBinding
    private val viewModel: SubmenuItemViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(_binding!!.root)

        InitSpinnerIsavailable()
        InitSpinnerIsRecomended()

        _binding.btnAddToCheckout.setOnClickListener {
            viewModel.updatesubemenu(DataTempMngr.submenu!!.id,converttosubitem(),"Bearer ${DataTempMngr.loginresponse!!.token}")
        }
        initializedview()
        computeWindowSizeClasses()
    }

    val initializedview={
        Glide.with(this)
            .load(DataTempMngr.submenu?.imageURL)
            .apply(RequestOptions.centerCropTransform())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(_binding.ivSubitemImageAdd)
        _binding.tvSubmenuName.text=DataTempMngr.submenu?.name
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
                    params.width = 900
                    layout.layoutParams = params
                }
                else -> {val params: ViewGroup.LayoutParams = layout.layoutParams
                    params.width = 900
                    layout.layoutParams = params}
            }
        }catch (e:java.lang.Exception){
            Log.d("ta",e.localizedMessage)
        }
    }

    var isAvailable:Boolean?= null

    val InitSpinnerIsavailable={
        val options = resources.getStringArray(R.array.true_false_options)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        _binding.spinnerAvailable.adapter = adapter

        _binding.spinnerAvailable.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                val selectedOption = options[position]
                val booleanValue = when (selectedOption) {
                    "True" -> isAvailable = true
                    "False" -> isAvailable = false
                    else ->isAvailable= false // Default to false if an unexpected option is selected
                }

                // Now you can use the booleanValue in your logic
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

    }

    var isrecomnded:Boolean?= null

    val InitSpinnerIsRecomended={
        val options = resources.getStringArray(R.array.true_false_options_isrecommended)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        _binding.spinnerRecommended.adapter = adapter

        _binding.spinnerRecommended.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                val selectedOption = options[position]
                val booleanValue = when (selectedOption) {
                    "True" -> isrecomnded = true
                    "False" -> isrecomnded = false
                    else ->isrecomnded= false // Default to false if an unexpected option is selected
                }

                // Now you can use the booleanValue in your logic
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

    }

    val converttosubitem:()-> UpdateMenuSubitem ={

        val method= UpdateMenuSubitem(

            isAvailable = isAvailable!!,
            isRecommended = isrecomnded!!,

        )
        method
    }

    override fun onStart() {
        super.onStart()
        viewModel.updatesubmenu.observe(this, Observer {
            state-> ProcessResponse(state)
        })
    }

    private fun ProcessResponse(state: ResultState<ResponseBody>){
        when(state){
            is ResultState.Loading ->{
                showCustomProgressDialog()
            }
            is ResultState.Success->{
                hideProgressDialog()
                this.finish()


            }
            is ResultState.Error->{

                Toast(this).showCustomToast(state.exception.toString(),this)
            }
            else -> {}
        }
    }


}