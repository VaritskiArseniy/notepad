package com.example.notepad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notepad.databinding.ActivityMainBinding
import com.example.notepad.db.MyAdapter
import com.example.notepad.db.MyDbManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private var job:Job? = null
    val myDbManager = MyDbManager(this)
    val myAdapter = MyAdapter(ArrayList(),this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initSearchView()
    }
    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
        fillAdapter("")
    }
    fun onClickNew(view: View){
        val i = Intent(this, EditActivity::class.java)
        startActivity(i)

    }
    fun init(){
        binding.rcView.layoutManager = LinearLayoutManager(this)
        val swapHelper = getSwapMg()
        swapHelper.attachToRecyclerView(binding.rcView)
        binding.rcView.adapter = myAdapter
    }
    private fun initSearchView(){
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {
                fillAdapter(text!!)
                return true
            }

        })
    }
    private fun fillAdapter(text:String){
        job?.cancel()
        job = CoroutineScope(Dispatchers.Main).launch {
            val list = myDbManager.readDbData(text)
            myAdapter.updateAdapter(list)
            if (list.size > 0){
                binding.tvNoElement.visibility = View.GONE
            }else{
                binding.tvNoElement.visibility = View.VISIBLE
            }
        }

    }
    private fun getSwapMg():ItemTouchHelper{
        return ItemTouchHelper(object:ItemTouchHelper
        .SimpleCallback(0,ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                myAdapter.removeItem(viewHolder.adapterPosition , myDbManager)
            }
        })
    }


}