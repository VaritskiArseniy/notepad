package com.example.notepad

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.view.View

import com.example.notepad.databinding.EditActivityBinding
import com.example.notepad.db.MyDbManager
import com.example.notepad.db.MyIntentConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EditActivity : AppCompatActivity() {

    var isEditState = false
    var id = 0
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
            contentResolver.takePersistableUriPermission(data?.data!! , Intent.FLAG_GRANT_READ_URI_PERMISSION)

        }
    }



    fun onClickAddImage(view: View) {
        binding.mainImageLayout.visibility = View.VISIBLE
        binding.fbAddImage.visibility = View.GONE
    }

    fun onClickDeleteImage(view: View) {
        binding.mainImageLayout.visibility = View.GONE
        binding.fbAddImage.visibility = View.VISIBLE
        tempImageUri = "empty"
    }
    fun onClickChooseImage(view: View){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type = "image/*"
        startActivityForResult(intent,imageRequestCode)
    }
    fun onClickSave(view: View){
        val myTitle = binding.edTitle.text.toString()
        val myDesc = binding.edDesc.text.toString()

        if (myTitle != "" && myDesc != ""){

        CoroutineScope(Dispatchers.Main).launch {
                if (isEditState){
                    myDbManager.updateItem(myTitle, myDesc, tempImageUri, id, getCurrentTime())
                }else{
                    myDbManager.insertToDb(myTitle , myDesc , tempImageUri , getCurrentTime())
                }
                finish()
        }
        }
    }
    fun getMyIntents(){
        val i = intent
        binding.fbEdit.visibility = View.GONE
        if (i != null){
            if (i.getStringExtra(MyIntentConstants.I_TITLE_KEY) != null){
                    binding.fbAddImage.visibility = View.GONE
                isEditState = true
                binding.edTitle.isEnabled = false
                binding.edDesc.isEnabled = false
                binding.fbEdit.visibility = View.VISIBLE
                    binding.edTitle.setText(i.getStringExtra(MyIntentConstants.I_TITLE_KEY))
                    binding.edDesc.setText(i.getStringExtra(MyIntentConstants.I_DESC_KEY))
                    id = i.getIntExtra(MyIntentConstants.I_ID_KEY, 0)
                    if(i.getStringExtra(MyIntentConstants.I_URI_KEY) != "empty"){
                        binding.mainImageLayout.visibility = View.VISIBLE
                        tempImageUri = i.getStringExtra(MyIntentConstants.I_URI_KEY)!!
                        binding.imMainImage.setImageURI(Uri.parse(tempImageUri))
                        binding.imButtonEditImage.visibility = View.GONE
                        binding.imButtonDeleteImage.visibility = View.GONE


                }


            }
        }

    }

    fun onEditEnable(view: View) {
        binding.edTitle.isEnabled = true
        binding.edDesc.isEnabled = true
        binding.fbEdit.visibility = View.GONE
        binding.fbAddImage.visibility = View.VISIBLE
        if (tempImageUri == "empty")return
        binding.imButtonEditImage.visibility = View.VISIBLE
        binding.imButtonDeleteImage.visibility = View.VISIBLE
    }

    //Get time and formating
    private fun getCurrentTime(): String {
        val time = Calendar.getInstance().time
        val formartter = SimpleDateFormat("dd-MM-yy kk:mm", Locale.getDefault())
        return formartter.format(time)
    }
}



