package com.dp13.githubuserapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dp13.githubuserapp.R
import com.dp13.githubuserapp.entity.User
import kotlinx.android.synthetic.main.item_row_github_user.view.*

class GithubUserAdapter(private val listGithubUser: ArrayList<User>) : RecyclerView.Adapter<GithubUserAdapter.ListViewHolder>() {
    private var onItemClickCallback: OnItemClickCallBack? = null

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallback = onItemClickCallBack
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_github_user, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listGithubUser.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listGithubUser[position])
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User){
            with(itemView){
                Glide.with(context)
                    .load(user.photo)
                    .apply(RequestOptions().override(55, 55))
                    .into(img_user_photo)
                tv_username.text = user.username
                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(user.username.toString())}
            }
        }
    }

    interface OnItemClickCallBack {
        fun onItemClicked(username: String)
    }
}