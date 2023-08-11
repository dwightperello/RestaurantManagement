package com.example.restaurantmanagement.presentation.activity.Subitems

import androidx.lifecycle.*
import com.example.restaurantmanagement.Util.ResultState
import com.example.restaurantmanagement.domain.model.response.AllMenuModelItem
import com.example.restaurantmanagement.domain.model.response.Submenu
import com.example.restaurantmanagement.usecase.Menu.AddMenuUseCase
import com.example.restaurantmanagement.usecase.Menu.DeleteMenuUseCase
import com.example.restaurantmanagement.usecase.Menu.EditMenuUseCase
import com.example.restaurantmanagement.usecase.Menu.UpdateMenuUseCase
import com.example.restaurantmanagement.usecase.submenu.AddNewSubmenuUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class SubmenuItemViewModel  @Inject constructor(private val editMenuUseCase: EditMenuUseCase,private val addNewSubmenuUseCase: AddNewSubmenuUseCase ,savedStateHandle: SavedStateHandle) :
    ViewModel() {

    private var _menus: MutableLiveData<ResultState<ArrayList<AllMenuModelItem>>> = MutableLiveData()
    val menus: LiveData<ResultState<ArrayList<AllMenuModelItem>>> get() = _menus

    private var _addnew:MutableLiveData<ResultState<ResponseBody>> = MutableLiveData()
    val addnew:LiveData<ResultState<ResponseBody>> get() = _addnew

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
}