package com.example.mycredible.Fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.mycredible.Activities.EducationalDetailsActivity
import com.example.mycredible.Activities.PersonalDetailsActivity
import com.example.mycredible.ControlClasses.EducationalDetailsClass
import com.example.mycredible.ControlClasses.LoginControlClass
import com.example.mycredible.MainActivity
import com.example.mycredible.MainScreenActivity

import com.example.mycredible.R
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.entity.StringEntity
import org.json.JSONObject

class EducationalScreenFragment : Fragment() {

    var organisation:TextView?=null
    var degree:TextView?=null
    var location:TextView?=null
    var start_date:TextView?=null
    var end_date:TextView?=null
    var update:Button?=null
    var activity:Context?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activity = context
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view= inflater.inflate(R.layout.fragment_educational_screen, container, false)

        organisation=view.findViewById(R.id.organization_text_view)
        degree=view.findViewById(R.id.degree_text_view)
        location=view.findViewById(R.id.location_text_view)
        update=view.findViewById(R.id.update_details_button)
        start_date= view.findViewById(R.id.start_date)
        end_date=view.findViewById(R.id.end_date)

        update?.setOnClickListener {
            MainActivity.static.updateCall=true
            val intent = Intent(activity, EducationalDetailsActivity::class.java)
            startActivity(intent)
        }
        displayDetails()

        return view
    }

    private fun displayDetails() {
        val client = AsyncHttpClient()

        client.get(MainActivity.static.educationalDetailsURL+MainActivity.static.id,null,object : JsonHttpResponseHandler(){
                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?)
                {
                    Log.v("response",response.toString())

                    super.onSuccess(statusCode, headers, response)
                    val educationControlClass: EducationalDetailsClass = EducationalDetailsClass(response as JSONObject)
                    degree?.setText(educationControlClass.degree)
                    organisation?.setText(educationControlClass.organisation)
                    location?.setText(educationControlClass.location)
                    start_date?.setText(educationControlClass.start_date)
                    end_date?.setText(educationControlClass.end_date)

                    MainActivity.static.EducationalputId=educationControlClass.put_id
                }

                override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?)
                {
                    super.onFailure(statusCode, headers, throwable, errorResponse)
                }
            }
        )
    }
}
