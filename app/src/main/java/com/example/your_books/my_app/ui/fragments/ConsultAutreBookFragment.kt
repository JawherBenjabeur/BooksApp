package com.example.your_books.my_app.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.your_books.R
import com.example.your_books.my_app.models.*
import com.example.your_books.my_app.ui.adapters.CommentAdapter
import com.example.your_books.my_app.viewmodels.LivreVM
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_autre_book.*

class ConsultAutreBookFragment: Fragment(),View.OnClickListener {

    lateinit var preferences : SharedPreferences
    lateinit var livreVm: LivreVM
    lateinit var listcomment :List<CommentSelect>

    //
    var found = false
    var foundLike = 1

    var nbr_likes = 0
    var nbr_dislikes = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_autre_book,container,false)
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

        initobservables()
        bookselected()
        btnconsulautretbook.setOnClickListener(this)
        btn_addcomment.setOnClickListener(this)
        likeother.setOnClickListener(this)
        dislikeother.setOnClickListener(this)
        btn_reserver.setOnClickListener(this)



    }

    fun initobservables(){

        livreVm.getCommentResponse.observe(viewLifecycleOwner,{
            when(it){

                is WebServiceResultComment ->{

                    livreVm.selectcomment(livreVm.selectedBook!!.id_livre)

                }
                is WebServiceResultFailureComment ->{
                    Log.e("erreurcommentaire","message=${it.message}")
                }
            }
        })

        livreVm.selectCommentaire.observe(viewLifecycleOwner,{

            when(it){
                is WebServiceResultCommentaire ->{

                    Log.e("commentaireselected","comentaireselected=$it")
                    listcomment= it.commentaire

                    val adapter = CommentAdapter(requireContext(),listcomment)
                    listecommentaireautre.adapter = adapter
                }

                is WebServiceResultFailureCommentaire ->{
                    Log.e("Commentaire","MessageCommentaire=${it.message}")
                }
            }

        })

        livreVm.selectLikesResponse.observe(viewLifecycleOwner,{

            if(it!=null){
                when(it){
                    is WebServiceResultLikes ->{

                        Log.e("likesss","likessssss=$it")

                        val likess = it.likes
                        Log.e("likkkkkk","likkkkkkk=$likess")

                        val client = preferences.getloginShared().id_client

                        nbr_likes = 0
                        nbr_dislikes = 0

                        found = false
                        foundLike = 1

                        for (i in 0..likess.size-1){

                            if (likess[i].id_client==client && likess[i].id_livre==livreVm.selectedBook!!.id_livre){

                                found = true

                                if(likess[i].likee=="1"){
                                    foundLike = 1
                                    likeother.setColorFilter(ContextCompat.getColor(requireContext(), R.color.purple_700), android.graphics.PorterDuff.Mode.SRC_IN)
                                    dislikeother.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gris), android.graphics.PorterDuff.Mode.SRC_IN)

                                }else{
                                    foundLike = 0
                                    dislikeother.setColorFilter(ContextCompat.getColor(requireContext(), R.color.purple_700), android.graphics.PorterDuff.Mode.SRC_IN)
                                    likeother.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gris), android.graphics.PorterDuff.Mode.SRC_IN)

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

                        livreVm.selectLikesResponse.value = null
                    }

                    is WebServiceResultFailurLikes ->{
                        Log.e("NoListelikes","likessss=${it.message}")
                        livreVm.selectLikesResponse.value = null
                    }
                }
            }

        })

         livreVm.addResResponse.observe(viewLifecycleOwner, Observer {

            if(it!=null){

                when (it) {
                    is WebServiceResultFailureRes -> {
                        Log.e("Failure",it.message)

                    }
                    is WebServiceResultResInsert -> {
                        Log.e("Success",it.res.toString())
                        // Log.e("Success",it.token)
                        btn_reserver.visibility=View.GONE
                        demandesend.visibility=View.VISIBLE
                    }
                }
            }
        })
        livreVm.updatesLikesResponse.observe(viewLifecycleOwner,{
            Log.e("updatesLikesResponse","observer")

            if(it!=null){
                when(it){
                    is WebServiceResultLikesUpdate ->{

                        Log.e("updateeLikkke","updateeLikkke=$it")
                        Log.e("updateeLikkke","foundLike=$foundLike")

                        if(foundLike == 0) { ////
                            foundLike = 1
                            likeother.setColorFilter(ContextCompat.getColor(requireContext(), R.color.purple_700), android.graphics.PorterDuff.Mode.SRC_IN)
                            dislikeother.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gris), android.graphics.PorterDuff.Mode.SRC_IN)

                            nbr_likes++
                            nbr_dislikes--
                            number_like.setText(nbr_likes.toString())
                            number_dislike.setText(nbr_dislikes.toString())

                        }else{
                            foundLike = 0
                            likeother.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gris), android.graphics.PorterDuff.Mode.SRC_IN)
                            dislikeother.setColorFilter(ContextCompat.getColor(requireContext(), R.color.purple_700), android.graphics.PorterDuff.Mode.SRC_IN)

                            nbr_likes--
                            nbr_dislikes++
                            number_like.setText(nbr_likes.toString())
                            number_dislike.setText(nbr_dislikes.toString())
                        }

                        livreVm.updatesLikesResponse.value = null
                    }

                    is WebServiceResultFailurLikesUpdates ->{
                        Log.e("WebServiceResultFailure","MessageCommentaire=${it.message}")
                        livreVm.updatesLikesResponse.value = null
                    }
                }
            }

        })

        livreVm.insertLikeResponse.observe(viewLifecycleOwner,{

            if(it!=null){
                when(it){

                    is WebServiceResultLikesInsert ->{

                        Log.e("InsertLikes","InsertLikes=$it")

                        found = true
                        if(foundLike==1){
                            likeother.setColorFilter(ContextCompat.getColor(requireContext(), R.color.purple_700), android.graphics.PorterDuff.Mode.SRC_IN)
                            dislikeother.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gris), android.graphics.PorterDuff.Mode.SRC_IN)

                            nbr_likes++
                            number_like.setText(nbr_likes.toString())
                        }else{
                            likeother.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gris), android.graphics.PorterDuff.Mode.SRC_IN)
                            dislikeother.setColorFilter(ContextCompat.getColor(requireContext(), R.color.purple_700), android.graphics.PorterDuff.Mode.SRC_IN)

                            nbr_dislikes++
                            number_dislike.setText(nbr_dislikes.toString())
                        }

                        livreVm.insertLikeResponse.value = null
                    }

                    is WebServiceResultFailurLikesInsert ->{
                        Log.e("InsertFaillure","insertFaillure=${it.message}")
                        livreVm.insertLikeResponse.value = null
                    }
                }
            }

        })
    }

    fun bookselected(){

        val user = preferences.getloginShared()

        val livreselected= livreVm.selectedBook

        if (livreselected !=null ){
            Log.e("livreconsult","livreconsult=${livreselected}")

            settitre.setText(livreselected!!.titre)
            setauteur.setText(livreselected!!.auteur)
            setediteur.setText(livreselected!!.editeur)
            setdate_edition.setText(livreselected!!.dateedition)
            setlangue.setText(livreselected!!.langue)
            setresume.setText(livreselected!!.resumer)
            setdisponibility.setText(livreselected!!.disponibilite)
            if(livreselected!!.id_client== user.id_client){
                btnconsulautretbook.visibility = View.GONE
            }
            if (livreselected!!.id_client==user.id_client){
                btn_reserver.visibility = View.GONE
            }
            if(livreVm.selectedBook!!.disponibilite=="non"){
                btn_reserver.visibility=View.GONE
            }
            if (livreselected!!.id_client== user.id_client){
                demandesend.visibility=View.GONE
                    }



            val url = getString(R.string.ipAdresse)+livreselected!!.photo_livre
            Log.e("photo","photo=$url")
            Picasso.with(requireContext())
                    .load(url)
                    .into(imagelivreconsult, object: com.squareup.picasso.Callback {
                        override fun onSuccess() {

                            //set animations here
                        }
                        override fun onError() {
                        }

                    })

        }else{
            Log.e("livreconsult","livreconsult=null")
        }

    }

    override fun onClick(v: View) {

        when(v.id){

            R.id.btnconsulautretbook ->{
                findNavController().navigate(ConsultAutreBookFragmentDirections.actionConsultAutreBookToConsultAutreProfil())
            }
            R.id.btn_reserver ->{
                livreVm.addRes(livreVm.selectedBook!!.id_livre.toInt(),livreVm.selectedBook!!.id_client.toInt(),preferences.getloginShared().id_client.toInt())
            }
            R.id.btn_addcomment ->{
                val commentaire_ajouter = add_commentaire.text.toString()
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
                add_commentaire.setText("")
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                Toast.makeText(requireContext(),"Commentaire ajouter", Toast.LENGTH_SHORT).show()
            }

            R.id.likeother ->{
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
                    foundLike = 1
                    livreVm.insertlike(
                            id_livre = livreVm.selectedBook!!.id_livre,
                            id_client = preferences.getloginShared().id_client,
                            likee = "1"
                    )
                }
            }
            R.id.dislikeother ->{

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
                    foundLike = 0
                    livreVm.insertlike(
                            id_livre = livreVm.selectedBook!!.id_livre,
                            id_client = preferences.getloginShared().id_client,
                            likee = "0"
                    )
                }
            }
        }

    }
}