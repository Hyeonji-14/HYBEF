package com.hybe.hybef.auth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.hybe.hybef.R

class CommentAdapter(val commentList: MutableList<CommentData>) : BaseAdapter() {
    override fun getCount(): Int {
        return commentList.size
    }

    override fun getItem(position: Int): Any {
        return commentList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if(view == null) {
            view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.comment_list, parent, false)
        }

        val usersName = view?.findViewById<TextView>(R.id.users_name)
        usersName!!.text = commentList[position].commentUsers

        val title = view?.findViewById<TextView>(R.id.title_text)
        title!!.text = commentList[position].commentTitle

        return view!!
    }

}