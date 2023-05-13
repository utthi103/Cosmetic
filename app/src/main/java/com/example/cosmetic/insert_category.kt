package com.example.cosmetic

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.cosmetic.Model.danhmucSP
import com.example.cosmetic.databinding.ActivityInsertCategoryBinding
import com.example.cosmetic.databinding.ActivityInsertspBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream

class insert_category : AppCompatActivity() {

    private lateinit var binding: ActivityInsertCategoryBinding
    private lateinit var db: DatabaseReference
    var simage: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = FirebaseDatabase.getInstance().getReference("danhmuc")
        binding.btnsave.setOnClickListener {
            val id_danhmuc = db.push().key!!
            val ten = binding.tendanhmuc.text.toString()
            val mota = binding.mota.text.toString()
            val danhmuc = danhmucSP(id_danhmuc,ten,simage,mota)
            db.child(id_danhmuc).setValue(danhmuc).addOnCompleteListener {
                Toast.makeText(this, "Thêm dữ liệu thành công", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener { err ->
                Toast.makeText(this, "ERR ${err.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun insert_image(view: View) {
        val myfileintent = Intent(Intent.ACTION_GET_CONTENT)
        myfileintent.setType("image/*")
        ActivityResultLauncher.launch(myfileintent)
    }
    private val ActivityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ){
            result: ActivityResult ->
        if (result.resultCode== RESULT_OK){
            val uri = result.data!!.data
            try {
                val inputStrem = contentResolver.openInputStream(uri!!)
                val myBitmap = BitmapFactory.decodeStream(inputStrem)
                val stream = ByteArrayOutputStream()
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val bytes = stream.toByteArray()
                simage = Base64.encodeToString(bytes, Base64.DEFAULT)
                binding.imageView4.setImageBitmap(myBitmap)
                inputStrem!!.close()
            }catch (ex:java.lang.Exception){
                Toast.makeText(this, ex.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}