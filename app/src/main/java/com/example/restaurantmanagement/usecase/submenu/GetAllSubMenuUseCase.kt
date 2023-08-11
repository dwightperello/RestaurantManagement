package com.example.restaurantmanagement.usecase.submenu

import com.example.restaurantmanagement.Util.ResultState
import com.example.restaurantmanagement.domain.model.response.Submenu
import com.example.restaurantmanagement.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetAllSubMenuUseCase @Inject constructor(private val repository: NetworkRepository) {

    operator fun invoke(): Flow<ResultState<ArrayList<Submenu>>> = flow {
        try {
            emit(ResultState.Loading)
            val response = repository.getAllSubmenu()
            emit(ResultState.Success<ArrayList<Submenu>>(response))
        }catch (ex: HttpException){
            emit(ResultState.Error(ex))
        }
        //EXAMPLE IO EXCEPTION - no internet
        catch (ex: IOException){
            emit(ResultState.Error(ex))
        }
    }
}