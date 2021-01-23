package com.dp13.githubuserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.dp13.githubuserapp.adapter.GithubUserAdapter
import com.dp13.githubuserapp.entity.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchView.queryHint = "Masukkan Username"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                getListUser(query)
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                getListUser(newText)
                return false
            }
        })
    }

    private fun showRecyclerList(Users: ArrayList<User>){
        rv_github_user.layoutManager = LinearLayoutManager(this)
        val githubUserAdapter = GithubUserAdapter(Users)
        rv_github_user.adapter = githubUserAdapter

       githubUserAdapter.setOnItemClickCallBack(object: GithubUserAdapter.OnItemClickCallBack{
            override fun onItemClicked(username: String) {
                showSelectedUser(username)
            }
        })
    }

   private fun showSelectedUser(username: String){
        val moveUserDetail = Intent(this@MainActivity, GithubUserDetailActivity::class.java)
        moveUserDetail.putExtra(GithubUserDetailActivity.EXTRA_USER, username)
        startActivity(moveUserDetail)
    }

    fun getListUser(username: String) {
        progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$username"
        client.addHeader("Authorization", BuildConfig.GITHUB_TOKEN)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {
                progressBar.visibility = View.INVISIBLE
                val listUser = ArrayList<User>()
                val result = String(responseBody)
                try {
                    val jsonObject = JSONObject(result)
                    val list = jsonObject.getJSONArray("items")
                    for (i in 0 until list.length()) {
                        val userObject = list.getJSONObject(i)
                        val user = User()
                        user.username = userObject.getString("login")
                        user.photo = userObject.getString("avatar_url")
                        listUser.add(user)
                    }
                    showRecyclerList(listUser)
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray, error: Throwable) {
                progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_user_favorite -> {
                val moveToFavoriteUser = Intent(this, GithubUserFavoriteActivity::class.java)
                startActivity(moveToFavoriteUser)
                return true
            }
            R.id.action_setting -> {
                val moveToSetting = Intent(this, GithubUserSettingActivity::class.java)
                startActivity(moveToSetting)
                return true
            }
            else -> return false
        }
    }
}