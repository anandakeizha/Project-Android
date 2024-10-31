package com.dev.insertdata

import android.icu.text.CaseMap.Title
import androidx.activity.result.contract.ActivityResultContracts

data class inputData(val id: Int , val name: String, val nickname : String, val email : String, val addres : String , val born: String,  val phone : String , val image: ByteArray)