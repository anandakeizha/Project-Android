package com.dev.insertdata

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.insertdata.databinding.ActivityDataBinding


class data : AppCompatActivity() {

    private lateinit var binding: ActivityDataBinding
    private lateinit var db: DBmain
    private lateinit var dataAdapter: adapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DBmain(this)
        dataAdapter = adapter(db.getAllData(), this)


    }

    override fun onResume() {
        super.onResume()
        dataAdapter.refreshData(db.getAllData())
    }
}