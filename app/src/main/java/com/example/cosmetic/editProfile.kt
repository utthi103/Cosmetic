package com.example.cosmetic

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import com.bumptech.glide.Glide
import com.example.cosmetic.Model.user
import com.example.cosmetic.databinding.ActivityChangePassBinding
import com.example.cosmetic.databinding.ActivityEditProfileBinding
import com.google.firebase.database.*

class editProfile : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var dpRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    check_image()
        setname()
        binding.changePasswordTv.setOnClickListener {
            val intent  = Intent(this, Change_pass::class.java)
            startActivity(intent)
        }
        binding.saveeBtn.setOnClickListener {
            changedata()
        }
    }
    private fun thongbao(thongbao:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Notification")
        builder.setMessage(thongbao)
        builder.setPositiveButton("Agree") { dialog, which ->
        }

        builder.show()
    }

    private fun changedata() {
        val address = binding.addressEt.text.toString()
        val phone = binding.phoneEt.text.toString()
        if(address.isEmpty()||phone.isEmpty()){
            thongbao("Please fill in form")
        }else{
            if(phone.length==10){
                val sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                val iduser = sharedPreferences?.getString("iduser",null)
                dpRef = FirebaseDatabase.getInstance().getReference("user").child(iduser.toString())
                val childUpdates = HashMap<String, Any>()
                childUpdates["address"] = address
                childUpdates["phone"] = phone
                dpRef.updateChildren(childUpdates).addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        thongbao("Updated successfull")
                        val intent = Intent(this@editProfile, nav_bottom::class.java)
                        startActivity(intent)
                    } else {
                    }
                }
            }else{
                thongbao("Wrong phone")
            }

        }
    }

    private fun check_image() {
        val sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val iduser = sharedPreferences?.getString("iduser",null)
        dpRef = FirebaseDatabase.getInstance().getReference("user").child(iduser.toString())
        dpRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(user::class.java)
                if (user != null) {
                    val imageUrl = user.image
                    if(imageUrl!=""){
                        val bytes= Base64.decode(imageUrl , Base64.DEFAULT)
                        val bitMap = BitmapFactory.decodeByteArray(bytes,0,bytes.size)
                        // Load ảnh từ đường dẫn và hiển thị trên ImageView bằng Glide
                        binding?.let {
                            Glide.with(this@editProfile)
                                .load(bitMap)
                                .into(it.imgView)
                        }
                    }



                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi nếu có
            }
        })
    }

    private fun setname() {
        val sharedPreferences =getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val iduser = sharedPreferences?.getString("iduser",null)
        dpRef = FirebaseDatabase.getInstance().getReference("user").child(iduser.toString())
        dpRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(user::class.java)

                val email = user?.email
                binding?.title?.text = email
                binding.addressEt.setText(user!!.address)
                binding.phoneEt.setText(user!!.phone)

            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi nếu có
            }
        })

    }
}