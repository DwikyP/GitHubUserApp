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

class GithubUserFavoriteAdapter(): RecyclerView.Adapter<GithubUserFavoriteAdapter.ListViewHolder>() {
    var listUser = ArrayList<User>()
        set(listNotes) {
            if (listNotes.size > 0) {
                this.listUser.clear()
            }
            this.listUser.addAll(listNotes)
            notifyDataSetChanged()
        }

    private var onItemClickCallback: OnItemClickCallBack? = null

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallback = onItemClickCallBack
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_github_user, parent, false)
        return ListViewHolder(view)
    }
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])
    }
    override fun getItemCount(): Int = this.listUser.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            with(itemView){
                tv_username.text = user.username
                Glide.with(context)
                    .load(user.photo)
                    .apply(RequestOptions.overrideOf(55,55))
                    .into(img_user_photo)
                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(user.username.toString())}
            }
        }
    }

    interface OnItemClickCallBack {
        fun onItemClicked(username: String)
    }
}