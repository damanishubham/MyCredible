package com.example.mycredible.Fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.mycredible.Activities.EducationalDetailsActivity
import com.example.mycredible.Activities.PersonalDetailsActivity
import com.example.mycredible.ControlClasses.PersonalDetailsClass
import com.example.mycredible.ControlClasses.ProfessionalDetailsClass
import com.example.mycredible.MainActivity

import com.example.mycredible.R
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class PersonalScreenFragment : Fragment() {

    var name: TextView? = null
    var mobile: TextView? = null
    var location: TextView? = null
    var links: TextView? = null
    var update: Button? = null
    var skills: TextView? = null

    var activity: Context? = null

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
        val view = inflater.inflate(R.layout.fragment_personal_screen, container, false)

        name = view.findViewById(R.id.personal_name_text_view)
        mobile = view.findViewById(R.id.personal_mobile_text_view)
        location = view.findViewById(R.id.personal_location_text_view)
        links = view.findViewById(R.id.personal_links_text_view)
        skills = view.findViewById(R.id.personal_skills_text_view)
        update = view.findViewById(R.id.personal_update_details_button)

        update?.setOnClickListener {
            MainActivity.static.updateCall = true
            val intent = Intent(activity, PersonalDetailsActivity::class.java)
            startActivity(intent)
        }

        displayDetails()

        return view
    }

    private fun displayDetails() {
        val client = AsyncHttpClient()

        client.get(MainActivity.static.personalDetailUrl + MainActivity.static.id, null, object : JsonHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?) {
                    super.onSuccess(statusCode, headers, response)

                    Log.v("response", response.toString())

                    val personalDetailControlClass: PersonalDetailsClass = PersonalDetailsClass(response as JSONObject)

                    name?.setText(personalDetailControlClass.fullname)
                    mobile?.setText(personalDetailControlClass.mobile)
                    location?.setText(personalDetailControlClass.location)
                    links?.setText(personalDetailControlClass.links)

                    skills?.setText(personalDetailControlClass.skills)

                    MainActivity.static.PersonalputId = personalDetailControlClass.put_id
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    throwable: Throwable?,
                    errorResponse: JSONObject?
                ) {
                    super.onFailure(statusCode, headers, throwable, errorResponse)
                    Toast.makeText(activity, throwable.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}


