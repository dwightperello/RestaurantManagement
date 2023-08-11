package com.example.restaurantmanagement.presentation.activity.Subitems

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.restaurantmanagement.R
import com.example.restaurantmanagement.Util.DataTempMngr
import com.example.restaurantmanagement.Util.ResultState
import com.example.restaurantmanagement.Util.showCustomToast
import com.example.restaurantmanagement.Util.startActivityWithAnimation
import com.example.restaurantmanagement.databinding.ActivityEditSubMenuitemBinding
import com.example.restaurantmanagement.domain.model.response.AllMenuModelItem
import com.example.restaurantmanagement.domain.model.response.Submenu
import com.example.restaurantmanagement.presentation.activity.BaseActivity
import com.example.restaurantmanagement.presentation.activity.Home.HomeActivity
import com.example.restaurantmanagement.presentation.activity.PreviewActivity
import com.example.restaurantmanagement.presentation.adapter.AllSubmenuAdapter
import com.example.restaurantmanagement.presentation.adapter.showMenuForsubitemAdapter
import com.thecode.aestheticdialogs.*
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.ResponseBody

@AndroidEntryPoint
class EditSubMenuitemActivity : BaseActivity() {
    lateinit var _binding:ActivityEditSubMenuitemBinding
    private val viewModel: SubmenuItemViewModel by viewModels()
    private lateinit var AllSubmenuAdapter: AllSubmenuAdapter
    private lateinit var showMenuForsubitemAdapter: showMenuForsubitemAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityEditSubMenuitemBinding.inflate(layoutInflater)
        setContentView(_binding!!.root)
        initmenu()


    }

   val initmenu= {
       viewModel.menu_subitem()


    }

    override fun onStart() {
        super.onStart()
        viewModel.allsubmenu.observe(this, Observer {
            state -> ProcessSubmenu(state)
        })

        viewModel.menus.observe(this, Observer {
            state -> ProcessMenu(state)
        })

        viewModel.deletesubitem.observe(this, Observer {
            state->processdelete(state)
        })
    }

    private fun processdelete(state: ResultState<ResponseBody>){
        when(state){
            is ResultState.Loading ->{

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

    private fun ProcessSubmenu(state: ResultState<ArrayList<Submenu>>){
        when(state){
            is ResultState.Loading ->{

            }
            is ResultState.Success->{
                hideProgressDialog()

                AllSubmenuAdapter= AllSubmenuAdapter(this)
                _binding!!.rvMenuItemList.adapter = AllSubmenuAdapter
                _binding!!.rvMenuItemList.layoutManager =   GridLayoutManager(this, 2)
                state.data?.let {
                    AllSubmenuAdapter.populateSubMenu(it)

                }
            }
            is ResultState.Error->{
                hideProgressDialog()
                Toast(this).showCustomToast(state.exception.toString(),this)
            }
            else -> {}
        }
    }

    private fun ProcessMenu(state: ResultState<ArrayList<AllMenuModelItem>>){
        when(state){
            is ResultState.Loading ->{
                showCustomProgressDialog()
            }
            is ResultState.Success->{


                showMenuForsubitemAdapter= showMenuForsubitemAdapter(this)
                _binding!!.rvMenuList.adapter = showMenuForsubitemAdapter
                _binding!!.rvMenuList.layoutManager =   GridLayoutManager(this, 2)
                state.data?.let {
                    showMenuForsubitemAdapter.populateMenu(it)

                }
                viewModel.allSubmenu()
            }
            is ResultState.Error->{
                hideProgressDialog()
                Toast(this).showCustomToast(state.exception.toString(),this)
            }
            else -> {}
        }
    }

    fun filterDate(menus: AllMenuModelItem) {

        var item= menus.submenu
        var itemList:ArrayList<Submenu> = ArrayList(item)

        AllSubmenuAdapter= AllSubmenuAdapter(this)
        _binding!!.rvMenuItemList.adapter = AllSubmenuAdapter
        _binding!!.rvMenuItemList.layoutManager =   GridLayoutManager(this, 2)
        itemList.let {
            AllSubmenuAdapter.populateSubMenu(it)

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        startActivityWithAnimation<HomeActivity>(com.karumi.dexter.R.anim.design_bottom_sheet_slide_in, androidx.appcompat.R.anim.abc_slide_out_top)
        finish()

    }

    fun ConfirmUpdate(menus: Submenu) {
        DataTempMngr.submenu= menus
        val intent = Intent(this, UpdateActivity::class.java)
        startActivity(intent)
    }

    fun confirmdelete(menus: Submenu) {
        viewModel.delete_subitem(menus.id,"Bearer ${DataTempMngr.loginresponse!!.token}")
    }

    val showdialog={
        AestheticDialog.Builder(this, DialogStyle.FLASH, DialogType.SUCCESS)
            .setTitle("Success")
            .setMessage("Changes Successful")
            .setCancelable(false)
            .setDarkMode(false)
            .setGravity(Gravity.CENTER)
            .setAnimation(DialogAnimation.SHRINK)
            .setOnClickListener(object : OnDialogClickListener {
                override fun onClick(dialog: AestheticDialog.Builder) {
                    dialog.dismiss()
                    viewModel.menu_subitem()

                }
            })
            .show()
    }
}