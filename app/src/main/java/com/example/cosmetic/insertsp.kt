package com.example.cosmetic

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.example.cosmetic.Model.Sanpham
import com.example.cosmetic.databinding.ActivityInsertspBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class insertsp : AppCompatActivity() {
    private lateinit var binding: ActivityInsertspBinding
    private lateinit var db: DatabaseReference
    var simage: String? = ""
    var simage2: String?=""
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertspBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = FirebaseDatabase.getInstance().getReference("sanpham")
        binding.btnsave.setOnClickListener {
            val Id_SanPham = db.push().key!!
            val ten = binding.ten.text.toString()
            val gia = binding.gia.text.toString()
            val soluong = binding.soluong.text.toString()
            val danhmuc = binding.idDanhmuc.text.toString()
            val nhacungcap = binding.nhacungcap.text.toString()
            val mota = binding.mota.text.toString()
            binding.soluongdaban.setText("0")
            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
            val formattedDateTime = currentDateTime.format(formatter)
            binding.ngay.setText(formattedDateTime)
            val sanpham = Sanpham(
                Id_SanPham,
                ten,
                gia.toFloat(),
                soluong.toInt(),
                simage ,
                simage2 ,
                danhmuc,
                nhacungcap,
                mota,
                0,
                formattedDateTime
            )
            db.child(Id_SanPham).setValue(sanpham).addOnCompleteListener {
                Toast.makeText(this, "Thêm dữ liệu thành công", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener { err ->
                Toast.makeText(this, "ERR ${err.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun insert_image(view: View) {
        val myfileintent = Intent(Intent.ACTION_GET_CONTENT)
        myfileintent.setType("image/*")
        ActivityResultLauncher.launch(myfileintent)
    }
    private val ActivityResultLauncher = registerForActivityResult<Intent,ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ){
            result:ActivityResult ->
        if (result.resultCode== RESULT_OK){
            val uri = result.data!!.data
            try {
                val inputStrem = contentResolver.openInputStream(uri!!)
                val myBitmap = BitmapFactory.decodeStream(inputStrem)
                val stream = ByteArrayOutputStream()
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val bytes = stream.toByteArray()
                simage = Base64.encodeToString(bytes,Base64.DEFAULT)
                binding.imageView4.setImageBitmap(myBitmap)
                inputStrem!!.close()
            }catch (ex:java.lang.Exception){
                Toast.makeText(this, ex.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun insert_image2(view: View) {
        val myfileintent = Intent(Intent.ACTION_GET_CONTENT)
        myfileintent.setType("image/*")
        ActivityResultLauncher2.launch(myfileintent)
    }
    private val ActivityResultLauncher2 = registerForActivityResult<Intent,ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ){
            result:ActivityResult ->
        if (result.resultCode== RESULT_OK){
            val uri = result.data!!.data
            try {
                val inputStrem = contentResolver.openInputStream(uri!!)
                val myBitmap = BitmapFactory.decodeStream(inputStrem)
                val stream = ByteArrayOutputStream()
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val bytes = stream.toByteArray()
                simage2 = Base64.encodeToString(bytes,Base64.DEFAULT)
                binding.imageView5.setImageBitmap(myBitmap)
                inputStrem!!.close()
            }catch (ex:java.lang.Exception){
                Toast.makeText(this, ex.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
