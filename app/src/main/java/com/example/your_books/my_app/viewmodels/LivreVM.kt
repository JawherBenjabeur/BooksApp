package com.example.your_books.my_app.viewmodels

import androidx.lifecycle.ViewModel
import com.example.your_books.my_app.models.Livre
import com.example.your_books.my_app.repositories.LivreRepo

class LivreVM: ViewModel (){

    val repo = LivreRepo()

    var servicesLiveData = repo.updateBookResponse
    var servicesLiveDataa = repo.userResponse

    var addLivreResponse = repo.addLivreResponse
    var getLivreResponse = repo.getLivreResponse

    var getBookResponse = repo.getBookResponse
    var updateBookResponse = repo.updateBookResponse
    var deleteBookResponse =repo.deleteResponse

    var getCommentResponse = repo.getCommentResponse
    var selectCommentaire = repo.selectCommentResponse

    val selectLikesResponse =repo.getLikesResponse
    val updatesLikesResponse =repo.updateLikesResponse
    val insertLikeResponse = repo.insertLikeResponse

    val selectRubriqueResponse = repo.rubriqueResponse
    val listRubriqueResponse = repo.listRubriqueResponse
    var addResResponse = repo.addResResponse
    var getLivreResResponse = repo.getLivreResResponse
    var updateResResponse = repo.updateResResponse




    fun addlivre(id_client :String, id_categorie:String, titre:String, auteur:String, editeur:String, dateedition:String, langue:String, resumer:String, photo_livre :String, disponibilite:String )=repo.addlivre(id_client,id_categorie,titre,auteur,editeur,dateedition,langue,resumer,photo_livre,disponibilite)

    fun getlivre(id_client :String)=repo.getbook(id_client)

    fun getallbook(search:String)=repo.getallbook(search)

    fun updatebook(id_livre :String,id_client :String, id_categorie:String, titre:String, auteur:String, editeur:String, dateedition:String, langue:String, resumer:String, photo_livre :String, disponibilite:String)=repo.updatebook(id_livre,id_client,id_categorie,titre,auteur,editeur,dateedition,langue,resumer,photo_livre,disponibilite)

    fun userselect(id_client: String) = repo.userselect(id_client)
    fun deletebook(id_livre: String )= repo.deletebook((id_livre))

    fun getcomment(id_livre: String,id_client: String,commentaire: String)=repo.insertcomment(id_livre,id_client,commentaire)
    fun selectcomment(id_livre: String)=repo.selectcomment(id_livre)

    fun getlikes(id_livre:String)= repo.getlikes(id_livre)
    fun updatelike(id_livre:String,id_client: String,likee:String)=repo.updatelike(id_livre,id_client,likee)
    fun insertlike(id_livre:String,id_client: String,likee:String)=repo.insertlike(id_livre,id_client,likee)

    fun getrubrique()=repo.selectrub()

    fun listrubrique()=repo.getrubrique()

    fun addRes(id_livre :Int, id_client_receveur:Int, id_client_demandeur:Int)=repo.addres(id_livre,id_client_receveur,id_client_demandeur)
    fun getallres(id_client_receveur:Int)=repo.getres(id_client_receveur)
    fun updateRes(id :Int, etat:Int, id_client_receveur: Int)=repo.updateres(id,etat, id_client_receveur)

    var selectedBook : Livre? = null

}