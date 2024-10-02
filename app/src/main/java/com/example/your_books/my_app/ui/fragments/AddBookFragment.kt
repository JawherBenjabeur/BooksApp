package com.example.your_books.my_app.ui.fragments

import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.your_books.R
import com.example.your_books.my_app.models.*
import com.example.your_books.my_app.ui.activities.AcceuilActivity
import com.example.your_books.my_app.ui.adapters.CategorieAdapter
import com.example.your_books.my_app.viewmodels.CategorieVM
import com.example.your_books.my_app.viewmodels.LivreVM
import kotlinx.android.synthetic.main.fragment_addbooks.*
import java.io.ByteArrayOutputStream
import java.util.*

class AddBookFragment : Fragment(),View.OnClickListener {

    lateinit var categorieVM: CategorieVM
    lateinit var listecategorie : List<Categorie>
    lateinit var preferences : SharedPreferences
    lateinit var livreVM: LivreVM
    lateinit var livre: Livre

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_addbooks,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().run {
            livreVM = ViewModelProvider(this).get(LivreVM::class.java)
        }

        preferences = SharedPreferences(requireContext())
        categorieVM = ViewModelProvider(this).get(CategorieVM::class.java)
        livreVM=ViewModelProvider(this).get(LivreVM::class.java)

        btn_addlivre.setOnClickListener(this)
        addphoto_livre.setOnClickListener(this)

        date_edition()
        initobservalbes()

        categorieVM.categorie()

    }


    fun initobservalbes(){

        categorieVM.servicesLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                when (it) {
                    is WebServiceResultCategorie -> {
                        Log.e("listecategorie", "Listecategorie=$it")
                        listecategorie = it.categorie


                        val adapter = CategorieAdapter(requireContext(),listecategorie)
                        spinner0.adapter = adapter
                    }
                    is WebServiceResultFailureCategorie -> {
                        Log.e("message", "message=${it.message}")
                    }
                }
            }
        })

        livreVM.addLivreResponse.observe(viewLifecycleOwner,{
            Log.e("message","result=$it")

            if (it !=null){

                when(it){
                    is WebServiceResultLivre ->{
                        Log.e("champsLivre","champsLivre =$it")

                        requireActivity().onBackPressed()
                        livreVM.addLivreResponse.value =null

                    }

                    is WebServiceResultFailure -> {
                        Log.e("message", "message = Erreur")
                        livreVM.addLivreResponse.value =null
                    }
                }
            }
        })
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_addlivre ->{

                categorieVM.categorie()
                verifchamp()
            }
            R.id.addphoto_livre ->{
                (requireActivity() as AcceuilActivity).imagePickDialog(3)
            }
        }
    }

    fun verifchamp(){

        val id_categoriebook = listecategorie.get(spinner0.selectedItemPosition).id_categorie

        val titre = titre_ajouter.text.toString()
        val auteur = auteur_ajouter.text.toString()
        val editeur = editeur_ajouter.text.toString()
        val dateedition = date_edition_ajouter.text.toString()
        val langue = langue_ajouter.text.toString()
        val resume = resumer_ajouter.text.toString()
        val disponibilite = "oui"


        if (titre.isEmpty()){
            titre_ajouter.setError("merci de saisir le titre")
            return
        }
        if (auteur.isEmpty()){
            titre_ajouter.setError("merci de saisir l'auteur")
            return
        }

        if (langue.isEmpty()){
            langue_ajouter.setError("merci de saisir la langue du livre")
        }

        var img64 = ""
        if(photolivreadd.tag == 3){
            var bitmap = (photolivreadd.getDrawable() as BitmapDrawable).bitmap
            img64 = convertBitmapToBase64(bitmap)
            Log.e("BASE64","ENCODEimage64=$img64")
        }

        livreVM.addlivre(
                id_client = preferences.getloginShared().id_client,
                id_categorie = id_categoriebook,
                titre = titre,
                auteur = auteur,
                editeur=editeur,
                dateedition = dateedition,
                langue = langue,
                resumer = resume,
                disponibilite = disponibilite,
                photo_livre = img64
        )
    }



    fun date_edition(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        //button click pour afficher DatePickerDialog
        date_edition_ajouter.setOnClickListener {
            val dpd = DatePickerDialog(requireActivity(),
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        //set to textView
                        date_edition_ajouter.setText("" + year + "-" + (month+1) + "-" + dayOfMonth)
                    },
                    year,
                    month,
                    day
            )
            dpd.getDatePicker().getTouchables().get(0).performClick()

            val cal = Calendar.getInstance()
            dpd.datePicker.maxDate = cal.timeInMillis

            //afficher dialog
            dpd.show()
        }
    }

    fun convertBitmapToBase64(bitmap: Bitmap):String{
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,stream)
        val image = stream.toByteArray()
        return Base64.encodeToString(image, Base64.DEFAULT)
    }

}