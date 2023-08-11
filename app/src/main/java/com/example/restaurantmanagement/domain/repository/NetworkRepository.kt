package com.example.restaurantmanagement.domain.repository

import com.example.restaurantmanagement.Util.ResultState
import com.example.restaurantmanagement.domain.model.request.NewMenu
import com.example.restaurantmanagement.domain.model.request.UpdateMenuSubitem
import com.example.restaurantmanagement.domain.model.request.request_login
import com.example.restaurantmanagement.domain.model.response.*
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

interface NetworkRepository {

    suspend fun Login(loginreqesut: request_login) : response_login

    suspend fun TableOrders(datetimestamp:String):ArrayList<TableOrdersItem>

    suspend fun SalesReport(datetimestamp: String): ArrayList<SalesReportItem>

    suspend fun PostNewMenu(newMenu: NewMenu):ResponseBody

    suspend fun Menus():ArrayList<AllMenuModelItem>

    suspend fun DeleteMenu(id:Int):ResponseBody

    suspend fun updateMenu(id:Int,newMenu: NewMenu):ResponseBody

    suspend fun AddNewSubmenu(submenu: Submenu,token:String):ResponseBody

    suspend fun getAllSubmenu():ArrayList<Submenu>

    suspend fun updateSubmenuitems(id:Int,updateMenuSubitem: UpdateMenuSubitem,token: String) :ResponseBody

    suspend fun deleteSubitem(id:Int,token: String):ResponseBody

}