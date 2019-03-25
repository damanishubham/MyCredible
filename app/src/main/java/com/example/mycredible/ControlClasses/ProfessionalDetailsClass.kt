package com.example.mycredible.ControlClasses

import org.json.JSONObject

class ProfessionalDetailsClass(response: JSONObject) {

    var organisation:String?=null
    var designation:String?=null
    var start_date:String?=null
    var end_date:String?=null
    var put_id:Int=0

    init {
        this.end_date=response.getJSONObject("data").getString("end_date")
        this.start_date=response.getJSONObject("data").getString("start_date")
        this.organisation=response.getJSONObject("data").getString("organisation")
        this.designation=response.getJSONObject("data").getString("designation")
        this.put_id=response.getJSONObject("data").getString("id").toInt()
    }
}