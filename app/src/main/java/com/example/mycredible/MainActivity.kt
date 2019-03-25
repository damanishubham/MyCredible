package com.example.mycredible

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.mycredible.Activities.PersonalDetailsActivity
import com.example.mycredible.ControlClasses.LoginControlClass
import com.example.mycredible.MainActivity.static.id
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.entity.ContentType
import org.json.JSONObject
import cz.msebera.android.httpclient.entity.StringEntity


class MainActivity : AppCompatActivity() {
    object static{

        var pref="MyPrefs"
        var baseUrl="http://139.59.65.145:9090"
        var serverUrl:String= "$baseUrl/test"
        var userLoginUrl= "$baseUrl/user/login"
        var userSignupUrl= "$baseUrl/user/signup"
        var personalDetailUrl= "$baseUrl/user/personaldetail/"
        var professionalDetailURl= "$baseUrl/user/professionaldetail/"
        var educationalDetailsURL= "$baseUrl/user/educationdetail/"
        var profilePicUploadURL= "$baseUrl/user/personaldetail/pp/post"
        var profilePicDownloadURL= "$baseUrl/user/personaldetail/profilepic/"

        var id:Int=0
        var email:String?=null

        var updateCall=false

        var PersonalputId=0
        var ProfessionalputId=0
        var EducationalputId=0
    }
    var email:EditText?=null
    var password:EditText?=null
    var login:Button?=null
    var signup:Button?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        email=findViewById(R.id.loginEmail)
        password=findViewById(R.id.loginPassword)
        login= findViewById(R.id.loginButton)
        signup=findViewById(R.id.signupButton)
    }

    override fun onResume() {
        super.onResume()

        var userEmail:String?=null
        var userPassword: String?=null

        serverTest()


        login?.setOnClickListener {
            userEmail=email?.getText().toString()
            userPassword=password?.getText().toString()
            getLoginValues(userEmail.toString(),userPassword.toString())
        }

        signup?.setOnClickListener {
            userEmail=email?.getText().toString()
            userPassword=password?.getText().toString()
            getSignUpValues(userEmail.toString(),userPassword.toString())
        }

    }

    fun getLoginValues(email:String,password:String)
    {

        var params = JSONObject()

        params.put("email", email)
        params.put("password",password)

        makeLoginReq(params)
    }

    fun getSignUpValues(email:String,password:String)
    {
        var params = JSONObject()
        params.put("email", email)
        params.put("password",password)

        makeSignUpReq(params)
    }

    fun serverTest()
    {
        val client = AsyncHttpClient()
        client.get(static.serverUrl,null,object : JsonHttpResponseHandler(){

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?)
            {
                super.onSuccess(statusCode, headers, response)
                Toast.makeText(this@MainActivity,"Server Up",Toast.LENGTH_SHORT).show()

            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?)
            {
                super.onFailure(statusCode, headers, throwable, errorResponse)
                Toast.makeText(this@MainActivity, "Server Down: $throwable",Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun makeLoginReq(params : JSONObject)
    {
        val client = AsyncHttpClient()

        val entity = StringEntity(params.toString())

        client.get(
            this@MainActivity, static.userLoginUrl, entity, "application/json",object : JsonHttpResponseHandler(){

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?)
            {
                super.onSuccess(statusCode, headers, response)
                val loginController: LoginControlClass =
                    LoginControlClass(response as JSONObject)
                static.id =loginController.returnId()
                static.email=loginController.returnEmail()

                Log.v("original id",static.id.toString())

                val intent = Intent(this@MainActivity, MainScreenActivity::class.java)
                startActivity(intent)
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?)
            {
                super.onFailure(statusCode, headers, throwable, errorResponse)
                Log.v("fuckedup",throwable.toString())
                Toast.makeText(this@MainActivity,"No such user found!",Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun makeSignUpReq(params : JSONObject)
    {
        val client = AsyncHttpClient()
        val entity = StringEntity(params.toString())
        var x= ContentType.APPLICATION_JSON.getMimeType()
        client.post(
            this@MainActivity,static.userSignupUrl, entity, "application/json",object : JsonHttpResponseHandler(){
                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?)
                {
                    super.onSuccess(statusCode, headers, response)
                    val loginController: LoginControlClass = LoginControlClass(response as JSONObject)
                    static.id =loginController.returnId()
                    static.email=loginController.returnEmail()

                    val intent = Intent(this@MainActivity, PersonalDetailsActivity::class.java)
                    startActivity(intent)
                    this@MainActivity.finish()
                }

                override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?)
                {
                    super.onFailure(statusCode, headers, throwable, errorResponse)
                    Log.v("fucjed",throwable.toString())
                    Toast.makeText(this@MainActivity,"No such user found!",Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

}
