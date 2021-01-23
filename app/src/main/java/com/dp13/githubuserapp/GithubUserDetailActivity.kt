package com.dp13.githubuserapp

import android.content.ContentValues
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.dp13.githubuserapp.adapter.SectionsPagerAdapter
import com.dp13.githubuserapp.db.UserContract
import com.dp13.githubuserapp.db.UserContract.UserColumns.Companion.CONTENT_URI
import com.dp13.githubuserapp.db.UserHelper
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_github_user_detail.*
import kotlinx.android.synthetic.main.activity_github_user_favorite.*
import org.json.JSONObject

class GithubUserDetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_USER = "extra_user"
    }

    private lateinit var userHelper: UserHelper
    private lateinit var uriWithId: Uri
    private lateinit var uriWithUsername: Uri
    private var userId: Int = 0
    private lateinit var username: String
    private lateinit var avatar: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_user_detail)


        username = intent.getStringExtra(EXTRA_USER).toString()
        getUserData(username)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.username = username
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)

        supportActionBar?.elevation = 0f

        //code for user favorite status
        var statusFavorite = false
        uriWithUsername = Uri.parse(CONTENT_URI.toString() + "/" + username)
        val resultUser = contentResolver.query(uriWithUsername,null, null, null, null)
        if (resultUser != null) {
            if(resultUser.count > 0 ){
                statusFavorite = true
                resultUser.close()
            }
        }

        setStatusFavorite(statusFavorite)

        fab_favorite.setOnClickListener {
            statusFavorite = !statusFavorite
            uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + userId)
            //code
            if (statusFavorite){
                val values = ContentValues()
                values.put(UserContract.UserColumns._ID, userId)
                values.put(UserContract.UserColumns.COLUMN_NAME_USERNAME, username)
                values.put(UserContract.UserColumns.COLUMN_NAME_AVATAR_URL, avatar)

                contentResolver.insert(CONTENT_URI, values)
                Toast.makeText(this@GithubUserDetailActivity, "Berhasil menambahkan user favorite", Toast.LENGTH_SHORT).show()
            }
            else{
                contentResolver.delete(uriWithId, null, null)
                Toast.makeText(this@GithubUserDetailActivity, "Berhasil menghapus user favorite", Toast.LENGTH_SHORT).show()
            }
            setStatusFavorite(statusFavorite)
        }
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            fab_favorite.setImageResource(R.drawable.ic_baseline_favorite_true_24)
        } else {
            fab_favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }


    fun getUserData(username: String) {
        progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username"
        client.addHeader("Authorization", BuildConfig.GITHUB_TOKEN)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                try {
                    val jsonObject = JSONObject(result)
                    userId = jsonObject.getInt("id")
                    avatar = jsonObject.getString("avatar_url")
                    val repository = jsonObject.getInt("public_repos")
                    val location = jsonObject.getString("location")
                    val company = jsonObject.getString("company")
                    val following = jsonObject.getInt("following")
                    val followers = jsonObject.getInt("followers")
                    tv_username.text = username
                    tv_repository.text = repository.toString()
                    tv_location.text = location
                    tv_company.text = company
                    tv_following.text = following.toString()
                    tv_followers.text = followers.toString()
                    Glide.with(this@GithubUserDetailActivity)
                        .load(avatar)
                        .into(img_item_photo)
                } catch (e: Exception) {
                    Toast.makeText(this@GithubUserDetailActivity, e.message, Toast.LENGTH_SHORT)
                        .show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@GithubUserDetailActivity, errorMessage, Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }
}