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
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_edit_book.*
import java.io.ByteArrayOutputStream
import java.util.*

class EditBookFragment: Fragment(),View.OnClickListener {

    lateinit var categorieVM: CategorieVM
    lateinit var listecategorie : List<Categorie>
    lateinit var livre : Livre
    lateinit var preferences : SharedPreferences
    lateinit var livreVM: LivreVM

    var first = true


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_edit_book,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = SharedPreferences(requireContext())
        categorieVM = ViewModelProvider(this).get(CategorieVM::class.java)

        requireActivity().run {
            livreVM = ViewModelProvider(this).get(LivreVM::class.java)
        }

        btn_saveedit.setOnClickListener(this)
        btn_add_img_up.setOnClickListener(this)
        date_edition()
        initObservables()

        categorieVM.categorie()

        if(first)
        {
            selectbook()
            first=false
        }


    }

    fun initObservables(){
        livreVM.servicesLiveData.observe(viewLifecycleOwner,{

            when(it){
                is WebServiceResultLivre ->{

                   livreVM.selectedBook = it.livre

                    requireActivity().onBackPressed()
                    Log.e("boooooook","book=${livreVM.selectedBook}")
                }
                is WebServiceResultFailure ->{
                    Log.e("messageeeeeeeeee","message=${it.message}")
                }
            }
        })

        categorieVM.servicesLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                when (it) {
                    is WebServiceResultCategorie -> {
                        Log.e("listecategorie", "Listecategorie=$it")
                        listecategorie = it.categorie
                        var id_categorie_selected= livreVM.selectedBook!!.id_categorie
                        Log.e("listecategorie", "Listecategorie=$id_categorie_selected")


                        val adapter = CategorieAdapter(requireContext(),listecategorie)
                        spinner1.adapter = adapter
                        spinner1.setSelection(id_categorie_selected.toInt()-1)
                    }
                    is WebServiceResultFailureCategorie -> {
                        Log.e("message", "message=${it.message}")
                    }
                }
            }
        })

    }



    fun selectbook(){

        val livreclick = livreVM.selectedBook!!
        //val id_categoriebook = listecategorie.get(spinner1.selectedItemPosition).id_categorie

        edit_titre.setText(livreclick.titre)
        edit_auteur.setText(livreclick.auteur)
        edit_editeur.setText(livreclick.editeur)

        edit_dateedition.setText(livreclick.dateedition)
        edit_langue.setText(livreclick.langue)
        edit_resumer.setText(livreclick.resumer)

        if(livreclick.disponibilite.equals("oui")){
            radioGroupe.check(R.id.dispo)
        }
        else{
            radioGroupe.check(R.id.nondispo)
        }

        val url = getString(R.string.ipAdresse)+livreclick.photo_livre
        Log.e("adresssss","adresss=$url")

        Picasso.with(context)
                .load(url)
                .into(edit_photo_livre, object: com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        //set animations here
                    }
                    override fun onError() {
                    }
                })

    }

    fun verifchamp(){
        val id_categoriebook = listecategorie.get(spinner1.selectedItemPosition).id_categorie
       Log.e("catcatcat","catacta=$id_categoriebook")
        var bitmap = (edit_photo_livre.getDrawable() as BitmapDrawable).bitmap

        val img64 = convertBitmapToBase64(bitmap)
        Log.e("BASE64","ENCODEimage64=$img64")

        var dipo = ""
        if (dispo.isChecked) {
            dipo = "oui"
        }else{
            dipo="non"
        }


        livreVM.updatebook(
                id_livre =livreVM.selectedBook!!.id_livre,
                id_client = livreVM.selectedBook!!.id_client,
                id_categorie = id_categoriebook,
                titre = edit_titre.text.toString(),
                auteur = edit_auteur.text.toString(),
                editeur = edit_editeur.text.toString(),
                dateedition = edit_dateedition.text.toString(),
                langue = edit_langue.text.toString(),
                resumer = edit_resumer.text.toString(),
                photo_livre = img64,
                disponibilite = dipo
        )
        Log.e("aaaaaa","aaaaaa=$dipo")


    }




    override fun onClick(v: View) {

        when(v.id){
            R.id.btn_saveedit ->{

                verifchamp()

            }

            R.id.btn_add_img_up -> {
                (requireActivity() as AcceuilActivity).imagePickDialog(2 )
            }
        }

    }



    fun convertBitmapToBase64(bitmap: Bitmap):String{
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,stream)
        val image = stream.toByteArray()
        return Base64.encodeToString(image, Base64.DEFAULT)
    }

    fun date_edition(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        //button click pour afficher DatePickerDialog
        btn_datepick.setOnClickListener {
            val dpd = DatePickerDialog(
                    requireActivity(),
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        //set to textView
                        edit_dateedition.setText("" + year + "-" + (month + 1) + "-" + dayOfMonth)
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
}