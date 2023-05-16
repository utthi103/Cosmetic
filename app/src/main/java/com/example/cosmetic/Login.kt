package com.example.cosmetic

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cosmetic.Model.user
import com.example.cosmetic.databinding.ActivityLoginBinding
import com.google.firebase.database.*
import java.security.MessageDigest

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginbtn.setOnClickListener {
            login()
        }
binding.register.setOnClickListener {
    val intent = Intent(this@Login, Register::class.java)
    startActivity(intent)
}

    }

    private fun login() {
        val email = binding.txtemail.text.toString()
        var password = binding.password.text.toString()
        password = hashPassword(password)
        dbRef = FirebaseDatabase.getInstance().getReference("user")
        val query = dbRef.orderByChild("email").equalTo(email)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(user::class.java)
                        if (user?.pass.equals(password) && user?.email.equals(email)) {
                            val idUser = user?.id_user.toString()
                            val sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                            sharedPreferences.edit().putString("iduser", idUser).apply()
//                            isPasswordMatched = true
                            val intent = Intent(this@Login, Welcome::class.java)
//                            startActivity(intent)
                            Toast.makeText(this@Login, "Login successful", Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                        }else{
//                            Toast.makeText(this@Login, "Mật khẩu kh", Toast.LENGTH_SHORT).show()
                            thongbao("Wrong password")
                        }
                    }

                } else {
                    thongbao("Email not exist")

                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray(Charsets.UTF_8)
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
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