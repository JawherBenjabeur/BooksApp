package com.example.your_books.my_app.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.your_books.my_app.data.requests.ApiAdd
import com.example.your_books.my_app.data.requests.ApiDelete
import com.example.your_books.my_app.data.requests.ApiUser
import com.example.your_books.my_app.models.*
import retrofit2.Call
import retrofit2.Response

class LivreRepo {

    val addLivreResponse = MutableLiveData<WebServiceResult>()
    val getLivreResponse = MutableLiveData<WebServiceResult>()
    val getBookResponse = MutableLiveData<WebServiceResult>()
    val updateBookResponse = MutableLiveData<WebServiceResult>()
    val userResponse = MutableLiveData<WebServiceResult>()
    val getCommentResponse = MutableLiveData<WebServiceResult>()
    val selectCommentResponse = MutableLiveData<WebServiceResult>()
    val getLikesResponse = MutableLiveData<WebServiceResult>()
    val updateLikesResponse = MutableLiveData<WebServiceResult>()
    val insertLikeResponse = MutableLiveData<WebServiceResult>()

    val deleteResponse = MutableLiveData<WebServiceResult>()
    val rubriqueResponse = MutableLiveData<WebServiceResult>()
    val listRubriqueResponse = MutableLiveData<WebServiceResult>()

    val addResResponse = MutableLiveData<WebServiceResult>()
    val updateResResponse = MutableLiveData<WebServiceResult>()
    val getLivreResResponse = MutableLiveData<WebServiceResult>()




    fun addlivre(id_client: String, id_categorie: String, titre: String, auteur: String, editeur: String, dateedition: String, langue: String, resumer: String, photo_livre: String, disponibilite: String)
            : MutableLiveData<WebServiceResult> {

        var photo: String? = photo_livre
        if (photo_livre.length == 0) {
            photo = null
            Log.e("phootoooo", "photoinsert=$photo")
        }
        ApiAdd().insertbook(id_client, id_categorie, titre, auteur, editeur, dateedition, langue, resumer, photo, disponibilite)
                .enqueue(object : retrofit2.Callback<WebServiceResultData<List<Livre>>> {

                    override fun onResponse(call: Call<WebServiceResultData<List<Livre>>>?, response: Response<WebServiceResultData<List<Livre>>>?) {
                        if (response != null) {

                            if (response.body().status) {

                                addLivreResponse.value = WebServiceResultLivre(response.body().data!![0])

                            } else {

                                addLivreResponse.value = WebServiceResultFailureLivre(response.body().message.toString())
                            }
                        }
                    }

                    override fun onFailure(call: Call<WebServiceResultData<List<Livre>>>?, t: Throwable?) {

                        t!!.printStackTrace()
                        addLivreResponse.value = WebServiceResultFailureLivre(message = "error")
                    }

                })
        return addLivreResponse
    }

    fun deletebook(id_livre: String): MutableLiveData<WebServiceResult> {
        ApiDelete().deletebook(id_livre).enqueue(object : retrofit2.Callback<WebServiceResultData<Any>> {
            override fun onResponse(call: Call<WebServiceResultData<Any>>?, response: Response<WebServiceResultData<Any>>?) {
                if (response != null) {

                    if (response.body().status) {
                        deleteResponse.value = WebServiceResulDelete(response.body().message, response.body().status)
                    } else {
                        deleteResponse.value = WebServiceResultFailureDelete(response.body().message)
                    }
                }
            }

            override fun onFailure(call: Call<WebServiceResultData<Any>>?, t: Throwable?) {
                Log.e("messss", "Reponse=${t!!.message}")

                t!!.printStackTrace()
                deleteResponse.value = WebServiceResultFailureDelete(message = "error")
            }

        })
        return deleteResponse
    }

    fun getbook(id_client: String): MutableLiveData<WebServiceResult> {
        ApiAdd().getbook(id_client).enqueue(object : retrofit2.Callback<WebServiceResultData<List<Livre>>> {

            override fun onResponse(call: Call<WebServiceResultData<List<Livre>>>?, response: Response<WebServiceResultData<List<Livre>>>?) {

                if (response!!.isSuccessful) {

                    getLivreResponse.value = WebServiceResultBook(response.body().data!!)

                } else {
                    getLivreResponse.value = WebServiceResultFailureBook(response.body().message.toString())
                }
            }

            override fun onFailure(call: Call<WebServiceResultData<List<Livre>>>?, t: Throwable?) {
                t!!.printStackTrace()
                getLivreResponse.value = WebServiceResultFailureBook(message = "error")
            }

        })
        return getLivreResponse
    }

