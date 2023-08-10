package com.example.restaurantmanagement.domain.repository

import com.example.restaurantmanagement.Util.ResultState
import com.example.restaurantmanagement.domain.model.request.NewMenu
import com.example.restaurantmanagement.domain.model.request.request_login
import com.example.restaurantmanagement.domain.model.response.AllMenuModelItem
import com.example.restaurantmanagement.domain.model.response.SalesReportItem
import com.example.restaurantmanagement.domain.model.response.TableOrdersItem
import com.example.restaurantmanagement.domain.model.response.response_login
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

interface NetworkRepository {

    suspend fun Login(loginreqesut: request_login) : response_login

    suspend fun TableOrders(datetimestamp:String):ArrayList<TableOrdersItem>

    suspend fun SalesReport(datetimestamp: String): ArrayList<SalesReportItem>

    suspend fun PostNewMenu(newMenu: NewMenu):ResponseBody

    suspend fun Menus():ArrayList<AllMenuModelItem>

}