package com.hybe.hybef

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.hybe.hybef.artist.*
import com.hybe.hybef.auth.LoginActivity
import com.hybe.hybef.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var googleSignInClient: GoogleSignInClient

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val user = auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        FirebaseApp.initializeApp(this)

        // google logout
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // user ID 불러오기
        if (user != null) {
            val userRef = db.collection("users").document(user.uid)
            userRef.get()
                .addOnSuccessListener { result ->
                    if (result != null) {
                        val name = result.getString("userName")

                        binding.welcomeId.text = name
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error : ", exception)
                }
        }

        binding.included.logout.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show()

            googleSignInClient.signOut().addOnCompleteListener {
                finish()
            }
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }



        binding.included.menuBtn.setOnClickListener {
            setDrawerLayout(binding.drawerLayoutMain, binding.appbarView.navView)
        }

        binding.appbarView.navView.setNavigationItemSelectedListener(this)
    }


    private fun setDrawerLayout(drawerLayout: DrawerLayout, navigationView: NavigationView) {
        drawerLayout.openDrawer(Gravity.RIGHT)
        drawerLayout.let {
            if (it.isDrawerOpen(GravityCompat.START)) {
                it.closeDrawer(GravityCompat.START)
            }
        }
    }

    //menu Item Intent
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bts -> {
                val intent = Intent(this, BtsActivity::class.java)
                startActivity(intent)

                return true
            }

            R.id.txt -> {
                val intent = Intent(this, TxtActivity::class.java)
                startActivity(intent)

                return true
            }

            R.id.svt -> {
                val intent = Intent(this, SvtActivity::class.java)
                startActivity(intent)

                return true
            }

            R.id.enhypen -> {
                val intent = Intent(this, EnhypenActivity::class.java)
                startActivity(intent)

                return true
            }

            R.id.baekho -> {
                val intent = Intent(this, BaekhoActivity::class.java)
                startActivity(intent)

                return true
            }

            R.id.minhyun -> {
                val intent = Intent(this, MinhyunActivity::class.java)
                startActivity(intent)

                return true
            }

            R.id.fromis -> {
                val intent = Intent(this, FromisActivity::class.java)
                startActivity(intent)

                return true
            }

            R.id.lessrafim -> {
                val intent = Intent(this, LessActivity::class.java)
                startActivity(intent)

                return true
            }

            R.id.newjeans -> {
                val intent = Intent(this, NewjeansActivity::class.java)
                startActivity(intent)

                return true
            }

            R.id.del -> {
                deleteld()

                return true
            }

            else -> false
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteld() {
        if (user != null) {
            db.collection("users")
                .document(user.uid)
                .delete()
                .addOnSuccessListener {
                    user.delete()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                                Toast.makeText(this, "회원 탈퇴가 완료되었습니다.", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(this, "탈퇴에 실패하였습니다.", Toast.LENGTH_LONG).show()
                            }
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "데이터 삭제 실패", Toast.LENGTH_SHORT).show()
                }
        } else if (auth.currentUser?.providerData?.find { it.providerId == GoogleAuthProvider.PROVIDER_ID } != null) {
            auth.currentUser?.delete()?.addOnSuccessListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                Toast.makeText(this, "회원 탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()

            }?.addOnFailureListener { e ->
                Toast.makeText(this, "탈퇴에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "로그인이 되어 있지 않습니다.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayoutMain.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayoutMain.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}