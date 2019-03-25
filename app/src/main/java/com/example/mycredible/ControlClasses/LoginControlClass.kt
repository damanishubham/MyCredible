package com.example.mycredible.ControlClasses

import android.util.Log
import org.json.JSONObject

class LoginControlClass(response:JSONObject) {

    var id:Int=0
    var email: String?=null

    init {
        email= response.getJSONObject("data").getString("email")
        id=response.getJSONObject("data").getString("id").toInt()
    }


    fun returnId():Int
    { Log.v("id in class",id.toString())
        return id
    }
    fun returnEmail():String{
        return email.toString()
    }
}