    fun getrubrique():MutableLiveData<WebServiceResult>{
        ApiAdd().getrubrique().enqueue(object :retrofit2.Callback<WebServiceResultData<List<ListRubrique>>>{

            override fun onResponse(call: Call<WebServiceResultData<List<ListRubrique>>>?, response: Response<WebServiceResultData<List<ListRubrique>>>?) {

                if (response!!.isSuccessful){
                    listRubriqueResponse.value=WebServiceResultListRubriques(response.body().data!!)
                    Log.e("ListeRub","ListRub=${response.body()}")
                }else{
                    listRubriqueResponse.value=WebServiceResultFailurListRubriques(response.body().message)
                }
            }

            override fun onFailure(call: Call<WebServiceResultData<List<ListRubrique>>>?, t: Throwable?) {
                t!!.printStackTrace()
                listRubriqueResponse.value=WebServiceResultFailurListRubriques(message = "erreur de récuperation" )
            }

        })
        return listRubriqueResponse
    }

    fun insertcomment(id_client: String, id_livre: String, commentaire: String): MutableLiveData<WebServiceResult> {
        ApiAdd().insertcomment(id_client, id_livre, commentaire).enqueue(object : retrofit2.Callback<WebServiceResultData<Commentaire>> {
            override fun onResponse(call: Call<WebServiceResultData<Commentaire>>?, response: Response<WebServiceResultData<Commentaire>>?) {

                if (response!!.isSuccessful) {

                    getCommentResponse.value = WebServiceResultComment(response.body().message)
                    Log.e("Booddyyy","Boddyyyyyy=${response.body()}")

                } else {
                    getCommentResponse.value = WebServiceResultFailureComment(response.body().message)
                    Log.e("Faillure","Faillure=${response.message()}")
                }

            }

            override fun onFailure(call: Call<WebServiceResultData<Commentaire>>?, t: Throwable?) {
                Log.e("Erreur comment", "erreurComent=${t!!.message}")

                t!!.printStackTrace()
                getCommentResponse.value = WebServiceResultFailureComment(message = "erreur")
            }

        })
        return getCommentResponse
    }

    fun selectcomment(id_livre: String):MutableLiveData<WebServiceResult>{
        ApiAdd().selectcomment(id_livre).enqueue(object :retrofit2.Callback<WebServiceResultData<List<CommentSelect>>>{

            override fun onResponse(call: Call<WebServiceResultData<List<CommentSelect>>>?, response: Response<WebServiceResultData<List<CommentSelect>>>?) {
                if (response!=null){
                    if (response!!.isSuccessful){

                        selectCommentResponse.value = WebServiceResultCommentaire(response.body().data!!)
                        Log.e("Mycommmennt","MyComment=${response.body().data}")

                    }else{

                        selectCommentResponse.value=WebServiceResultFailureCommentaire(response.body().message)
                        Log.e("Mycommmennt","MyCommenterreur=${response.body().message}")
                    }
                }

            }

            override fun onFailure(call: Call<WebServiceResultData<List<CommentSelect>>>?, t: Throwable?) {
                t!!.printStackTrace()
                selectCommentResponse.value = WebServiceResultFailureCommentaire(message = "erreur")
            }
        })
        return selectCommentResponse
    }

    fun getlikes(id_livre:String):MutableLiveData<WebServiceResult>{
        ApiAdd().getlikes(id_livre).enqueue(object :retrofit2.Callback<WebServiceResultData<List<Likes>>>{
            override fun onResponse(call: Call<WebServiceResultData<List<Likes>>>?, response: Response<WebServiceResultData<List<Likes>>>?) {
                if (response!=null){
                    if (response!!.isSuccessful){
                        getLikesResponse.value=WebServiceResultLikes(response.body().data!!)


                    }else{
                        getLikesResponse.value=WebServiceResultFailurLikes(response.body().message)
                    }
                }
            }

            override fun onFailure(call: Call<WebServiceResultData<List<Likes>>>?, t: Throwable?) {

                t!!.printStackTrace()
            getLikesResponse.value=WebServiceResultFailurLikes(message = "erreur de récuperation likes")}

        })
        return getLikesResponse
    }



