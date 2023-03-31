package com.hybe.hybef.artist

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.hybe.hybef.R
import com.hybe.hybef.auth.CommentAdapter
import com.hybe.hybef.auth.CommentData
import com.hybe.hybef.auth.FBRef
import com.hybe.hybef.databinding.ActivityFromisBinding
import java.util.*

class FromisActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFromisBinding
    private lateinit var key: String
    private lateinit var commentAdapter: CommentAdapter
    private val commentDataList = mutableListOf<CommentData>()

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val user = auth.currentUser

    private val images = listOf(
        R.drawable.fromis_rom,
        R.drawable.fromis_ha,
        R.drawable.fromis_jiwon,
        R.drawable.fromis_jisun,
        R.drawable.fromis_seoyeon,
        R.drawable.fromis_young,
        R.drawable.fromis_na,
        R.drawable.fromis_jiheon,
    )

    val urls = listOf(
        "https://youtu.be/NGmhPgx4MwA",
        "https://youtu.be/yR8Dz7St6Jc",
        "https://youtu.be/NFKhp4ftcEY",
        "https://youtu.be/QI8KhYXCIoM",
        "https://youtu.be/mOrQtNmfSJw",
        "https://youtu.be/OwXoRfjQDlQ",
        "https://youtu.be/4GllWVB9DDs",
        "https://youtu.be/OEluoNnoWFM",
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fromis)
        Glide.with(this).load(R.raw.frocd).into(binding.fromisImg)
        val view = binding.viewPager
        view.adapter = ImageAdapter(images, urls)

        val textView = binding.titleText
        val typing = Timer()
        val fullText = textView.text
        var currentText = ""

        typing.schedule(object : TimerTask() {
            override fun run() {
                if (currentText.length < fullText.length) {
                    currentText += fullText[currentText.length]
                    runOnUiThread {
                        textView.text = currentText
                    }
                } else {
                    typing.cancel()
                }
            }
        }, 0, 150)

        key = intent.getStringExtra("key").toString()

        binding.commentButton.setOnClickListener {
            var isGoToComment = true
            val edit = binding.commentEdit.text.toString()

            if (edit.isEmpty()) {
                Toast.makeText(this, "댓글을 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToComment = false
            }
            if (isGoToComment) {
                insertComment(key)
            }
        }

        commentAdapter = CommentAdapter(commentDataList)
        binding.commentLv.adapter = commentAdapter

        getCommentData(key)
    }
    fun getCommentData(key: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                commentDataList.clear()

                for (dataModel in snapshot.children) {
                    val item = dataModel.getValue(CommentData::class.java)
                    commentDataList.add(item!!)
                }
                commentAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "loadPost:onCancelled", error.toException())
            }
        }
        FBRef.fromisRef.child(key).addValueEventListener(postListener)
    }

    fun insertComment(key: String) {
        if (user != null) {
            val userRef = db.collection("users").document(user.uid)
            userRef.get().addOnSuccessListener { result ->
                if (result != null) {
                    val name = result.getString("userName")
                    FBRef.fromisRef.child(key).push().setValue(
                        CommentData(
                            name, binding.commentEdit.text.toString()
                        )
                    )
                    Toast.makeText(this, "댓글을 입력하였습니다.", Toast.LENGTH_LONG).show()
                    binding.commentEdit.setText("")
                }
            }.addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error : ", exception)
            }
        }
    }

    class ImageAdapter(
        private val images: List<Int>, private val urls: List<String>
    ) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imageView: ImageView = itemView.findViewById(R.id.image_view)

            init {
                imageView.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val url = urls[position]
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        imageView.context.startActivity(intent)
                    }
                }
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int
        ): ImageAdapter.ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.imageView.setImageResource(images[position])
        }

        override fun getItemCount(): Int {
            return images.size
        }
    }
}