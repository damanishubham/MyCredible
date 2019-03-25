package com.example.mycredible.ControlClasses

import org.json.JSONObject

class PersonalDetailsClass(response: JSONObject) {
    var skills:String?=null
    var mobile:String?=null
    var fullname:String?=null
    var links:String?=null
    var location:String?=null
    var email:String?=null
    var put_id:Int=0

    init {
        this.skills=response.getJSONObject("data").getString("skills")
        this.mobile=response.getJSONObject("data").getString("mobile_no")
        this.fullname=response.getJSONObject("data").getString("name")
        this.links=response.getJSONObject("data").getString("links")
        this.location=response.getJSONObject("data").getString("location")
        this.put_id=response.getJSONObject("data").getString("id").toInt()
    }
}