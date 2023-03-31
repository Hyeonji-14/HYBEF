package com.hybe.hybef.auth

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRef {

    companion object {
        private val database = Firebase.database

        val btsRef = database.getReference("bts_comment")
        val txtRef = database.getReference("txt_comment")
        val svtRef = database.getReference("svt_comment")
        val hypenRef = database.getReference("hypen_comment")
        val minhyunRef = database.getReference("minhyun_comment")
        val baekhoRef = database.getReference("baekho_comment")
        val fromisRef = database.getReference("fromis_comment")
        val lessRef = database.getReference("less_comment")
        val newjeansRef = database.getReference("newjeans_comment")

    }
}