package com.example.restaurantmanagement.domain.network

import com.example.restaurantmanagement.domain.model.request.NewMenu
import com.example.restaurantmanagement.domain.model.request.UpdateMenuSubitem
import com.example.restaurantmanagement.domain.model.request.request_login
import com.example.restaurantmanagement.domain.model.response.*
import okhttp3.ResponseBody
import retrofit2.http.*

interface NetworkService {

    @POST("Account/login")
    suspend fun login(@Body loginreqesut: request_login):response_login

    @GET("TableOrder/GetOrdersForChart/{datetimestamp}")
    suspend fun getTableOrder(@Path("datetimestamp") datetimestamp:String): ArrayList<TableOrdersItem>

    @GET("Sales/sales/{datetimestamp}")
    suspend fun getSalesReport(@Path("datetimestamp") datetimestamp:String): ArrayList<SalesReportItem>

    @POST("Menus")
    suspend fun AddNewMenu(@Body newMenu: NewMenu): ResponseBody

    @GET("Menus")
    suspend fun GetMenu():ArrayList<AllMenuModelItem>

    @DELETE("Menus/{id}")
    suspend fun DeleteMenu(@Path("id") id:Int) : ResponseBody

    @PUT("Menus/{id}")
    suspend fun updateMenu(@Path("id") id:Int,@Body newMenu: NewMenu): ResponseBody

    @POST("SubMenus")
    suspend fun AddNewSubmenu(@Body submenu: Submenu,@Header("Authorization")token: String):ResponseBody

    @GET("SubMenus")
    suspend fun getAllSubMenu(): ArrayList<Submenu>

    @PUT("SubMenus/{id}")
    suspend fun UpdateSubMenu(@Path("id")id:Int,@Body updateMenuSubitem: UpdateMenuSubitem,@Header("Authorization")token: String): ResponseBody

    @DELETE("SubMenus/{id}")
    suspend fun deletesubmenuitem(@Path("id")id:Int,@Header("Authorization")token: String):ResponseBody




}