package com.example.mycredible.ControlClasses

import org.json.JSONObject

class EducationalDetailsClass(response:JSONObject) {

    var location:String?=null
    var put_id:Int=0
    var start_date:String?=null
    var end_date:String?=null
    var organisation:String?=null
    var degree:String?=null

    init {
        this.location=response.getJSONObject("data").getString("location")
        this.put_id=response.getJSONObject("data").getString("id").toInt()
        this.end_date=response.getJSONObject("data").getString("end_year")
        this.start_date=response.getJSONObject("data").getString("start_year")
        this.organisation=response.getJSONObject("data").getString("organisation")
        this.degree=response.getJSONObject("data").getString("degree")
    }
}