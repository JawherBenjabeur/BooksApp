package com.example.your_books.my_app.data.requests

import com.example.your_books.my_app.models.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiAdd {
    companion object {

        operator fun  invoke() : ApiAdd {

            val gson: Gson = GsonBuilder().setLenient().create()
            return Retrofit.Builder().baseUrl("http://10.192.76.34:8080/PFE/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(ApiAdd::class.java)
        }
    }

    @POST("categorie.php")
    fun getcategorie():Call<WebServiceResultData<List<Categorie>>>

    @FormUrlEncoded
    @POST("add_book.php")
    fun insertbook(
            @Field("id_client")  id_client:String,
            @Field("id_categorie") id_categorie:String,
            @Field("titre") titre:String,
            @Field("auteur") auteur:String,
            @Field("editeur") editeur:String,
            @Field("dateedition") dateedition:String,
            @Field("langue") langue:String,
            @Field("resumer") resumer:String,
            @Field("photo_livre") photo_livre:String?,
            @Field("disponibilite") disponibilite:String
    ):Call<WebServiceResultData<List<Livre>>>

    @FormUrlEncoded
    @POST("getbook.php")
    fun getbook(
            @Field ("id_client") id_client: String
    ):Call<WebServiceResultData<List<Livre>>>


    @FormUrlEncoded
    @POST("search.php")
    fun getallbook(
            @Field("search")search:String
    ):Call<WebServiceResultData<List<Livre>>>

    @FormUrlEncoded
    @POST("updatebook.php")
    fun updatebook(
            @Field("id_livre") id_livre:String,
            @Field("id_client")  id_client:String,
            @Field("id_categorie") id_categorie:String,
            @Field("titre") titre:String,
            @Field("auteur") auteur:String,
            @Field("editeur") editeur:String,
            @Field("dateedition") dateedition:String,
            @Field("langue") langue:String,
            @Field("resumer") resumer:String,
            @Field("photo_livre") photo_livre:String,
            @Field("disponibilite") disponibilite:String
    ):Call<WebServiceResultData<List<Livre>>>

    @FormUrlEncoded
    @POST("insertcom.php")
    fun insertcomment(
            @Field("id_livre") id_livre:String,
            @Field("id_client") id_client: String,
            @Field("commentaire") commentaire:String
    ):Call<WebServiceResultData<Commentaire>>

    @FormUrlEncoded
    @POST("selectcom.php")
    fun selectcomment(
            @Field("id_livre") id_livre: String
    ):Call<WebServiceResultData<List<CommentSelect>>>

    @FormUrlEncoded
    @POST("getlike.php")
    fun getlikes(
            @Field("id_livre") id_livre:String
    ):Call<WebServiceResultData<List<Likes>>>


    @POST("selectrub.php")
    fun selectrub():Call<WebServiceResultData<List<Livre>>>

    @POST("listrubrique.php")
    fun getrubrique():Call<WebServiceResultData<List<ListRubrique>>>

    @FormUrlEncoded
    @POST("updatelike.php")
    fun updatelikes(
            @Field("id_livre") id_livre: String,
            @Field("id_client") id_client: String,
            @Field("likee")likee:String
    ):Call<WebServiceResultData<List<Likes>>>

    @FormUrlEncoded
    @POST("insertlike.php")
    fun insertlikes(
            @Field("id_livre") id_livre: String,
            @Field("id_client") id_client: String,
            @Field("likee")likee:String
    ):Call<WebServiceResultData<List<Likes>>>

    @FormUrlEncoded
    @POST("getallres.php")
    fun getallres(
            @Field("id_client_receveur") id_client_receveur:Int
    ):Call<WebServiceResultData<ArrayList<LivreReservation>>>


    @FormUrlEncoded
    @POST("addres.php")
    fun insertres(
            @Field("id_livre") id_livre:Int,
            @Field("id_client_receveur") id_client_receveur: Int,
            @Field("id_client_demandeur") id_client_demandeur:Int
    ):Call<WebServiceResultData<Reservation>>

    @FormUrlEncoded
    @POST("updateres.php")
    fun updateres(
            @Field("id") id:Int,
            @Field("etat") etat: Int,
            @Field("id_client_receveur") id_client_receveur: Int
    ):Call<WebServiceResultData<ArrayList<LivreReservation>>>
}