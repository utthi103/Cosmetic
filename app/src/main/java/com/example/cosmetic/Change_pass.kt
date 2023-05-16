package com.example.cosmetic

import android.app.AlertDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.example.cosmetic.Model.user
import com.example.cosmetic.databinding.ActivityChangePassBinding
import com.example.cosmetic.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*
import java.security.MessageDigest

class Change_pass : AppCompatActivity() {
    private lateinit var binding: ActivityChangePassBinding
    private lateinit var dpRef: DatabaseReference
    private var pass = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePassBinding.inflate(layoutInflater)
        setContentView(binding.root)
      check_image()
        setname()
        changePass()
        binding.saveeBtn.setOnClickListener {
            checkdata()

        }
    }

    private fun checkdata() {
        val curPasset = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.curPasset)
        var pass_curr = curPasset.text.toString()
        val newPass = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.newPasset)
        var pass_new = newPass.text.toString()
        val confirm = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.confPasset)
        val pass_confrim = confirm.text.toString()

        if(pass_confrim.isEmpty()||pass_new.isEmpty()||pass_curr.isEmpty()){
            thongbao("Please fill in form")
        }else{
                pass_curr = hashPassword(pass_curr)

            if(pass_curr==pass){
                if(pass_new==pass_confrim){
                        if(pass_new.length<6){
                            thongbao("Pasword is more than 6 characters ")
                        }else{
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle("Notification")
                            builder.setMessage("Do you want to change your password")
                            builder.setPositiveButton("Agree") { dialog, which ->
                             pass_new = hashPassword(pass_new)
                                val sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                                val iduser = sharedPreferences?.getString("iduser",null)
                                dpRef = FirebaseDatabase.getInstance().getReference("user").child(iduser.toString())
                                dpRef.child("pass").setValue(pass_new).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                            val builder = AlertDialog.Builder(this)
                                            builder.setTitle("Notification")
                                            builder.setMessage("Change password successfully. Please sign in again")
                                            builder.setPositiveButton("Agrees") { dialog, which ->
                                                val intent  = Intent(this, Login::class.java)
                                                startActivity(intent)
                                            }

                                            builder.show()

                                    } else {
                                    }
                                }




                            }
                            builder.setNegativeButton("Cancel") { dialog, which ->
                                dialog.dismiss()

                            }
                            builder.show()
                        }
                }else{
                    thongbao("Password does not match")
                }

            }else{
                thongbao("Wrong password")
            }

        }

    }

    private fun changePass() {
        val curPasset = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.curPasset)
        val password = curPasset.text.toString()
        val sharedPreferences =getSharedPreferences("my_preferences", MODE_PRIVATE)
        val iduser = sharedPreferences?.getString("iduser",null)
        val query = dpRef.orderByChild("id_user").equalTo(iduser)
        dpRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(user::class.java)
                 pass = user?.pass.toString()

            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi nếu có
            }
        })
    }

    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray(Charsets.UTF_8)
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
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


            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi nếu có
            }
        })

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
                            Glide.with(this@Change_pass)
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
    private fun thongbao(thongbao:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Notification")
        builder.setMessage(thongbao)
        builder.setPositiveButton("Agree") { dialog, which ->
        }

        builder.show()
    }

}