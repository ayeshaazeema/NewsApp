package com.ayeshaazeema.newsapp.activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayeshaazeema.newsapp.NewsAdapter
import com.ayeshaazeema.newsapp.R
import com.ayeshaazeema.newsapp.model.ResponseNews
import com.ayeshaazeema.newsapp.service.RetrofitConfig
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    val date = getCurrentDateTime()
    var refUsers: DatabaseReference? = null
    var firebaseUser: FirebaseUser? = null

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    companion object {
        fun getLaunchService(from: Context) = Intent(from, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        ib_profile_main.setOnClickListener(this)
        tv_date_main.text = date.toString("dd/MM/yyyy")
        getNews()

        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser!!.uid)
        refUsers!!.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                for (p0 in snapshot.children) {
                    val photo = snapshot.child("photo").value.toString()

                    Glide.with(this@MainActivity).load(photo).into(iv_profile_main)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getNews() {
        val country = "id"
        val apiKey = "464f2ccd81db443f9d0c492e9dc0a64d"

        val loading = ProgressDialog.show(this, "Request Data", "Loading...")
        RetrofitConfig.getInstance().getNewsData(country, apiKey).enqueue(
            object : Callback<ResponseNews> {

                override fun onResponse(
                    call: Call<ResponseNews>,
                    response: Response<ResponseNews>
                ) {
                    Log.d("Response", "Success" + response.body()?.articles)
                    loading.dismiss()
                    if (response.isSuccessful) {
                        val status = response.body()?.status
                        if (status.equals("ok")) {
                            Toast.makeText(this@MainActivity, "Data Success!", Toast.LENGTH_SHORT)
                                .show()
                            val newsData = response.body()?.articles
                            val newsAdapter = NewsAdapter(this@MainActivity, newsData)

                            rv_main.adapter = newsAdapter
                            rv_main.layoutManager = LinearLayoutManager(this@MainActivity)

                            val dataHighlight = response.body()
                            Glide.with(this@MainActivity)
                                .load(dataHighlight?.articles?.component5()?.urlToImage)
                                .centerCrop().into(iv_main_banner)

                            tv_highlight.text = dataHighlight?.articles?.component5()?.title
                            tv_name_author.text = dataHighlight?.articles?.component5()?.author
                            tv_date_main.text = dataHighlight?.articles?.component5()?.publishedAt

                        } else {
                            Toast.makeText(this@MainActivity, "Data Failed!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseNews>, t: Throwable) {
                    Log.d("Response", "Failed :" + t.localizedMessage)
                    loading.dismiss()
                }
            }
        )
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ib_profile_main -> startActivity(Intent(ProfileActivity.getLaunchService(this)))
        }
    }
}