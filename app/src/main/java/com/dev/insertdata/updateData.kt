package com.dev.insertdata

import android.Manifest
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
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
import com.dev.insertdata.databinding.ActivityUpdateDataBinding
import com.squareup.picasso.Picasso

class updateData : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateDataBinding
    private lateinit var db: DBmain
    private var DataId: Int = -1

    val Camera_Request = 100
    val Storage_Permisson = 101

    val cameraPermission: Array<String> = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    val storagePermission : Array<String> = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    val cropImageLauncher = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            val uri: Uri? = result.uriContent
            Picasso.get().load(uri).into(binding.updateImage)
        } else {
            val error = result.error
            error?.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityUpdateDataBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        db = DBmain(this)

        binding.updateImage.setOnClickListener {
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

        DataId = intent.getIntExtra("data_id", -1)
        if (DataId == -1){
            finish()
            return
        }

        val data = db.getDatabyId(DataId)
        val bitmap = BitmapFactory.decodeByteArray(data.image, 0, data.image.size)
        binding.updateNama.setText(data.name)
        binding.updateNick.setText(data.nickname)
        binding.updateEmail.setText(data.email)
        binding.updateAlamat.setText(data.addres)
        binding.updateBorn.setText(data.born)
        binding.updateTelp.setText(data.phone)
        binding.updateImage.setImageBitmap(bitmap)


        binding.update.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirmation")
            builder.setMessage("Are you sure?")

            builder.setPositiveButton("Yes") { dialog, _ ->
                val newName = binding.updateNama.text.toString()
                val newNick = binding.nick.text.toString()
                val newEmail = binding.updateEmail.text.toString()
                val newAddres = binding.updateAlamat.text.toString()
                val newBorn = binding.updateBorn.text.toString()
                val newTelp = binding.updateTelp.text.toString()
                val newImage = db.ImageViewToByte(binding.updateImage)
                val updatedNote = inputData(DataId, newName, newNick, newEmail, newAddres, newBorn, newTelp, newImage)
                db.updateData(updatedNote)
                finish()
                Toast.makeText(this,"changes saved", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("No") { dialog, _ ->
                // Tetap di laman tambahdata, hanya tutup dialog
                dialog.dismiss()
            }
            builder.show()
        }

        binding.updateBorn.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = "$selectedDay-${selectedMonth + 1}-$selectedYear"
                    binding.updateBorn.setText(selectedDate) // Set tanggal ke inputBorn
                },
                year, month, day
            )

            datePickerDialog.show()
        }
    }

    private fun pickFromGallery() {
        cropImageLauncher.launch(CropImageContractOptions(null, CropImageOptions()))
    }

    private fun requstCameraPersmission() {
        requestPermissions(storagePermission, Storage_Permisson)
    }

    private fun requestStoragePermission() {
        requestPermissions(storagePermission, Storage_Permisson)
    }

    private fun checkCameraPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED)
        val result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED)
        return result && result2
    }

    private fun checkStoragePermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED)
        return result
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