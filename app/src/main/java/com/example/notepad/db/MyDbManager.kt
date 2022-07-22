package com.example.notepad.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class MyDbManager(val context: Context) {
    val myDbHelper = MyDbHelper(context)
    var db:SQLiteDatabase? = null

    fun openDb(){
        db = myDbHelper.writableDatabase
    }
    fun insertToDb(title:String,content:String, uri : String){
        val values =ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME_TITLE , title)
            put(MyDbNameClass.COLUMN_NAME_CONTENT , content)
            put(MyDbNameClass.COLUMN_NAME_IMAGE_URI , uri)
        }
        db?.insert(MyDbNameClass.TABLE_NAME, null , values)
    }
    fun readDbData() : ArrayList<ListItem>{
        val dataList = ArrayList<ListItem>()
        val cursor = db?.query(MyDbNameClass.TABLE_NAME,
            null,null,null,null,null,null)

            while (cursor?.moveToNext()!!){
                val dataTitle = cursor.getString(cursor.getColumnIndexOrThrow(MyDbNameClass.COLUMN_NAME_TITLE))
                val dataContent = cursor.getString(cursor.getColumnIndexOrThrow(MyDbNameClass.COLUMN_NAME_CONTENT))
                val dataUri = cursor.getString(cursor.getColumnIndexOrThrow(MyDbNameClass.COLUMN_NAME_IMAGE_URI))
                var item = ListItem()
                item.title = dataTitle
                item.desc = dataContent
                item.uri = dataUri

                dataList.add(item)
            }
        cursor.close()
        return dataList
    }
    fun closeDb(){
        myDbHelper.close()
    }
}