    fun selectrub():MutableLiveData<WebServiceResult>{
        ApiAdd().selectrub().enqueue(object :retrofit2.Callback<WebServiceResultData<List<Livre>>>{
            override fun onResponse(call: Call<WebServiceResultData<List<Livre>>>?, response: Response<WebServiceResultData<List<Livre>>>?) {
                if (response!!.isSuccessful){
                    rubriqueResponse.value=WebServiceResultRubriques(response.body().data!!)
                }else{
                    rubriqueResponse.value=WebServiceResultFailurRubriques(response.body().message)
                }
            }

            override fun onFailure(call: Call<WebServiceResultData<List<Livre>>>?, t: Throwable?) {
                t!!.printStackTrace()
                rubriqueResponse.value=WebServiceResultFailurRubriques(message = "erreur de récuperation" )
            }

        })
        return rubriqueResponse
    }


    fun getallbook(search:String): MutableLiveData<WebServiceResult> {
        ApiAdd().getallbook(search).enqueue(object : retrofit2.Callback<WebServiceResultData<List<Livre>>> {
            override fun onResponse(call: Call<WebServiceResultData<List<Livre>>>?, response: Response<WebServiceResultData<List<Livre>>>?) {

                if (response!!.isSuccessful) {

                    getBookResponse.value = WebServiceResultBook(response.body().data!!)

                } else {
                    getBookResponse.value = WebServiceResultFailureBook(response.body().message.toString())
                }
            }

            override fun onFailure(call: Call<WebServiceResultData<List<Livre>>>?, t: Throwable?) {
                t!!.printStackTrace()
                getBookResponse.value = WebServiceResultFailureBook(message = "error")
            }
        })
        return getBookResponse
    }

    fun updatebook(id_livre: String, id_client: String, id_categorie: String, titre: String, auteur: String, editeur: String, dateedition: String, langue: String, resumer: String, photo_livre: String, disponibilite: String)
            : MutableLiveData<WebServiceResult> {
        ApiAdd().updatebook(id_livre, id_client, id_categorie, titre, auteur, editeur, dateedition, langue, resumer, photo_livre, disponibilite)
                .enqueue(object : retrofit2.Callback<WebServiceResultData<List<Livre>>> {

                    override fun onResponse(call: Call<WebServiceResultData<List<Livre>>>?, response: Response<WebServiceResultData<List<Livre>>>?) {
                        if (response!!.isSuccessful) {
                            if (response.body().status) {
                                updateBookResponse.value = WebServiceResultLivre(response.body().data!![0])
                            } else {
                                updateBookResponse.value = WebServiceResultFailure(response.body().message.toString())
                            }
                        }

                    }

                    override fun onFailure(call: Call<WebServiceResultData<List<Livre>>>?, t: Throwable?) {
                        t!!.printStackTrace()
                        updateBookResponse.value = WebServiceResultFailure(message = "error")
                    }
                })
        return updateBookResponse
    }

    fun userselect(id_client: String): MutableLiveData<WebServiceResult> {
        ApiUser().userselected(id_client)
                .enqueue(object : retrofit2.Callback<WebServiceResultData<List<User>>> {
                    override fun onResponse(call: Call<WebServiceResultData<List<User>>>?, response: Response<WebServiceResultData<List<User>>>?) {

                        if (response!!.isSuccessful) {
                            if (response.body().status) {

                                userResponse.value = WebServiceResultLoginSuccess(response.body().data!![0])

                            } else {
                                userResponse.value = WebServiceResultFailure(response.body().message.toString())
                            }
                        }
                    }

                    override fun onFailure(call: Call<WebServiceResultData<List<User>>>?, t: Throwable?) {
                        t!!.printStackTrace()
                        userResponse.value = WebServiceResultFailure(message = "error")
                    }
                })
        return userResponse
    }

    fun updatelike(id_livre: String,id_client: String,likee:String):MutableLiveData<WebServiceResult>{
        ApiAdd().updatelikes(id_livre,id_client,likee).enqueue(object :retrofit2.Callback<WebServiceResultData<List<Likes>>>{
            override fun onResponse(call: Call<WebServiceResultData<List<Likes>>>?, response: Response<WebServiceResultData<List<Likes>>>?) {
                Log.e("updatelike","ressssss=${response!!.body()}")
                if (response!!.isSuccessful){
                    if (response.body().status){

                        updateLikesResponse.value=WebServiceResultLikesUpdate(response.body().data!!)

                    }else{
                        updateLikesResponse.value=WebServiceResultFailurLikesUpdates(response.body().message)
                    }
                }
            }

            override fun onFailure(call: Call<WebServiceResultData<List<Likes>>>?, t: Throwable?) {
                t!!.printStackTrace()
                updateLikesResponse.value=WebServiceResultFailurLikesUpdates(message = "erreur")
            }
        })
        return updateLikesResponse
    }

