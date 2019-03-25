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
import com.example.mycredible.Activities.EducationalDetailsActivity
import com.example.mycredible.Activities.ProfetionalDetailsActivity
import com.example.mycredible.ControlClasses.EducationalDetailsClass
import com.example.mycredible.ControlClasses.ProfessionalDetailsClass
import com.example.mycredible.MainActivity

import com.example.mycredible.R
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject


class ProfessionalScreenFragment : Fragment() {

    var organisation: TextView?=null
    var designation: TextView?=null
    var start_date: TextView?=null
    var end_date: TextView?=null
    var update: Button?=null
    var activity: Context?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activity = context

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_professional_screen, container, false)
        organisation=view.findViewById(R.id.professional_organization_text_view)
        designation=view.findViewById(R.id.professional_designation_text_view)
        start_date= view.findViewById(R.id.professional_start_date_text_view)
        end_date=view.findViewById(R.id.professiona_end_date_text_view)
        update=view.findViewById(R.id.professional_update_details_button)

        displayDetails()

        update?.setOnClickListener {
            MainActivity.static.updateCall=true
            val intent = Intent(activity, ProfetionalDetailsActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun displayDetails() {
        val client = AsyncHttpClient()

        client.get(MainActivity.static.professionalDetailURl+MainActivity.static.id,null,object : JsonHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?)
            {
                super.onSuccess(statusCode, headers, response)
                Log.v("response",response.toString())
                val professionalControlClass: ProfessionalDetailsClass = ProfessionalDetailsClass(response as JSONObject)

                organisation?.setText(professionalControlClass.organisation)
                start_date?.setText(professionalControlClass.start_date)
                end_date?.setText(professionalControlClass.end_date)
                designation?.setText(professionalControlClass.designation)

                MainActivity.static.ProfessionalputId=professionalControlClass.put_id
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?)
            {
                super.onFailure(statusCode, headers, throwable, errorResponse)
            }
        }
        )
    }


}
