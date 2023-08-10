package com.example.restaurantmanagement.usecase.mainActivityUseCase

import com.example.restaurantmanagement.Util.ResultState
import com.example.restaurantmanagement.domain.model.request.request_login
import com.example.restaurantmanagement.domain.model.response.response_login
import com.example.restaurantmanagement.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MainUseCase @Inject constructor(private val repository: NetworkRepository) {

    operator fun invoke(requestLogin: request_login): Flow<ResultState<response_login>> = flow {
        try {
            emit(ResultState.Loading)
            val response = repository.Login(requestLogin)
            emit(ResultState.Success<response_login>(response))
        }catch (ex: HttpException){
            emit(ResultState.Error(ex))
        }
        //EXAMPLE IO EXCEPTION - no internet
        catch (ex: IOException){
            emit(ResultState.Error(ex))
        }
    }
}