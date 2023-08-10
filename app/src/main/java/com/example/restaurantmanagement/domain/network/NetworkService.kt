package com.example.restaurantmanagement.domain.network

import com.example.restaurantmanagement.domain.model.request.NewMenu
import com.example.restaurantmanagement.domain.model.request.request_login
import com.example.restaurantmanagement.domain.model.response.AllMenuModelItem
import com.example.restaurantmanagement.domain.model.response.SalesReportItem
import com.example.restaurantmanagement.domain.model.response.TableOrdersItem
import com.example.restaurantmanagement.domain.model.response.response_login
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

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



}