package com.example.mycredible.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.mycredible.MainActivity
import com.example.mycredible.MainScreenActivity
import com.example.mycredible.R
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.entity.StringEntity
import org.json.JSONObject

class EducationalDetailsActivity : AppCompatActivity() {

    var organisation: EditText?=null
    var degree: EditText?=null
    var save: Button?=null
    var location:EditText?=null
    var startYearSpinner: Spinner?=null
    var endYearSpinner: Spinner?=null


    var id=0
    var email:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_educational_details)

        organisation=findViewById(R.id.organization_edit_text)
        degree=findViewById(R.id.degree_edit_text)
        save=findViewById(R.id.save_button)
        location=findViewById(R.id.location_edit_text)
        startYearSpinner=findViewById(R.id.start_year_spinner)
        endYearSpinner=findViewById(R.id.end_year_spinner)
    }

    override fun onResume() {
        super.onResume()
        var Uorganisation:String?=null
        var Udegree:String?=null

        var startyear= startYearSpinner?.selectedItem.toString()
        var endyear= endYearSpinner?.selectedItem.toString()
        var Ulocation:String?=null


        save?.setOnClickListener {
            Uorganisation=organisation?.getText().toString()
            Udegree=degree?.getText().toString()
            Ulocation=location?.getText().toString()
            putEducationDetails(startyear,endyear,Uorganisation,Udegree,Ulocation)
        }

    }

    private fun putEducationDetails(
        startyear: String, endyear: String, uorganisation: String?, udegree: String?, ulocation: String?)
    {
        var params = JSONObject()

        params.put("organisation", uorganisation.toString())
        params.put("degree",udegree.toString())
        params.put("location",ulocation.toString())
        params.put("start_year", startyear)
        params.put("end_year", endyear)

        if(MainActivity.static.updateCall)
        {
            updateHTTPReq(params)
        }
        else
            makeHTTPReq(params)
    }

    private fun updateHTTPReq(params: JSONObject) {
        val client = AsyncHttpClient()
        var entity=StringEntity(params.toString())
        client.put(this@EducationalDetailsActivity,MainActivity.static.educationalDetailsURL+MainActivity.static.id,entity,"application/json",object : JsonHttpResponseHandler(){

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?)
            {
                super.onSuccess(statusCode, headers, response)
                MainActivity.static.updateCall=false
                val intent = Intent(this@EducationalDetailsActivity, MainScreenActivity::class.java)
                startActivity(intent)
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?)
            {
                super.onFailure(statusCode, headers, throwable, errorResponse)
                Toast.makeText(this@EducationalDetailsActivity,throwable.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun makeHTTPReq(params: JSONObject) {

        val client = AsyncHttpClient()
        var entity=StringEntity(params.toString())
        client.post(this@EducationalDetailsActivity,MainActivity.static.educationalDetailsURL+MainActivity.static.id,entity,"application/json",object : JsonHttpResponseHandler(){

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?)
            {
                super.onSuccess(statusCode, headers, response)
                val intent = Intent(this@EducationalDetailsActivity, MainScreenActivity::class.java)
                startActivity(intent)
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?)
            {
                super.onFailure(statusCode, headers, throwable, errorResponse)
                Toast.makeText(this@EducationalDetailsActivity,throwable.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        id=data?.getStringExtra("id")!!.toInt()
        email=data.getStringExtra("email")
    }

}
