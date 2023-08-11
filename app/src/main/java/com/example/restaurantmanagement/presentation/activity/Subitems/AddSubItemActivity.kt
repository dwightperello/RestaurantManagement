package com.example.restaurantmanagement.presentation.activity.Subitems

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.restaurantmanagement.R
import com.example.restaurantmanagement.Util.DataTempMngr
import com.example.restaurantmanagement.Util.ResultState
import com.example.restaurantmanagement.Util.showCustomToast
import com.example.restaurantmanagement.Util.startActivityWithAnimation
import com.example.restaurantmanagement.databinding.ActivityAddSubItemBinding
import com.example.restaurantmanagement.databinding.ActivityHomeBinding
import com.example.restaurantmanagement.domain.model.response.AllMenuModelItem
import com.example.restaurantmanagement.domain.model.response.Submenu
import com.example.restaurantmanagement.presentation.activity.BaseActivity
import com.example.restaurantmanagement.presentation.activity.Home.HomeActivity

import com.example.restaurantmanagement.presentation.adapter.AllMenuAdapter
import com.example.restaurantmanagement.presentation.adapter.showMenuForsubitemAdapter
import com.thecode.aestheticdialogs.*
import dagger.hilt.android.AndroidEntryPoint
import hilt_aggregated_deps._com_example_restaurantmanagement_presentation_activity_Home_HomeViewModel_HiltModules_BindsModule
import okhttp3.ResponseBody

@AndroidEntryPoint
class AddSubItemActivity : BaseActivity() {
    lateinit var _binding:ActivityAddSubItemBinding
    private val viewModel: SubmenuItemViewModel by viewModels()
    private lateinit var showMenuForsubitemAdapter: showMenuForsubitemAdapter
    var tagids:ArrayList<Int> = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        _binding= ActivityAddSubItemBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        initData()

        _binding.btnCancel.setOnClickListener {
            startActivityWithAnimation<HomeActivity>(R.anim.screenslideleft,R.anim.screen_slide_out_right)
            finish()
        }

        _binding.btnSave.setOnClickListener {
            var tagid=_binding.etTagid.text.toString()
            if(tagids.contains(tagid.toInt())){
                Toast(this).showCustomToast("TAG ID ALREADY EXIST",this)
                return@setOnClickListener
            }

            val validatefieldsresult= DataTempMngr.validAllFields(_binding.etUrl,_binding.etTagid,_binding.etMenuname,_binding.etPrice,_binding.etDescription)
            if(validatefieldsresult) viewModel.addnewsubmenu(converttosubitem(),"Bearer ${DataTempMngr.loginresponse!!.token}")
            else Toast(this).showCustomToast("Check entry",this)
        }

        InitSpinnerIsavailable()
        InitSpinnerIsRecomended()
    }

    private fun initData(){
        viewModel.menu_subitem()
    }

    override fun onStart() {
        super.onStart()
        viewModel.menus.observe(this, Observer {
            state->ProcessMenu(state)
        })

        viewModel.addnew.observe(this, Observer {
            state-> ProcessStateadd(state)
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivityWithAnimation<HomeActivity>(R.anim.screenslideleft,R.anim.screen_slide_out_right)
        finish()
    }

    private fun ProcessMenu(state: ResultState<ArrayList<AllMenuModelItem>>){
        when(state){
            is ResultState.Loading ->{
                showCustomProgressDialog()
            }
            is ResultState.Success->{
                hideProgressDialog()

                showMenuForsubitemAdapter= showMenuForsubitemAdapter(this)
                _binding!!.rvMenuList.adapter = showMenuForsubitemAdapter
                _binding!!.rvMenuList.layoutManager =   GridLayoutManager(this, 2)
                state.data?.let {
                    showMenuForsubitemAdapter.populateMenu(it)

                }

                state.data.forEach {
                    it.submenu.forEach {
                        tagids.add(it.tag)
                    }
                }
            }
            is ResultState.Error->{
                hideProgressDialog()
                Toast(this).showCustomToast(state.exception.toString(),this)
            }
            else -> {}
        }
    }

    private fun ProcessStateadd(state: ResultState<ResponseBody>){
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

    var menuid:Int?= null
    fun showmenu(menus: AllMenuModelItem) {
        _binding.txtCategory.text=menus.name
        menuid=menus.id
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

    val converttosubitem:()->Submenu={
        var price=_binding.etPrice.text.toString()
        var tagid= _binding.etTagid.text.toString()
        val method= Submenu(
            id = 0,
            name = _binding.etMenuname.text.toString(),
            description = _binding.etDescription.text.toString(),
            price = price.toDouble(),
            imageURL = _binding.etUrl.text.toString(),
            menuId = menuid!!,
            isAvailable = isAvailable!!,
            isRecommended = isrecomnded!!,
            tag = tagid.toInt()
        )
        method
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
                    viewModel.menu_subitem()
                    _binding.etUrl.setText("")
                    _binding.etMenuname.setText("")
                    _binding.etDescription.setText("")
                    _binding.etTagid.setText("")
                    _binding.etPrice.setText("")
                    _binding.txtCategory.setText("")

                }
            })
            .show()
    }
}