    fun insertlike(id_livre: String,id_client: String,likee:String):MutableLiveData<WebServiceResult>{
        ApiAdd().insertlikes(id_livre,id_client,likee).enqueue(object :retrofit2.Callback<WebServiceResultData<List<Likes>>>{
            override fun onResponse(call: Call<WebServiceResultData<List<Likes>>>?, response: Response<WebServiceResultData<List<Likes>>>?) {
             Log.e("ressssssss","ressssss=${response!!.body()}")
                if (response!!.isSuccessful){
                    if (response.body().status){
                        insertLikeResponse.value=WebServiceResultLikesInsert(response.body().data!!)
                    }else{
                        insertLikeResponse.value=WebServiceResultFailurLikesUpdates(response.body().message)
                    }
                }
            }

            override fun onFailure(call: Call<WebServiceResultData<List<Likes>>>?, t: Throwable?) {
                t!!.printStackTrace()
                insertLikeResponse.value=WebServiceResultFailurLikesUpdates(message = "erreur")
            }

        })
        return insertLikeResponse
    }

    fun addres(id_livre: Int, id_client_receveur: Int, id_client_demandeur: Int)
            : MutableLiveData<WebServiceResult> {

        ApiAdd().insertres(id_livre, id_client_receveur, id_client_demandeur)
                .enqueue(object : retrofit2.Callback<WebServiceResultData<Reservation>> {

                    override fun onResponse(call: Call<WebServiceResultData<Reservation>>?, response: Response<WebServiceResultData<Reservation>>?) {
                        if (response != null) {
                            Log.e("reeees", response.toString())
                            if (response.body()!!.status) {

                                addResResponse.value = WebServiceResultResInsert(response.message())

                            } else {

                                addResResponse.value = WebServiceResultFailureRes(response.body()!!.message.toString())
                            }
                        }
                    }

                    override fun onFailure(call: Call<WebServiceResultData<Reservation>>?, t: Throwable?) {

                        t!!.printStackTrace()
                        addResResponse.value = WebServiceResultFailureRes(message = t.message!!)
                    }

                })
        return addResResponse
    }
    fun getres(id_client_receveur: Int): MutableLiveData<WebServiceResult> {
        ApiAdd().getallres(id_client_receveur).enqueue(object : retrofit2.Callback<WebServiceResultData<ArrayList<LivreReservation>>> {

            override fun onResponse(call: Call<WebServiceResultData<ArrayList<LivreReservation>>>?, response: Response<WebServiceResultData<ArrayList<LivreReservation>>>?) {

                if (response!!.isSuccessful) {

                    getLivreResResponse.value = WebServiceResultRes(response.body()!!.data!!)

                } else {
                    getLivreResResponse.value = WebServiceResultFailureRes(response.body()!!.message.toString())
                }
            }

            override fun onFailure(call: Call<WebServiceResultData<ArrayList<LivreReservation>>>?, t: Throwable?) {
                t!!.printStackTrace()
                getLivreResResponse.value = WebServiceResultFailureRes(message = "error")
            }

        })
        return getLivreResResponse
    }


    fun updateres(id: Int, etat: Int, id_client_receveur: Int)
            : MutableLiveData<WebServiceResult> {

        ApiAdd().updateres(id, etat, id_client_receveur)
                .enqueue(object : retrofit2.Callback<WebServiceResultData<ArrayList<LivreReservation>>> {

                    override fun onResponse(call: Call<WebServiceResultData<ArrayList<LivreReservation>>>?, response: Response<WebServiceResultData<ArrayList<LivreReservation>>>?) {
                        if (response != null) {
                            Log.e("reeees", response.toString())
                            if (response.body()!!.status) {

                                updateResResponse.value = WebServiceResultRes(response.body()!!.data!!)

                            } else {

                                updateResResponse.value = WebServiceResultFailureRes(response.body()!!.message.toString())
                            }
                        }
                    }

                    override fun onFailure(call: Call<WebServiceResultData<ArrayList<LivreReservation>>>?, t: Throwable?) {

                        t!!.printStackTrace()
                        updateResResponse.value = WebServiceResultFailureRes(message = t.message!!)
                    }

                })
        return updateResResponse
    }

}
