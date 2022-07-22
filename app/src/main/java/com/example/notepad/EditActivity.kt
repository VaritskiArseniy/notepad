package com.example.notepad

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View

import androidx.activity.result.contract.ActivityResultContracts
import com.example.notepad.databinding.EditActivityBinding
import com.example.notepad.db.MyDbManager
import com.example.notepad.db.MyDbNameClass
import com.example.notepad.db.MyIntentConstants

class EditActivity : AppCompatActivity() {

    val myDbManager = MyDbManager(this)
    val imageRequestCode = 10
    var tempImageUri = "empty"


    private lateinit var binding: EditActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getMyIntents()
    }
    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == imageRequestCode){
            binding.imMainImage.setImageURI(data?.data)
            tempImageUri = data?.data.toString()

        }
    }



    fun onClickAddImage(view: View) {
        binding.mainImageLayout.visibility = View.VISIBLE
        binding.fbAddImage.visibility = View.GONE
    }

    fun onClickDeleteImage(view: View) {
        binding.mainImageLayout.visibility = View.GONE
        binding.fbAddImage.visibility = View.VISIBLE
    }
    fun onClickChooseImage(view: View){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type = "image/*"
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivityForResult(intent,imageRequestCode)
    }
    fun onClickSave(view: View){
        val myTitle = binding.edTitle.text.toString()
        val myDesc = binding.edDesc.text.toString()

        if (myTitle != "" && myDesc != ""){
            myDbManager.insertToDb(myTitle , myDesc , tempImageUri)
        }
    }
    fun getMyIntents(){
        val i = intent
        if (i != null){
            if (i.getStringExtra(MyIntentConstants.I_TITLE_KEY) != null){
                    binding.fbAddImage.visibility = View.GONE
                    binding.edTitle.setText(i.getStringExtra(MyIntentConstants.I_TITLE_KEY))
                    binding.edDesc.setText(i.getStringExtra(MyIntentConstants.I_DESC_KEY))
                    if(i.getStringExtra(MyIntentConstants.I_URI_KEY) != "empty"){
                        binding.mainImageLayout.visibility = View.VISIBLE
                        binding.imMainImage.setImageURI(Uri.parse(i.getStringExtra(MyIntentConstants.I_URI_KEY)))
                        binding.imButtonEditImage.visibility = View.GONE
                        binding.imButtonDeleteImage.visibility = View.GONE


                }


            }
        }

    }
}



