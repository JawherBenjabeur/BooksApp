package com.example.your_books.my_app.data.requests

import com.example.your_books.my_app.models.User
import com.example.your_books.my_app.models.WebServiceResultData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiUser {


    companion object {

        operator fun  invoke() : ApiUser {
            val gson: Gson = GsonBuilder().setLenient().create()
            return Retrofit.Builder().baseUrl("http://10.192.76.34:8080/PFE/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(ApiUser::class.java)
        }
    }

    @FormUrlEncoded
    @POST("login.php")
    fun userLogin(
            @Field("login") login: String,
            @Field("password") password: String
    ) : Call<WebServiceResultData<List<User>>>


    @FormUrlEncoded
    @POST("Registration.php")
    fun userSignup(
            @Field("nom") nom:String,
            @Field("prenom") prenom:String,
            @Field("ville") ville:String,
            @Field("date") date:String,
            @Field("telephone") telephone:String,
            @Field("email") email:String,
            @Field("password") password: String,
            @Field("photo_client") photo_client:String
    ):Call<WebServiceResultData<List<User>>>


    @FormUrlEncoded
    @POST("update.php")
    fun userUpdate(
            @Field("id_client") id_client:String,
            @Field("nom") nom:String,
            @Field("prenom") prenom:String,
            @Field("ville") ville:String,
            @Field("date") date:String,
            @Field("telephone") telephone:String,
            @Field("email") email:String,
            @Field("photo_client") photo_client:String?
    ):Call<WebServiceResultData<List<User>>>

    @FormUrlEncoded
    @POST("updatepassword.php")
    fun userUpdatepass(
            @Field("id_client") id_client:String,
            @Field("password") password: String
    ):Call<WebServiceResultData<List<User>>>

    @FormUrlEncoded
    @POST("profilselected.php")
    fun userselected(
            @Field("id_client") id_client: String
    ):Call<WebServiceResultData<List<User>>>

}