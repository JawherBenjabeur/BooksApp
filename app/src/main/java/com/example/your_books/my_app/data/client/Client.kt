package com.example.your_books.my_app.data.client

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Client {

    companion object{
        private const val BASE_Url= "http://10.192.76.34/PFE/"
    }

    fun<Api> buildApi(
            api: Class<Api>
    ) :Api {
        return Retrofit.Builder()
                .baseUrl(BASE_Url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(api)
    }

}
