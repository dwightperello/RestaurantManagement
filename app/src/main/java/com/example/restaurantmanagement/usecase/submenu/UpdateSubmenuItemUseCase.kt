package com.example.restaurantmanagement.usecase.submenu

import com.example.restaurantmanagement.Util.ResultState
import com.example.restaurantmanagement.domain.model.request.UpdateMenuSubitem
import com.example.restaurantmanagement.domain.model.response.Submenu
import com.example.restaurantmanagement.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UpdateSubmenuItemUseCase @Inject constructor(private val repository: NetworkRepository) {

    operator fun invoke(id:Int,updateMenuSubitem: UpdateMenuSubitem,token:String): Flow<ResultState<ResponseBody>> = flow {
        try {
            emit(ResultState.Loading)
            val response = repository.updateSubmenuitems(id,updateMenuSubitem,token)
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