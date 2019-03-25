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

class ProfetionalDetailsActivity : AppCompatActivity() {

    var organisation: EditText?=null
    var designation: EditText?=null
    var save:Button?=null
    var startMonthSpinner: Spinner?=null
    var startYearSpinner:Spinner?=null
    var endMonthSpinner:Spinner?=null
    var endYearSpinner:Spinner?=null


    var id=0
    var email:String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profetional_details)

        organisation= findViewById(R.id.organization_edit_text)
        designation=findViewById(R.id.designation_edit_text)
        startMonthSpinner=findViewById(R.id.start_month_spinner)
        startYearSpinner=findViewById(R.id.start_year_spinner)
        endMonthSpinner=findViewById(R.id.end_month_spinner)
        endYearSpinner=findViewById(R.id.end_year_spinner)
        save=findViewById(R.id.save)
    }

    override fun onResume() {
        super.onResume()

        save?.setOnClickListener {
            var Uorganisation=organisation?.getText().toString()
            var Udesignation=designation?.getText().toString()
            var startmonth= startMonthSpinner?.selectedItem.toString()
            var startyear= startYearSpinner?.selectedItem.toString()
            var endmonth= endMonthSpinner?.selectedItem.toString()
            var endyear= endYearSpinner?.selectedItem.toString()
            putProfessionnalDetails(startmonth,startyear,endmonth,endyear,Uorganisation,Udesignation)
        }

    }

    private fun putProfessionnalDetails(startmonth: String, startyear: String, endmonth: String, endyear: String, uorganisation: String?, udesignation: String?)
    {
        var params = JSONObject()

        params.put("organisation", uorganisation)
        params.put("designation",udesignation)
        params.put("start_date", "$startmonth-$startyear")
        params.put("end_date","$endmonth-$endyear")

        if(MainActivity.static.updateCall)
        {
            updateHTTPReq(params)
        }
        else
            makeHTTPReq(params)
    }

    private fun updateHTTPReq(params: JSONObject) {
        val client = AsyncHttpClient()
        var entity= StringEntity(params.toString())
        client.put(this@ProfetionalDetailsActivity,MainActivity.static.professionalDetailURl +MainActivity.static.id,entity,"application/json",object : JsonHttpResponseHandler(){

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?)
            {
                super.onSuccess(statusCode, headers, response)
                MainActivity.static.updateCall=false
                val intent = Intent(this@ProfetionalDetailsActivity, MainScreenActivity::class.java)
                startActivity(intent)
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?)
            {
                super.onFailure(statusCode, headers, throwable, errorResponse)
                Toast.makeText(this@ProfetionalDetailsActivity,throwable.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun makeHTTPReq(params: JSONObject) {
        val client = AsyncHttpClient()
        var entity= StringEntity(params.toString())
        client.post(this@ProfetionalDetailsActivity,MainActivity.static.professionalDetailURl +MainActivity.static.id,entity,"application/json",object : JsonHttpResponseHandler(){

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?)
            {
                super.onSuccess(statusCode, headers, response)
                val intent = Intent(this@ProfetionalDetailsActivity,
                    EducationalDetailsActivity::class.java)
                startActivity(intent)
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?)
            {
                super.onFailure(statusCode, headers, throwable, errorResponse)
                Toast.makeText(this@ProfetionalDetailsActivity,throwable.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        id=data?.getStringExtra("id")!!.toInt()
        email=data.getStringExtra("email")
    }

}
