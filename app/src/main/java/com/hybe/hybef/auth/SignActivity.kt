package com.hybe.hybef.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.hybe.hybef.MainActivity
import com.hybe.hybef.R
import com.hybe.hybef.databinding.ActivitySignBinding

class SignActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignBinding
    var auth: FirebaseAuth? = null
    var firestore : FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.signBtn2.setOnClickListener {
            var isGoToJoin = true

            val email = binding.emailText2.text.toString()
            val pw = binding.pwdText2.text.toString()
            val pwcheck = binding.pwdcheckText.text.toString()
            val name = binding.nicknameText.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if (pw.isEmpty()) {
                Toast.makeText(this, "패스워드를 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if (!pw.equals(pwcheck)) {
                Toast.makeText(this, "패스워드 값이 다릅니다.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if (pw.length < 6) {
                Toast.makeText(this, "비밀번호를 6자리 이상으로 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if (name.isEmpty()) {
                Toast.makeText(this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show()
                isGoToJoin = false
            }

            if (isGoToJoin) {
                auth!!.createUserWithEmailAndPassword(email, pw)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            var userInfo = AuthData()

                            userInfo.uid = auth?.uid
                            userInfo.userEmail = auth?.currentUser?.email
                            userInfo.userName = binding.nicknameText.text.toString()

                            firestore?.collection("users")?.document(auth?.uid.toString())?.set(userInfo)

                            Toast.makeText(this, "회원가입에 성공하였습니다!", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "회원가입에 실패하였습니다.", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }

    }
}