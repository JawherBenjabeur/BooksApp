package com.example.your_books.my_app.ui.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.your_books.R
import com.example.your_books.my_app.ui.fragments.EditPasswordFragment
import com.example.your_books.my_app.ui.fragments.EditProfilFragment
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_navig.*
import kotlinx.android.synthetic.main.fragment_addbooks.*
import kotlinx.android.synthetic.main.fragment_edit_book.*
import kotlinx.android.synthetic.main.fragment_edit_profil.*


class AcceuilActivity : AppCompatActivity() {

    val STORAGE_REQUEST_CODE  =100
    val CAMERA_REQUEST_CODE = 101

    val IMAGE_PICK_CAMERA_CODE = 102
    val IMAGE_PICK_GALLERY_CODE =103


    lateinit var cameraPermission: Array<String>
    lateinit var storagePermission: Array<String>

    var imageUri: Uri? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navig)

        cameraPermission= arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.CAMERA)
        storagePermission= arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

   //*******************Naviguer entre les fragments*******************
        val navController=findNavController(R.id.hostFragment)
        bottom_navigation.setupWithNavController(navController)

    }

    fun editerprofil(){

        val editerprofil = EditProfilFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.hostFragment, editerprofil)
            commit()
        }

    }

    fun editpassword(){
        val editerpassword = EditPasswordFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.hostFragment, editerpassword)
            commit()
        }
    }

    // flag = 1 => pickImage Profil
    // flag = 2 => edit image book
    // flag = 3 => add image book
    //*******************dialog *******************
    var flagPickImage = 0

    fun imagePickDialog(flag : Int){

        flagPickImage = flag

        val options = arrayOf("Camera","Gallery")
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Pick Image From ")
        builder.setItems(options){dialog, which ->
            if (which==0){
                if (!checkCameraPermission()){
                    requestCameraPermission()
                }
                else{
                    pickFromCamera()
                }


            }
            else if (which ==1){
                if (!checkStoragePermission()){
                    requestStoragePermission()
                }
                else{
                    pickFromStorage()
                }

            }
        }
        builder.create().show()
    }

    //************************Check storage permission************************

    fun checkStoragePermission():Boolean{
        Log.e("checkStorage","CheckStorage*****************")
        return(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ==PackageManager.PERMISSION_GRANTED)

    }


    //************************Request for sttorage permission ************************
    fun requestStoragePermission(){

        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE )
    }


    //************************check camera and storage both permission************************
    fun checkCameraPermission():Boolean{
        val result = (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ==PackageManager.PERMISSION_GRANTED)
        val result1 = (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ==PackageManager.PERMISSION_GRANTED)

        return result && result1
    }


    //************************request for camera permission************************
    fun requestCameraPermission(){

        ActivityCompat.requestPermissions(this,cameraPermission,CAMERA_REQUEST_CODE)

    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){

            CAMERA_REQUEST_CODE-> {
                if (grantResults.isNotEmpty() && grantResults[0] ==PackageManager.PERMISSION_GRANTED){

                    val cameraAccepted = grantResults[0]== PackageManager.PERMISSION_GRANTED
                    val storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED

                    if (cameraAccepted && storageAccepted){
                        // if both permission accepted then pick image from camera
                        pickFromCamera()
                    }
                    else{
                        Toast.makeText(this,"Camera Permission Required",Toast.LENGTH_LONG).show()
                    }
                }

            }
            STORAGE_REQUEST_CODE-> {
                if (grantResults.isNotEmpty() && grantResults[0] ==PackageManager.PERMISSION_GRANTED){

                    val storageAccepted = grantResults[0]== PackageManager.PERMISSION_GRANTED

                    if (storageAccepted){
                        // if storage permission accepted then pick from storage
                        pickFromStorage()
                    }
                    else{
                        Toast.makeText(this,"Storage Permission Required",Toast.LENGTH_LONG).show()
                    }
                }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode== Activity.RESULT_OK){

            if (requestCode==IMAGE_PICK_GALLERY_CODE){
                imageUri = data!!.data

                when(flagPickImage){
                    1 -> {
                        photo_client.setImageURI(null)
                        photo_client.setImageURI(imageUri)
                        photo_client.setTag(1)
                    }
                    2 -> {
                        Log.e("photolivre","photolivre=$imageUri")
                        edit_photo_livre.setImageURI(null)
                        edit_photo_livre.setImageURI(imageUri)
                        edit_photo_livre.setTag(2)
                    }
                    3 -> {

                        photolivreadd.setImageURI(null)
                        photolivreadd.setImageURI(imageUri)
                        photolivreadd.setTag(3)
                    }
                }
                flagPickImage = 0

            }
            else if (requestCode ==IMAGE_PICK_CAMERA_CODE){

                when(flagPickImage){
                    1 -> {
                        photo_client.setImageURI(null)
                        photo_client.setImageURI(imageUri)
                        photo_client.setTag(1)
                    }
                    2 -> {
                        Log.e("photolivre","photolivre=$imageUri")
                        edit_photo_livre.setImageURI(null)
                        edit_photo_livre.setImageURI(imageUri)
                        edit_photo_livre.setTag(2)
                    }
                    3 -> {

                        photolivreadd.setImageURI(null)
                        photolivreadd.setImageURI(imageUri)
                        photolivreadd.setTag(3)
                    }
                }
                flagPickImage = 0

            }
            else if (requestCode ==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK){
                    imageUri = data!!.data

                    when(flagPickImage){
                        1 -> {
                            photo_client.setImageURI(null)
                            photo_client.setImageURI(imageUri)
                            photo_client.setTag(1)
                        }
                        2 -> {
                            Log.e("photolivre","photolivre=$imageUri")
                            edit_photo_livre.setImageURI(null)
                            edit_photo_livre.setImageURI(imageUri)
                            edit_photo_livre.setTag(2)
                        }
                        3 -> {
                            photolivreadd.setImageURI(null)
                            photolivreadd.setImageURI(imageUri)
                            photolivreadd.setTag(3)
                        }
                    }
                    flagPickImage = 0
                }
                else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    val error = result.error
                    Toast.makeText(this,""+error,Toast.LENGTH_LONG).show()
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun pickFromStorage() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent,IMAGE_PICK_GALLERY_CODE)
    }

    private fun pickFromCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE,"Image title")
        values.put(MediaStore.Images.Media.DESCRIPTION,"Image description")

        imageUri= contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE)
    }



}