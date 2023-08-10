package com.example.restaurantmanagement.presentation.activity.MainActivity

import androidx.lifecycle.*
import com.example.restaurantmanagement.Util.ResultState
import com.example.restaurantmanagement.domain.model.request.request_login
import com.example.restaurantmanagement.domain.model.response.AllMenuModelItem
import com.example.restaurantmanagement.domain.model.response.response_login
import com.example.restaurantmanagement.usecase.Menu.EditMenuUseCase
import com.example.restaurantmanagement.usecase.mainActivityUseCase.MainUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainUseCase: MainUseCase,private val editMenuUseCase: EditMenuUseCase,savedStateHandle: SavedStateHandle) :ViewModel() {

    private var _login:MutableLiveData<ResultState<response_login>> = MutableLiveData()
    val login:LiveData<ResultState<response_login>> get() = _login

    private var _menus:MutableLiveData<ResultState<ArrayList<AllMenuModelItem>>> = MutableLiveData()
    val menu:LiveData<ResultState<ArrayList<AllMenuModelItem>>> get() = _menus

    fun InitLogin(requestLogin: request_login){
        mainUseCase(requestLogin).onEach {
            when(it){
                is ResultState.Success ->{
                    _login.value= it
                }
                is ResultState.Error -> {
                    _login.value= it
                }
                is ResultState.Loading ->{
                    _login.value=it
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun getMenus(){
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

}