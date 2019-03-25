package com.example.mycredible.Activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.*
import com.example.mycredible.MainActivity
import com.example.mycredible.MainActivity.static.id
import com.example.mycredible.MainScreenActivity
import com.example.mycredible.R
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.entity.StringEntity
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import kotlinx.android.synthetic.main.activity_personal_details.view.*


class PersonalDetailsActivity : AppCompatActivity() {

    private val imageUri = "content://media/internal/images/media"

    var name:EditText?=null
    var mobile:EditText?=null
    var location:EditText?=null
    var links:EditText?=null
    var save: Button?=null
    var skills:EditText?=null
    var profilepic: ImageView?=null

    var email:EditText?=null

    private var profilePicBitmap: Bitmap? = null
    private var baos: ByteArrayOutputStream? = null
    private var imageByteArray: ByteArray? = null
    private var encodedImage: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_details)

        name= findViewById(R.id.full_name_edit_text)
        mobile=findViewById(R.id.mobile_edit_text)
        location=findViewById(R.id.location_edit_text)
        links=findViewById(R.id.links_edit_text)
        save=findViewById(R.id.save_button)
        skills=findViewById(R.id.skills_edit_text)
        profilepic=findViewById(R.id.profilePic)
        email=findViewById(R.id.personal_email_edit_text)
    }

    override fun onResume() {
        super.onResume()
        var Uname:String?=null
        var Uemail:String?=null
        var Umobile:String?=null
        var Ulocation:String?=null
        var Ulinks:String?=null
        var Uskills:String?=null

        profilepic?.setOnClickListener{
            val intent =Intent(Intent.ACTION_PICK, Uri.parse(imageUri))
            startActivityForResult(intent,1)
        }


        save?.setOnClickListener {
            Uname=name?.getText().toString()
            Umobile=mobile?.getText().toString()
            Ulocation=location?.getText().toString()
            Ulinks=links?.getText().toString()
            Uskills=skills?.getText().toString()
            Uemail=email?.text.toString()
            putPersonalDetails(Uname,Umobile,Ulocation,Ulinks,id,Uskills,Uemail)
        }

    }

    private fun putPersonalDetails(
        uname: String?, umobile: String?, ulocation: String?, ulinks: String?, id: Int, uskills: String?, uemail: String?) {

        var params = JSONObject()

        params.put("skills", uskills)
        params.put("mobile_no",umobile)
        params.put("name",uname)
        params.put("links",ulinks)
        params.put("location",ulocation)
        params.put("email",uemail)

        if(MainActivity.static.updateCall)
        {
            updateHTTPReq(params)
        }
        else
            makeHTTPReq(params)
    }

    private fun updateHTTPReq(params: JSONObject) {
        val client = AsyncHttpClient()
        val entity = StringEntity(params.toString())
        client.put(this@PersonalDetailsActivity,
            MainActivity.static.personalDetailUrl +MainActivity.static.id,entity,
            "application/json",object : JsonHttpResponseHandler(){

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?)
            {
                super.onSuccess(statusCode, headers, response)
                MainActivity.static.updateCall=false
                val intent = Intent(this@PersonalDetailsActivity, MainScreenActivity::class.java)
                startActivity(intent)
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?)
            {
                super.onFailure(statusCode, headers, throwable, errorResponse)
                Toast.makeText(this@PersonalDetailsActivity,throwable.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun makeHTTPReq(params: JSONObject) {

        val client = AsyncHttpClient()
        val entity = StringEntity(params.toString())
        client.post(this@PersonalDetailsActivity,
            MainActivity.static.personalDetailUrl + MainActivity.static.id,
            entity,"application/json",object : JsonHttpResponseHandler(){

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?)
            {
                super.onSuccess(statusCode, headers, response)
                val intent = Intent(this@PersonalDetailsActivity,
                    ProfetionalDetailsActivity::class.java)
                startActivity(intent)
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?)
            {
                super.onFailure(statusCode, headers, throwable, errorResponse)
                Toast.makeText(this@PersonalDetailsActivity,throwable.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {

            val picturePath = getPath(this, data?.data!!)

            profilePicBitmap = BitmapFactory.decodeFile(picturePath)
            baos = ByteArrayOutputStream()
            profilePicBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            imageByteArray = baos?.toByteArray()
            encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT)

            var params = JSONObject()
            params.put("photo",encodedImage)
            params.put("uid",MainActivity.static.id)

            val client = AsyncHttpClient()
            val entity = StringEntity(params.toString())
            client.post(this@PersonalDetailsActivity,
                MainActivity.static.profilePicUploadURL,
                entity,"application/json",object : JsonHttpResponseHandler(){

                    override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?)
                    {
                        super.onSuccess(statusCode, headers, response)
                        Toast.makeText(this@PersonalDetailsActivity, response?.getString("status_message"),Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?)
                    {
                        super.onFailure(statusCode, headers, throwable, errorResponse)
                        Toast.makeText(this@PersonalDetailsActivity,throwable.toString(), Toast.LENGTH_SHORT).show()
                    }
                })


        }
    }

    fun getPath(context: Context, uri: Uri): String {
        var result: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, proj, null, null, null)
        cursor.moveToFirst()
        val column_index = cursor.getColumnIndexOrThrow(proj[0])
        result = cursor.getString(column_index)
        cursor.close()

        return result
    }
}
