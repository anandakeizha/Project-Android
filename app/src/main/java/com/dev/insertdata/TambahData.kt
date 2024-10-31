package com.dev.insertdata

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.dev.insertdata.databinding.ActivityDataBinding
import com.dev.insertdata.databinding.ActivityTambahDataBinding
import com.squareup.picasso.Picasso
import java.util.*


class TambahData : AppCompatActivity() {


    private lateinit var binding: ActivityTambahDataBinding
    private lateinit var db: DBmain

    val Camera_Request = 100
    val Storage_Permisson = 101

    val cameraPermission: Array<String> = arrayOf(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE)
    val storagePermission : Array<String> = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    val cropImageLauncher = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            val uri: Uri? = result.uriContent
            Picasso.get().load(uri).into(binding.foto)
        } else {
            val error = result.error
            error?.printStackTrace()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahDataBinding.inflate(layoutInflater)

        setContentView(binding.root)
        

        db = DBmain(this)

        binding.foto.setOnClickListener {
            var avatar = 0
            if (avatar == 0) {
                if (!checkCameraPermission()) {
                    requstCameraPersmission()
                } else {
                    pickFromGallery()
                }
            } else if (avatar == 1) {
                if (!checkStoragePermission()) {
                    requestStoragePermission()
                } else {
                    pickFromGallery()
                }
            }
        }

        binding.back.setOnClickListener(){
            val intent = Intent(this , MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.submit.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirmation")
            builder.setMessage("Are you sure?")

            builder.setPositiveButton("Yes") { dialog, _ ->
                val nama = binding.inputNama.text.toString()
                val namapanggilan = binding.inputNick.text.toString()
                val email = binding.inputEmail.text.toString()
                val addres = binding.inputAlamat.text.toString()
                val born = binding.inputBorn.text.toString()
                val phone = binding.inputTelp.text.toString()
                val image = db.ImageViewToByte(binding.foto)
                val data = inputData(0,nama , namapanggilan , email, addres, born, phone, image)
                db.insertData(data)
                finish()
                Toast.makeText(this,"Data Saved" , Toast.LENGTH_SHORT).show()
            }
            builder.show()

            builder.setNegativeButton("No") { dialog, _ ->
                // Tetap di laman tambahdata, hanya tutup dialog
                dialog.dismiss()
            }

        }

        binding.inputBorn.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = "$selectedDay-${selectedMonth + 1}-$selectedYear"
                    binding.inputBorn.setText(selectedDate) // Set tanggal ke inputBorn
                },
                year, month, day
            )

            datePickerDialog.show()
        }

    }

    private fun requestStoragePermission() {
        requestPermissions(storagePermission, Storage_Permisson)
    }

    private fun checkStoragePermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED)
        return result
    }

    private fun pickFromGallery() {
        cropImageLauncher.launch(CropImageContractOptions(null, CropImageOptions()))
    }

    private fun requstCameraPersmission() {
        requestPermissions(cameraPermission, Camera_Request)
    }

    private fun checkCameraPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED)
        val result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED)
        return result && result2
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Camera_Request -> {
                if (grantResults.size > 0) {
                    val cameraAccept = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (cameraAccept) {
                        pickFromGallery()
                    } else {
                        Toast.makeText(this, "Enable Camera and Storage Permissions", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            Storage_Permisson -> {
                if (grantResults.size > 0) {
                    val storegaAccept = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (storegaAccept) {
                        pickFromGallery()
                    } else {
                        Toast.makeText(this, "Enable Storage Permission", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}