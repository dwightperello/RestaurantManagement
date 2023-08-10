package com.example.restaurantmanagement.presentation.activity.Home

import androidx.lifecycle.*
import com.example.restaurantmanagement.Util.ResultState
import com.example.restaurantmanagement.domain.model.response.SalesReportItem
import com.example.restaurantmanagement.domain.model.response.TableOrdersItem
import com.example.restaurantmanagement.domain.model.response.response_login
import com.example.restaurantmanagement.usecase.Home.Home_SaleReportLoopUseCase
import com.example.restaurantmanagement.usecase.Home.Home_SalesReportUseCase
import com.example.restaurantmanagement.usecase.Home.Home_TableOrdersUseCase
import com.example.restaurantmanagement.usecase.mainActivityUseCase.MainUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val home_tableordersUseCase: Home_TableOrdersUseCase,
    private val homeSalesreportusecase: Home_SalesReportUseCase,
    private val homeSalereportloopusecase: Home_SaleReportLoopUseCase,
    savedStateHandle: SavedStateHandle) : ViewModel() {

    private var _tableorders: MutableLiveData<ResultState<ArrayList<TableOrdersItem>>> = MutableLiveData()
    val tableorders: LiveData<ResultState<ArrayList<TableOrdersItem>>> get() = _tableorders

    private var _salesreport:MutableLiveData<ResultState<ArrayList<SalesReportItem>>> = MutableLiveData()
    val salesreport:LiveData<ResultState<ArrayList<SalesReportItem>>> get() = _salesreport

    private  var _salesreportloop: MutableLiveData<ResultState<ArrayList<SalesReportItem>>> = MutableLiveData()
    val salesreportloop:LiveData<ResultState<ArrayList<SalesReportItem>>> get() = _salesreportloop

    fun getTableOrdersForChart(datetimestamp:String){
        home_tableordersUseCase(datetimestamp).onEach {
            when(it){
                is ResultState.Success ->{
                    _tableorders.value= it
                }
                is ResultState.Error -> {
                    _tableorders.value= it
                }
                is ResultState.Loading ->{
                    _tableorders.value=it
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun getSalesReportForChart(datetimestamp:String){
        homeSalesreportusecase(datetimestamp).onEach {
            when(it){
                is ResultState.Success ->{
                    _salesreport.value= it
                }
                is ResultState.Error -> {
                    _salesreport.value= it
                }
                is ResultState.Loading ->{
                    _salesreport.value=it
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun getSalesReportLoop(datetimestamp: String){
        homeSalereportloopusecase(datetimestamp).onEach {
            when(it){
                is ResultState.Success ->{
                    _salesreportloop.value= it
                }
                is ResultState.Error -> {
                    _salesreportloop.value= it
                }
                is ResultState.Loading ->{
                    _salesreportloop.value=it
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }
}