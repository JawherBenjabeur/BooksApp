package com.example.your_books.my_app.ui.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.your_books.R
import com.example.your_books.R.layout.activity_inscription
import com.example.your_books.my_app.models.SharedPreferences
import com.example.your_books.my_app.models.WebServiceResultFailure
import com.example.your_books.my_app.models.WebServiceResultLoginSuccess
import com.example.your_books.my_app.viewmodels.SignupVM
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_inscription.*
import java.io.ByteArrayOutputStream
import java.util.*


class RegisterActivity : AppCompatActivity(),View.OnClickListener{

    lateinit var signupVM: SignupVM
    lateinit var sharedpreference : SharedPreferences

    val STORAGE_REQUEST_CODE  =100
    val CAMERA_REQUEST_CODE = 101

    val IMAGE_PICK_CAMERA_CODE = 102
    val IMAGE_PICK_GALLERY_CODE =103


    lateinit var cameraPermission: Array<String>
    lateinit var storagePermission: Array<String>

    var imageUri: Uri? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_inscription)

        sharedpreference = SharedPreferences(this)
        signupVM = ViewModelProvider(this).get(SignupVM::class.java)


        cameraPermission= arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.CAMERA)
        storagePermission= arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)


        date_naissance()
        initObservables()
        btn_inscrit.setOnClickListener(this)
        btnphotoclientadd.setOnClickListener(this)

    }

    private fun initObservables() {

        signupVM.servicesLiveData.observe(this, {
            Log.e("message", "result=$it")

            when (it) {

                is WebServiceResultLoginSuccess -> {

                    sharedpreference.setloginShared(
                        it.user.id_client,
                        it.user.nom,
                        it.user.prenom,
                        it.user.ville,
                        it.user.datedenaissance,
                        it.user.numerotelephone,
                        it.user.email,
                            "",
                        //it.user.motdepass,
                        it.user.photo_client
                    )

                    Toast.makeText(this, "Inscription valider", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, AcceuilActivity::class.java)
                    this.startActivity(intent)
                    finish()

                }

                is WebServiceResultFailure -> {
                    Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }


    fun verif_champ(){
        val prenom = prenom_inscri.text.toString()
        val nom = nom_inscri.text.toString()
        val ville =  ville_iscri.text.toString()
        val date_de_naissance= date_naiss_isncri.text.toString()
        val numero_de_telephone = numero_inscri.text.toString()
        val email = email_inscri.text.toString()
        val password = password_inscri.text.toString()
        val photo_client = ""
        val checkbox = checkBox


            photoclientadd.tag == 0
            var bitmap = (photoclientadd.getDrawable() as BitmapDrawable).bitmap
            var img64 = convertBitmapToBase64(bitmap)
            Log.e("BASE64","ENCODEimage64=$img64")

        Log.e("ddddd","eeeeeeeeee")

        if(prenom.isEmpty()){

            prenom_inscri.setError(getString(R.string.msg_saisirprenom))
            return
        }

        if(nom.isEmpty()){

            nom_inscri.setError(getString(R.string.msg_saisirnom))
            return
        }


        if(numero_de_telephone.isEmpty()){

            numero_inscri.setError(getString(R.string.msg_saisirnumero))
            return
        }

        if(numero_de_telephone.length!=8){

            numero_inscri.setError(getString(R.string.msg_verifier_numero))
            return
        }

        if(email.isEmpty()){

           email_inscri.setError(getString(R.string.msg_saisiremail))
            return
        }

        if(!isValidEmail(email)){
            email_inscri.setError(getString(R.string.msg_verifieremail))
            return
        }


        if(password.isEmpty()){

            password_inscri.setError(getString(R.string.msg_saisirpassword))
            return
        }

        if (!checkbox.isChecked){

            checkbox.setError(getString(R.string.msg_checking))
            return
        }

        signupVM.userSingup(
            prenom = prenom,
            nom = nom,
            ville = ville,
            date = date_de_naissance,
            telephone = numero_de_telephone,
            email = email,
            password = password,
            photo_client = img64
        )
    }


    fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onClick(v: View) {

        when(v.id){

            R.id.btn_inscrit -> {
                verif_champ()
            }

            R.id.btnphotoclientadd ->{
                imagePickDialog()
            }
        }
    }
    fun imagePickDialog(){
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
                == PackageManager.PERMISSION_GRANTED)

    }


    //************************Request for sttorage permission ************************
    fun requestStoragePermission(){

        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE )

    }


    //************************check camera and storage both permission************************
    fun checkCameraPermission():Boolean{
        val result = (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
        val result1 = (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)

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
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){

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
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){

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
                photoclientadd.setImageURI(imageUri)


            }
            else if (requestCode ==IMAGE_PICK_CAMERA_CODE){

                photoclientadd.setImageURI(imageUri)

            }
            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK){
                    val resulUri = result.uri
                    imageUri = resulUri
                    photoclientadd.setImageURI(resulUri)
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




    fun date_naissance(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        //button click pour afficher DatePickerDialog
        date_naiss_isncri.setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    //set to textView
                    date_naiss_isncri.setText("" + year + "-" + (month+1) + "-" + dayOfMonth)
                },
                year,
                month,
                day
            )
            dpd.getDatePicker().getTouchables().get(0).performClick()

            val cal = Calendar.getInstance()
            cal.add(Calendar.YEAR,-10);
            dpd.datePicker.maxDate = cal.timeInMillis

            //afficher dialog
            dpd.show()
        }
    }
    fun convertBitmapToBase64(bitmap: Bitmap):String{
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,stream)
        val image = stream.toByteArray()
        return Base64.encodeToString(image,Base64.DEFAULT)
    }

}