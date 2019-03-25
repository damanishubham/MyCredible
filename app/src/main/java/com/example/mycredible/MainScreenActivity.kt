package com.example.mycredible

import android.content.Intent
import android.net.Uri
import android.support.design.widget.TabLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.example.mycredible.Activities.EducationalDetailsActivity
import com.example.mycredible.ControlClasses.EducationalDetailsClass
import com.example.mycredible.Fragments.EducationalScreenFragment
import com.example.mycredible.Fragments.PersonalScreenFragment
import com.example.mycredible.Fragments.ProfessionalScreenFragment
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.squareup.picasso.Picasso
import cz.msebera.android.httpclient.Header

import kotlinx.android.synthetic.main.activity_main_screen.*
import org.json.JSONObject

class MainScreenActivity : AppCompatActivity() {

    var profilePicture:ImageView?=null


    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        profilePicture=findViewById(R.id.displayPic)
        getprofilepic()

        setSupportActionBar(toolbar)

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)


        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

    }

    private fun getprofilepic() {
        val uri = Uri.parse(MainActivity.static.profilePicDownloadURL + MainActivity.static.id)
        Log.v("profilepic","profilepicfunc called")
        Log.v("profilepic",MainActivity.static.profilePicDownloadURL + MainActivity.static.id)
        Picasso.get().load(uri).resize(100,100).into(profilePicture)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main_screen, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (id == R.id.action_Logout) {
            val intent = Intent(this@MainScreenActivity, MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }
        else if(id==R.id.action_delete)
        {
            val client = AsyncHttpClient()

            client.delete(MainActivity.static.educationalDetailsURL+MainActivity.static.id,null,object : JsonHttpResponseHandler()
            {
                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?)
                {
                    super.onSuccess(statusCode, headers, response)
                }

                override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?)
                {
                    super.onFailure(statusCode, headers, throwable, errorResponse)
                }
            })

            client.delete(MainActivity.static.professionalDetailURl+MainActivity.static.id,null,object : JsonHttpResponseHandler()
            {
                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?)
                {
                    super.onSuccess(statusCode, headers, response)
                }

                override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?)
                {
                    super.onFailure(statusCode, headers, throwable, errorResponse)
                }
            })

            Handler().postDelayed({
                Toast.makeText(this@MainScreenActivity,"User Data Deleted successfully",Toast.LENGTH_LONG).show()
            },500)
            val intent = Intent(this@MainScreenActivity, MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }

        return super.onOptionsItemSelected(item)
    }


    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
           when(position){
               0->{
                   val personalScreenFragment: PersonalScreenFragment = PersonalScreenFragment()
                   return personalScreenFragment
               }
               1->{
                   val educationalScreenFragment: EducationalScreenFragment=EducationalScreenFragment()
                   return educationalScreenFragment
               }
               2->{
                   val professionalScreenFragment: ProfessionalScreenFragment=ProfessionalScreenFragment()
                   return professionalScreenFragment
               }
               else->
               {
                   val professionalScreenFragment: ProfessionalScreenFragment=ProfessionalScreenFragment()
                   return professionalScreenFragment
               }
           }
        }

        override fun getCount(): Int {
            return 3
        }

    }

}
