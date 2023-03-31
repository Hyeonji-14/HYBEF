package com.hybe.hybef.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class AuthData(
    var userName: String? = null,
    var userEmail: String? = null,
    var uid : String? = null,
)