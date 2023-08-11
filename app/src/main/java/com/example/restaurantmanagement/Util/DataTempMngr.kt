package com.example.restaurantmanagement.Util

import android.widget.EditText
import com.example.restaurantmanagement.domain.model.response.AllMenuModelItem
import com.example.restaurantmanagement.domain.model.response.Submenu
import com.example.restaurantmanagement.domain.model.response.response_login

object DataTempMngr {
    const val API_BASE_URL="http://10.0.0.9:8083/api/"

    var loginresponse:response_login?= null

    var submenu:Submenu?= null

    fun validAllFields(vararg editText: EditText):Boolean{
        for (editText in editText) {
            if (editText.text.toString().trim().isEmpty()) {
                return false
            }
        }
        return true
    }
}