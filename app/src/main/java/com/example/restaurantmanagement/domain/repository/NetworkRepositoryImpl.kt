package com.example.restaurantmanagement.domain.repository

import com.example.restaurantmanagement.Util.ResultState
import com.example.restaurantmanagement.domain.model.request.NewMenu
import com.example.restaurantmanagement.domain.model.request.request_login
import com.example.restaurantmanagement.domain.model.response.AllMenuModelItem
import com.example.restaurantmanagement.domain.model.response.SalesReportItem
import com.example.restaurantmanagement.domain.model.response.TableOrdersItem
import com.example.restaurantmanagement.domain.model.response.response_login
import com.example.restaurantmanagement.domain.network.NetworkService
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor (private val networkService: NetworkService):NetworkRepository {

    override suspend fun Login(loginreqesut: request_login): response_login {
      return networkService.login(loginreqesut)
    }

    override suspend fun TableOrders(datetimestamp:String): ArrayList<TableOrdersItem> {
        return networkService.getTableOrder(datetimestamp)
    }

    override suspend fun SalesReport(datetimestamp: String): ArrayList<SalesReportItem> {
        return networkService.getSalesReport(datetimestamp)
    }

    override suspend fun PostNewMenu(newMenu: NewMenu): ResponseBody {
        return  networkService.AddNewMenu(newMenu)
    }

    override suspend fun Menus(): ArrayList<AllMenuModelItem> {
       return  networkService.GetMenu()
    }


}