package com.example.restaurantmanagement.presentation.activity.Menu

import androidx.lifecycle.*
import com.example.restaurantmanagement.Util.ResultState
import com.example.restaurantmanagement.domain.model.request.NewMenu
import com.example.restaurantmanagement.domain.model.request.request_login
import com.example.restaurantmanagement.domain.model.response.AllMenuModelItem
import com.example.restaurantmanagement.domain.model.response.response_login
import com.example.restaurantmanagement.usecase.Menu.AddMenuUseCase
import com.example.restaurantmanagement.usecase.Menu.DeleteMenuUseCase
import com.example.restaurantmanagement.usecase.Menu.EditMenuUseCase
import com.example.restaurantmanagement.usecase.Menu.UpdateMenuUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(private val addMenuUseCase: AddMenuUseCase,private val editMenuUseCase: EditMenuUseCase,private val deleteMenuUseCase: DeleteMenuUseCase,private val updateMenuUseCase: UpdateMenuUseCase,savedStateHandle: SavedStateHandle) :
    ViewModel() {

    private var _newmenu: MutableLiveData<ResultState<ResponseBody>> = MutableLiveData()
    val newmenu: LiveData<ResultState<ResponseBody>> get() = _newmenu

    private var _allmenu:MutableLiveData<ResultState<ArrayList<AllMenuModelItem>>> = MutableLiveData()
    val allmenu:LiveData<ResultState<ArrayList<AllMenuModelItem>>> get() = _allmenu

    private var _delete:MutableLiveData<ResultState<ResponseBody>> = MutableLiveData()
    val delete:LiveData<ResultState<ResponseBody>> get() = _delete

    private var _update:MutableLiveData<ResultState<ResponseBody>> = MutableLiveData()
    val update:LiveData<ResultState<ResponseBody>> get() = _update

    fun AddNewMenu(newMenu: NewMenu){
        addMenuUseCase(newMenu).onEach {
            when(it){
                is ResultState.Success ->{
                    _newmenu.value= it
                }
                is ResultState.Error -> {
                    _newmenu.value= it
                }
                is ResultState.Loading ->{
                    _newmenu.value=it
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun GetAllMenu(){
        editMenuUseCase().onEach {
            when(it){
                is ResultState.Success ->{
                    _allmenu.value= it
                }
                is ResultState.Error -> {
                    _allmenu.value= it
                }
                is ResultState.Loading ->{
                    _allmenu.value=it
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun DeleteMenu(id:Int){
        deleteMenuUseCase(id).onEach {
            when(it){
                is ResultState.Success<ResponseBody> ->{
                    _delete.value= it
                }
                is ResultState.Error -> {
                    _delete.value= it
                }
                is ResultState.Loading ->{
                    _delete.value=it
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun UpdateMenu(id: Int,newMenu: NewMenu){
        updateMenuUseCase(id,newMenu).onEach {
            when(it){
                is ResultState.Success ->{
                    _update.value= it
                }
                is ResultState.Error -> {
                    _update.value= it
                }
                is ResultState.Loading ->{
                    _update.value=it
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }
}