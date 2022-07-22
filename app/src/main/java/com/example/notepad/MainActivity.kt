package com.example.notepad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notepad.databinding.ActivityMainBinding
import com.example.notepad.db.MyAdapter
import com.example.notepad.db.MyDbManager

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    val myDbManager = MyDbManager(this)
    val myAdapter = MyAdapter(ArrayList(),this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
        fillAdapter()
    }
    fun onClickNew(view: View){
        val i = Intent(this, EditActivity::class.java)
        startActivity(i)

    }
    fun init(){
        binding.rcView.layoutManager = LinearLayoutManager(this)
        binding.rcView.adapter = myAdapter
    }
    fun fillAdapter(){
        myAdapter.updateAdapter(myDbManager.readDbData())
    }


}