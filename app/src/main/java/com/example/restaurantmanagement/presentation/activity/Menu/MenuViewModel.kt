package com.example.restaurantmanagement.presentation.activity.Menu

import androidx.lifecycle.*
import com.example.restaurantmanagement.Util.ResultState
import com.example.restaurantmanagement.domain.model.request.NewMenu
import com.example.restaurantmanagement.domain.model.request.request_login
import com.example.restaurantmanagement.domain.model.response.AllMenuModelItem
import com.example.restaurantmanagement.domain.model.response.response_login
import com.example.restaurantmanagement.usecase.Menu.AddMenuUseCase
import com.example.restaurantmanagement.usecase.Menu.EditMenuUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(private val addMenuUseCase: AddMenuUseCase,private val editMenuUseCase: EditMenuUseCase,savedStateHandle: SavedStateHandle) :
    ViewModel() {

    private var _newmenu: MutableLiveData<ResultState<ResponseBody>> = MutableLiveData()
    val newmenu: LiveData<ResultState<ResponseBody>> get() = _newmenu

    private var _allmenu:MutableLiveData<ResultState<ArrayList<AllMenuModelItem>>> = MutableLiveData()
    val allmenu:LiveData<ResultState<ArrayList<AllMenuModelItem>>> get() = _allmenu

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
}