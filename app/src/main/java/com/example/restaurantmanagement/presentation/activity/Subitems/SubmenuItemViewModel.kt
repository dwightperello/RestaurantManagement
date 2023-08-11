package com.example.restaurantmanagement.presentation.activity.Subitems

import androidx.lifecycle.*
import com.example.restaurantmanagement.Util.ResultState
import com.example.restaurantmanagement.domain.model.request.UpdateMenuSubitem
import com.example.restaurantmanagement.domain.model.response.AllMenuModelItem
import com.example.restaurantmanagement.domain.model.response.Submenu
import com.example.restaurantmanagement.usecase.Menu.AddMenuUseCase
import com.example.restaurantmanagement.usecase.Menu.DeleteMenuUseCase
import com.example.restaurantmanagement.usecase.Menu.EditMenuUseCase
import com.example.restaurantmanagement.usecase.Menu.UpdateMenuUseCase
import com.example.restaurantmanagement.usecase.submenu.AddNewSubmenuUseCase
import com.example.restaurantmanagement.usecase.submenu.DeleteSubitemUseCase
import com.example.restaurantmanagement.usecase.submenu.GetAllSubMenuUseCase
import com.example.restaurantmanagement.usecase.submenu.UpdateSubmenuItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class SubmenuItemViewModel  @Inject constructor(private val editMenuUseCase: EditMenuUseCase,private val addNewSubmenuUseCase: AddNewSubmenuUseCase,private val getAllSubMenuUseCase: GetAllSubMenuUseCase,private val updateSubmenuItemUseCase: UpdateSubmenuItemUseCase,private val deleteSubitemUseCase: DeleteSubitemUseCase,savedStateHandle: SavedStateHandle) :
    ViewModel() {

    private var _menus: MutableLiveData<ResultState<ArrayList<AllMenuModelItem>>> = MutableLiveData()
    val menus: LiveData<ResultState<ArrayList<AllMenuModelItem>>> get() = _menus

    private var _addnew:MutableLiveData<ResultState<ResponseBody>> = MutableLiveData()
    val addnew:LiveData<ResultState<ResponseBody>> get() = _addnew

    private var _allsubmenu: MutableLiveData<ResultState<ArrayList<Submenu>>> = MutableLiveData()
    val allsubmenu: LiveData<ResultState<ArrayList<Submenu>>> get() = _allsubmenu

    private var _updatesubmenu:MutableLiveData<ResultState<ResponseBody>> = MutableLiveData()
    val updatesubmenu:LiveData<ResultState<ResponseBody>> get() = _updatesubmenu

    private var _deletesubitem:MutableLiveData<ResultState<ResponseBody>> = MutableLiveData()
    val deletesubitem:LiveData<ResultState<ResponseBody>> get() = _deletesubitem

    fun menu_subitem(){
        editMenuUseCase().onEach {
            when(it){
                is ResultState.Success ->{
                    _menus.value= it
                }
                is ResultState.Error -> {
                    _menus.value= it
                }
                is ResultState.Loading ->{
                    _menus.value=it
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun allSubmenu(){
        getAllSubMenuUseCase().onEach {
            when(it){
                is ResultState.Success ->{
                    _allsubmenu.value= it
                }
                is ResultState.Error -> {
                    _allsubmenu.value= it
                }
                is ResultState.Loading ->{
                    _allsubmenu.value=it
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun addnewsubmenu(submenu: Submenu,token:String){
        addNewSubmenuUseCase(submenu,token).onEach {
            when(it){
                is ResultState.Success ->{
                    _addnew.value= it
                }
                is ResultState.Error -> {
                    _addnew.value= it
                }
                is ResultState.Loading ->{
                    _addnew.value=it
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun updatesubemenu(id:Int,updateMenuSubitem: UpdateMenuSubitem,token: String){
        updateSubmenuItemUseCase(id,updateMenuSubitem,token).onEach {
            when(it){
                is ResultState.Success ->{
                    _updatesubmenu.value= it
                }
                is ResultState.Error -> {
                    _updatesubmenu.value= it
                }
                is ResultState.Loading ->{
                    _updatesubmenu.value=it
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun delete_subitem(id:Int,token: String){
        deleteSubitemUseCase(id,token).onEach {
            when(it){
                is ResultState.Success ->{
                    _deletesubitem.value= it
                }
                is ResultState.Error -> {
                    _deletesubitem.value= it
                }
                is ResultState.Loading ->{
                    _deletesubitem.value=it
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }
}