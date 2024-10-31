package com.dev.insertdata

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.insertdata.databinding.ActivityDataBinding
import com.dev.insertdata.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: DBmain
    private lateinit var dataAdapter: adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getData()

        val insert: Button = binding.tambah
        insert.setOnClickListener(){
            val intent = Intent(this,TambahData::class.java)
            startActivity(intent)

        }

        val bukuu: Button = binding.tentang
        bukuu.setOnClickListener(){
            val intent = Intent(this , buku::class.java)
            startActivity(intent)
        }

        binding.recyclered.layoutManager= LinearLayoutManager (this)
        binding.recyclered.adapter = dataAdapter
    }

    private fun getData(){
        db = DBmain(this)
        dataAdapter = adapter(db.getAllData(), this)
    }

    override fun onResume() {
        super.onResume()
        dataAdapter.refreshData(db.getAllData())
    }
}