package com.example.restaurantmanagement.usecase.Menu

import com.example.restaurantmanagement.Util.ResultState
import com.example.restaurantmanagement.domain.model.request.NewMenu
import com.example.restaurantmanagement.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DeleteMenuUseCase @Inject constructor(private val repository: NetworkRepository) {
    operator fun invoke(id:Int): Flow<ResultState<ResponseBody>> = flow {
        try {
            emit(ResultState.Loading)
            val response = repository.DeleteMenu(id)
            emit(ResultState.Success<ResponseBody>(response))
        }catch (ex: HttpException){
            emit(ResultState.Error(ex))
        }
        //EXAMPLE IO EXCEPTION - no internet
        catch (ex: IOException){
            emit(ResultState.Error(ex))
        }
    }
}