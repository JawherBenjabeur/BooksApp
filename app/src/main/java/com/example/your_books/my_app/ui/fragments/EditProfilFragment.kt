package com.example.your_books.my_app.ui.fragments

import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.your_books.R
import com.example.your_books.my_app.models.SharedPreferences
import com.example.your_books.my_app.models.WebServiceResultFailure
import com.example.your_books.my_app.models.WebServiceResultLoginSuccess
import com.example.your_books.my_app.ui.activities.AcceuilActivity
import com.example.your_books.my_app.viewmodels.UpdateVM
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_inscription.*
import kotlinx.android.synthetic.main.fragment_edit_profil.*
import java.io.ByteArrayOutputStream
import java.util.*


class EditProfilFragment:Fragment(),View.OnClickListener  {

    lateinit var preferences : SharedPreferences
    lateinit var updateVM: UpdateVM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_profil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateVM = ViewModelProvider(this).get(UpdateVM::class.java)
        preferences = SharedPreferences(requireContext())

        btn_save.setOnClickListener(this)
        btn_date.setOnClickListener(this)
        btn_add_img.setOnClickListener(this)

        setEdit()
        initObservables()
        verif_champ_update()

    }

    fun initObservables(){
        updateVM.servicesLiveData.observe(viewLifecycleOwner, {
            Log.e("message", "result=$it")

            if (it != null) {

                when (it) {
                    is WebServiceResultLoginSuccess -> {

                        preferences.setloginShared(
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
                        Log.e("user","user=${it.user.photo_client}")

                        requireActivity().onBackPressed()
                        updateVM.servicesLiveData.value = null
                    }

                    is WebServiceResultFailure -> {

                        Log.e("message", "message${it.message}")

                        updateVM.servicesLiveData.value = null
                    }
                }
            }

        })
    }



    fun verif_champ_update(){
        val prenom_update = edit_prenom.text.toString()
        val nom_update = edit_nom.text.toString()
        val ville_update = edit_city.text.toString()
        val date_de_naissance_update = edit_date.text.toString()
        val numero_de_telephone_update = edit_number.text.toString()
        val email_update = edit_mail.text.toString()

        if(prenom_update.isEmpty()){

            edit_prenom.setError(getString(R.string.msg_saisirprenom))
            return
        }

        if(nom_update.isEmpty()){

            edit_nom.setError(getString(R.string.msg_saisirnom))
            return
        }

        if(numero_de_telephone_update.isEmpty()){

            edit_number.setError(getString(R.string.msg_saisirnumero))
            return
        }

        if(numero_de_telephone_update.length!=8){

            edit_number.setError(getString(R.string.msg_verifier_numero))
            return
        }

        if(email_update.isEmpty()){

            edit_mail.setError(getString(R.string.msg_saisiremail))
            return
        }

        if(!isValidEmail(email_update)){
            edit_mail.setError(getString(R.string.msg_verifieremail))
            return
        }


        var img64 = ""
        if(photo_client.tag == 1){
            var bitmap = (photo_client.getDrawable() as BitmapDrawable).bitmap
            img64 = convertBitmapToBase64(bitmap)
            Log.e("BASE64","ENCODEimage64=$img64")
        }


        updateVM.userUpdate(
            id_client = preferences.getloginShared().id_client,
            prenom = prenom_update,
            nom = nom_update,
            ville = ville_update,
            date = date_de_naissance_update,
            telephone = numero_de_telephone_update,
            email = email_update,
            photo_client = img64
        )
    }
    //******************************Vérification d'adresse e-mail******************************
    fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_save -> {
                verif_champ_update()
            }
            R.id.btn_date -> {
                date_naissance()
            }
            R.id.btn_add_img -> {

                (requireActivity() as AcceuilActivity).imagePickDialog(1)

            }
        }
    }

    //Récupérer des coordonnées
    fun setEdit(){

        val user = preferences.getloginShared()

        edit_prenom.setText(user.prenom)
        edit_nom.setText(user.nom)
        edit_city.setText(user.ville)
        edit_date.setText(user.datedenaissance)
        edit_number.setText(user.numerotelephone)
        edit_mail.setText(user.email)

        val url = getString(R.string.ipAdresse)+user.photo_client
        Log.e("adresssss","adresss=$url")

        Picasso.with(context)
                .load(url)
                .into(photo_client, object: com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        //set animations here
                    }
                    override fun onError() {
                    }
                })

    }
    //data picker
    fun date_naissance(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        //button click pour afficher DatePickerDialog
        btn_date.setOnClickListener {
            val dpd = DatePickerDialog(
                requireActivity(),
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    //set to textView
                    date_naiss_isncri.setText("" + year + "-" + (month + 1) + "-" + dayOfMonth)
                },
                year,
                month,
                day
            )
            dpd.getDatePicker().getTouchables().get(0).performClick()

            val cal = Calendar.getInstance()
            cal.add(Calendar.YEAR, -10);
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