package com.example.notepad.db

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notepad.EditActivity
import com.example.notepad.R

class MyAdapter(listMain: ArrayList<ListItem>,contextM: Context):RecyclerView.Adapter<MyAdapter.MyHolder>() {

    var listArray = listMain
    var context = contextM

    class MyHolder(itemView: View, contextV: Context) : RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        var context = contextV
        fun setData(item: ListItem){
            tvTitle.text = item.title
            itemView.setOnClickListener {
                val intent = Intent(context , EditActivity::class.java).apply {
                 putExtra(MyIntentConstants.I_TITLE_KEY,item.title)
                 putExtra(MyIntentConstants.I_DESC_KEY,item.desc)
                 putExtra(MyIntentConstants.I_URI_KEY,item.uri)
                }
                context.startActivity(intent)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyHolder(inflater.inflate(R.layout.rc_item,parent,false), context)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.setData(listArray.get(position))
    }

    override fun getItemCount(): Int {
        return listArray.size
    }
    fun updateAdapter(listItems:List<ListItem>){
        listArray.clear()
        listArray.addAll(listItems)
        notifyDataSetChanged()
    }
}