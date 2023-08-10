package com.example.restaurantmanagement.usecase.Home

import com.example.restaurantmanagement.Util.ResultState
import com.example.restaurantmanagement.domain.model.response.TableOrdersItem
import com.example.restaurantmanagement.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class Home_TableOrdersUseCase @Inject constructor(private val repository: NetworkRepository) {

    operator fun invoke(datetimestamp:String): Flow<ResultState<ArrayList<TableOrdersItem>>> = flow {
        try {
            emit(ResultState.Loading)
            val response = repository.TableOrders(datetimestamp)
            emit(ResultState.Success<ArrayList<TableOrdersItem>>(response))
        }catch (ex: HttpException){
            emit(ResultState.Error(ex))
        }
        //EXAMPLE IO EXCEPTION - no internet
        catch (ex: IOException){
            emit(ResultState.Error(ex))
        }
    }
}