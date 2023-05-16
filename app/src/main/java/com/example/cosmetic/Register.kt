package com.example.cosmetic

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.widget.EditText
import android.widget.Toast
import com.example.cosmetic.Model.user
import com.example.cosmetic.databinding.ActivityMainBinding
import com.example.cosmetic.databinding.ActivityRegisterBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.security.MessageDigest

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var dbRef: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        lay gia tri
        binding.btnRegister.setOnClickListener{
            pass()
            checkvalue()
        }

        binding.login.setOnClickListener {
            val intent = Intent(this@Register, Login::class.java)
            startActivity(intent)
        }
    }

    private fun pass() {

    }

    private fun checkvalue() {

       var email = binding.email.text.toString()
        var  pass = binding.password.text.toString()
        var confirm = binding.confirm.text.toString()
        var address = binding.address.text.toString()
        var phone = binding.phone.text.toString()
        var image = ""
       if(email.isEmpty()||pass.isEmpty()||confirm.isEmpty()||address.isEmpty()||phone.isEmpty()){
           thongbao("Please fill in form")
       }else{
           if(pass!=confirm){
               thongbao("Password not match")
           }else{
               if(pass.length<6){
                   thongbao("Password is more than 6 characters")
               }else{
                   if(phone.length==10){
                       pass = hashPassword(pass)
                       dbRef = FirebaseDatabase.getInstance().getReference("user")
                       val Id_user = dbRef.push().key!!
                       val user = user(Id_user, email, pass, address,phone,image)
                       dbRef.child(Id_user).setValue(user).addOnCompleteListener {

                           val builder = AlertDialog.Builder(this)
                           builder.setTitle("Notification")
                           builder.setMessage("Register successful, please login to continue")
                           builder.setPositiveButton("Đồng ý") { dialog, which ->
                               val intent  = Intent(this, Login::class.java)
                               startActivity(intent)
                           }
//        }
                           builder.show()

                       }.addOnFailureListener { err ->
                           Toast.makeText(this@Register, "ERR ${err.message}", Toast.LENGTH_SHORT).show()
                       }
                   }else{
                       thongbao("Wrong phone")
                   }
               }
           }
       }

    }

    private fun thongbao(thongbao:String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Notification")
        builder.setMessage(thongbao)
        builder.setPositiveButton("Agree") { dialog, which ->
            // Xử lý sự kiện khi người dùng nhấn nút "Đồng ý"
        }
//        builder.setNegativeButton("Hủy bỏ") { dialog, which ->
//            // Xử lý sự kiện khi người dùng nhấn nút "Hủy bỏ"
//        }
        builder.show()
    }
    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray(Charsets.UTF_8)
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }

}