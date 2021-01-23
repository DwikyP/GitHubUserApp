package com.dp13.githubuserapp

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dp13.githubuserapp.adapter.GithubUserFavoriteAdapter
import com.dp13.githubuserapp.db.UserContract.UserColumns.Companion.CONTENT_URI
import com.dp13.githubuserapp.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_github_user_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class GithubUserFavoriteActivity : AppCompatActivity() {
    private lateinit var adapter: GithubUserFavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_user_favorite)

        showRecyclerList()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        loadAsync()
    }

    override fun onRestart() {
        super.onRestart()
        showRecyclerList()

        loadAsync()
    }

    private fun showRecyclerList(){
        rv_user_favorite.layoutManager = LinearLayoutManager(this)
        rv_user_favorite.setHasFixedSize(true)
        adapter = GithubUserFavoriteAdapter()
        rv_user_favorite.adapter = adapter

        adapter.setOnItemClickCallBack(object: GithubUserFavoriteAdapter.OnItemClickCallBack{
            override fun onItemClicked(username: String) {
                showSelectedUser(username)
            }
        })
    }

    private fun showSelectedUser(username: String){
        val moveUserDetail = Intent(this@GithubUserFavoriteActivity, GithubUserDetailActivity::class.java)
        moveUserDetail.putExtra(GithubUserDetailActivity.EXTRA_USER, username)
        startActivity(moveUserDetail)
    }

    private fun loadAsync(){
        GlobalScope.launch(Dispatchers.Main){
            progressbar.visibility = View.VISIBLE
            val deferredUsers = async(Dispatchers.IO){
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progressbar.visibility = View.INVISIBLE
            val users = deferredUsers.await()
            if(users.size > 0){
                adapter.listUser = users
            }
            else{
                adapter.listUser = ArrayList()
                showSnackbarMessage("Tidak ada data")
            }
        }
    }
    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_user_favorite, message, Snackbar.LENGTH_SHORT).show()
    }
}