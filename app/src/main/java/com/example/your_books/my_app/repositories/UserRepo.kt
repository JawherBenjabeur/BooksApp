package com.example.your_books.my_app.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.your_books.my_app.data.requests.ApiUser
import com.example.your_books.my_app.models.*
import retrofit2.Call
import retrofit2.Response

class UserRepo{
    val loginResponse = MutableLiveData<WebServiceResult>()
    val signupResponse = MutableLiveData<WebServiceResult>()
    val updateResponse = MutableLiveData<WebServiceResult>()
    val updatepassResponse = MutableLiveData<WebServiceResult>()


    fun userLogin(login: String, password: String): MutableLiveData<WebServiceResult> {


        ApiUser().userLogin(login , password)
            .enqueue(object : retrofit2.Callback<WebServiceResultData<List<User>>> {

                override fun onResponse(
                        call: Call<WebServiceResultData<List<User>>>?, response: Response<WebServiceResultData<List<User>>>?) {
                    if (response!!.isSuccessful) {

                        if (response.body().status) {

                            loginResponse.value = WebServiceResultLoginSuccess(response.body().data!![0])

                        } else {

                            loginResponse.value = WebServiceResultFailure(response.body().message.toString())
                        }
                    }
                }
                override fun onFailure(call: Call<WebServiceResultData<List<User>>>?, t: Throwable?) {
                    Log.e("message","message=${t!!.message}")
                    t!!.printStackTrace()
                    loginResponse.value = WebServiceResultFailure(message = "error")
                }
            })
        return loginResponse
    }



    fun userSignup(nom:String, prenom:String, ville:String, date:String, telephone:String, email:String, password: String,photo_client:String): MutableLiveData<WebServiceResult> {


        ApiUser().userSignup(nom, prenom, ville, date, telephone, email, password, photo_client)
                .enqueue(object : retrofit2.Callback<WebServiceResultData<List<User>>> {

                    override fun onResponse(
                            call: Call<WebServiceResultData<List<User>>>?, response: Response<WebServiceResultData<List<User>>>?) {
                        if (response!!.isSuccessful) {
                            if (response.body().status) {

                                signupResponse.value = WebServiceResultLoginSuccess(response.body().data!![0])

                            } else {

                                signupResponse.value = WebServiceResultFailure(response.body().message.toString())

                            }
                        }
                    }
                    override fun onFailure(call: Call<WebServiceResultData<List<User>>>?, t: Throwable?) {
                        Log.e("messssaaaa","messsss=${t!!.message}")
                        signupResponse.value = WebServiceResultFailure(message = "error")
                    }
                })
        return signupResponse
    }

    fun userUpdate( id_client:String,nom:String, prenom:String, ville:String, date:String, telephone:String, email:String,photo_client:String)
                  : MutableLiveData<WebServiceResult> {

        Log.e("résultat= $id_client $nom $prenom $ville $date $telephone $email $photo_client","résultat")

        ApiUser().userUpdate(id_client, nom, prenom, ville, date, telephone, email,photo_client)
            .enqueue(object : retrofit2.Callback<WebServiceResultData<List<User>>> {

                override fun onResponse(
                    call: Call<WebServiceResultData<List<User>>>?, response: Response<WebServiceResultData<List<User>>>?) {
                    Log.e("message","true response=${response!!.body()}")
                    if (response!!.isSuccessful) {

                        if (response.body().status) {

                            updateResponse.value = WebServiceResultLoginSuccess(response.body().data!![0])

                        } else {

                            updateResponse.value = WebServiceResultFailure(response.body().message.toString())

                        }
                    }
                }
                override fun onFailure(call: Call<WebServiceResultData<List<User>>>?, t: Throwable?) {
                    t!!.printStackTrace()
                    updateResponse.value = WebServiceResultFailure(message = "error")
                }
            })
        return updateResponse
    }

    fun userUpdatepass(id_client: String,password: String): MutableLiveData<WebServiceResult>{

        ApiUser().userUpdatepass(id_client,password).enqueue(object : retrofit2.Callback<WebServiceResultData<List<User>>> {
            override fun onResponse(
                    call: Call<WebServiceResultData<List<User>>>?,response: Response<WebServiceResultData<List<User>>>?){
                if (response!!.isSuccessful){
                    if (response.body().status){

                        updatepassResponse.value = WebServiceResultLoginSuccess(response.body().data!![0])
                    }else{
                        updatepassResponse.value = WebServiceResultFailure(response.body().message.toString())
                    }
                }
            }
            override fun onFailure(call: Call<WebServiceResultData<List<User>>>?, t:Throwable?){
                t!!.printStackTrace()
                updatepassResponse.value=WebServiceResultFailure(message = "error")
            }
        })
        return updatepassResponse
    }


}

