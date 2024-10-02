package com.example.your_books.my_app.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.your_books.R
import com.example.your_books.my_app.models.*
import com.example.your_books.my_app.ui.adapters.CommentAdapter
import com.example.your_books.my_app.viewmodels.LivreVM
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_consulte_my_book.*

class ConsultMyBookFragment:Fragment(),View.OnClickListener {

    lateinit var livreVm: LivreVM
    lateinit var preferences : SharedPreferences
    lateinit var listcomment :List<CommentSelect>

    //
    var found = false
    var foundLike = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_consulte_my_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = SharedPreferences(requireContext())

        requireActivity().run {
            livreVm = ViewModelProvider(this).get(LivreVM::class.java)
        }

        livreVm.getlivre(livreVm.selectedBook!!.id_livre)
        livreVm.selectcomment(livreVm.selectedBook!!.id_livre)

        livreVm.getlikes(livreVm.selectedBook!!.id_livre)

        Log.e("messs","messs=${livreVm.selectedBook!!.id_livre}")

        btnconsultmybook.setOnClickListener(this)
        btndelete.setOnClickListener(this)
        btncomment.setOnClickListener(this)
        like.setOnClickListener(this)
        dislike.setOnClickListener(this)

        selectbook()
        initobservables()

    }
    fun initobservables(){

        livreVm.deleteBookResponse.observe(viewLifecycleOwner,{

            when(it){
                is WebServiceResulDelete ->{
                    Log.e("message","message=success")

                    requireActivity().onBackPressed()
                    Toast.makeText(requireContext(),"${it.message}",Toast.LENGTH_SHORT).show()

                }
                is WebServiceResultFailureDelete ->{
                    Log.e("messagessssssss","message=${it.message}")

                }
            }
        })

        livreVm.getCommentResponse.observe(viewLifecycleOwner,{
            when(it){

                is WebServiceResultComment ->{

                    Toast.makeText(requireContext(),"${it.message}",Toast.LENGTH_SHORT).show()
                    livreVm.selectcomment(livreVm.selectedBook!!.id_livre)

                }
                is WebServiceResultFailureComment ->{
                    Log.e("erreurcommentaire","message=${it.message}")
                }
            }
        })

        livreVm.selectCommentaire.observe(viewLifecycleOwner,{
            Log.e("aaaaaaaaaa","aaaaaaaa")

                when(it){
                    is WebServiceResultCommentaire ->{

                        Log.e("commentaireselecddddd","comentaireselecteeeddd=$it")
                        listcomment= it.commentaire

                        val adapter = CommentAdapter(requireContext(),listcomment)
                        listecommentaire.adapter = adapter
                    }

                    is WebServiceResultFailureCommentaire ->{
                        Log.e("Commentaire","MessageCommentaire=${it.message}")
                    }
                }

        })

        livreVm.selectLikesResponse.observe(viewLifecycleOwner,{
            when(it){
                is WebServiceResultLikes ->{

                    Log.e("likesss","likessssss=$it")

                    val likess = it.likes
                    Log.e("likkkkkk","likkkkkkk=$likess")

                    val client = preferences.getloginShared().id_client

                    var nbr_likes = 0
                    var nbr_dislikes = 0

                    found = false
                    foundLike = 1

                    for (i in 0..likess.size-1){

                        if (likess[i].id_client==client && likess[i].id_livre==livreVm.selectedBook!!.id_livre){

                            found = true

                            if(likess[i].likee=="1"){
                                foundLike = 1
                                like.setColorFilter(ContextCompat.getColor(requireContext(), R.color.purple_700), android.graphics.PorterDuff.Mode.SRC_IN)
                                dislike.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gris), android.graphics.PorterDuff.Mode.SRC_IN)

                            }else{
                                foundLike = 0
                                dislike.setColorFilter(ContextCompat.getColor(requireContext(), R.color.purple_700), android.graphics.PorterDuff.Mode.SRC_IN)
                                like.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gris), android.graphics.PorterDuff.Mode.SRC_IN)

                            }
                        }

                        if(likess[i].id_livre==livreVm.selectedBook!!.id_livre){
                            if(likess[i].likee=="1"){
                                nbr_likes++
                            }else{
                                nbr_dislikes++
                            }
                        }
                    }

                    number_like.setText(nbr_likes.toString())
                    number_dislike.setText(nbr_dislikes.toString())

                }
                is WebServiceResultFailurLikes ->{
                    Log.e("NoListelikes","likessss=${it.message}")
                }
            }
        })

        livreVm.updatesLikesResponse.observe(viewLifecycleOwner,{
            when(it){
                is WebServiceResultLikesUpdate ->{

                    Log.e("UpdatesLikesss","UpdatesLikesss=$it")
                }

                is WebServiceResultFailurLikesUpdates ->{
                    Log.e("UpdatesLikesssFaillure","UpdatesLikesssFaillure=${it.message}")
                }

            }
        })

        livreVm.insertLikeResponse.observe(viewLifecycleOwner,{
            when(it){

                is WebServiceResultLikesInsert ->{

                    Log.e("InsertLikes","InsertLikes=$it")
                }

                is WebServiceResultFailurLikesInsert ->{
                    Log.e("InsertFaillure","insertFaillure=${it.message}")
                }
            }
        })
    }


    fun selectbook(){

        if (livreVm.selectedBook != null){
            Log.e("livreeeeeeeee", "livre=${livreVm.selectedBook}")

            titre.setText(livreVm.selectedBook!!.titre)
            auteur.setText(livreVm.selectedBook!!.auteur)
            editeur.setText(livreVm.selectedBook!!.editeur)
            date_edition.setText(livreVm.selectedBook!!.dateedition)
            langue.setText(livreVm.selectedBook!!.langue)
            resume.setText(livreVm.selectedBook!!.resumer)
            disponibility.setText(livreVm.selectedBook!!.disponibilite)

            val url = getString(R.string.ipAdresse)+livreVm.selectedBook!!.photo_livre
            Log.e("photo", "photo=$url")
            Picasso.with(requireContext())
                .load(url)
                .into(imagelivre, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        //set animations here
                    }
                    override fun onError() {
                    }
                })
        }else{
            Log.e("livreeeeeeeee", "livre= null")
        }
    }

    override fun onClick(v: View) {

        when(v.id){

            R.id.btnconsultmybook -> {
                findNavController().navigate(ConsultMyBookFragmentDirections.actionConsultLivreToEditBook())
            }

            R.id.btndelete -> {

                val builder = AlertDialog.Builder(requireContext())

                builder.setTitle("Supprimer ce livre!!")
                builder.setMessage("voulez vous supprimer ce livre?")
                builder.setPositiveButton("Oui", { dialogInterface: DialogInterface, i: Int ->

                    livreVm.deletebook(livreVm.selectedBook!!.id_livre)

                })
                builder.setNegativeButton("Non", { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.cancel()
                })
                val alert11: AlertDialog = builder.create()
                alert11.show()
            }

            R.id.btncomment -> {

                val commentaire_ajouter = add_commentairemybook.text.toString()
                Log.e("Adddd","Addd=$commentaire_ajouter")
                var id_livre_show= livreVm.selectedBook!!.id_livre
                var id_client_use= preferences.getloginShared().id_client

                Log.e("id_client","id_clientConnecter=$id_client_use")
                Log.e("id_livre","id_livreeee=$id_livre_show")

                livreVm.getcomment(
                        id_livre = id_livre_show,
                        id_client = id_client_use,
                        commentaire = commentaire_ajouter
                )
                //keybord hide**********
                add_commentairemybook.setText("")
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }

            R.id.dislike ->{

                Log.e("grgrgr","found=$found id_client= ${preferences.getloginShared().id_client}")

                if(found){  // user like or dislike book

                    if(foundLike == 1){ //// user like book

                        livreVm.updatelike(
                                id_livre = livreVm.selectedBook!!.id_livre,
                                id_client = preferences.getloginShared().id_client,
                                likee = "0"
                        )
                    }
                }
                else{ // no like no dislike
                    livreVm.insertlike(
                            id_livre = livreVm.selectedBook!!.id_livre,
                            id_client = preferences.getloginShared().id_client,
                            likee = "0"
                    )
                }
            }
            R.id.like ->{
                if(found){  // user like or dislike book

                    if(foundLike == 0){ //// user like book
                        livreVm.updatelike(
                                id_livre = livreVm.selectedBook!!.id_livre,
                                id_client = preferences.getloginShared().id_client,
                                likee = "1"
                        )
                    }
                }
                else{ // no like no dislike
                    livreVm.insertlike(
                            id_livre = livreVm.selectedBook!!.id_livre,
                            id_client = preferences.getloginShared().id_client,
                            likee = "1"
                    )
                }
            }
        }
    }

}