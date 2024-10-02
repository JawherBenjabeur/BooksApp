package com.example.your_books.my_app.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.your_books.R
import com.example.your_books.R.layout.activity_login
import com.example.your_books.my_app.models.SharedPreferences
import com.example.your_books.my_app.models.WebServiceResultFailure
import com.example.your_books.my_app.models.WebServiceResultLoginSuccess
import com.example.your_books.my_app.viewmodels.LoginVM
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*

open class LoginActivity : AppCompatActivity(), View.OnClickListener  {

    lateinit var loginVm: LoginVM
    lateinit var sharedpreference : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(activity_login)


        sharedpreference = SharedPreferences(this)
        loginVm = ViewModelProvider(this).get(LoginVM::class.java)

        initObservables()
        btn_login.setOnClickListener(this)

    }

    private fun initObservables() {

        loginVm.servicesLiveData.observe(this, Observer {

            Log.e("message", "result=$it")

            when (it) {

                is WebServiceResultLoginSuccess -> {

                    sharedpreference.setloginShared(
                            it.user.id_client,it.user.nom,it.user.prenom,it.user.ville,it.user.datedenaissance,it.user.numerotelephone,it.user.email,it.user.motdepass!!,it.user.photo_client)

                    Toast.makeText(this, "Bienvenue", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, AcceuilActivity::class.java)
                    this.startActivity(intent)
                    finish()

                }

                is WebServiceResultFailure -> {

                    Snackbar.make(content,it.message,Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }

    //*******************Verifications Adresse E-mail*******************
    fun isValidEmail(login: String): Boolean {
        return !TextUtils.isEmpty(login) && Patterns.EMAIL_ADDRESS.matcher(login).matches()
    }

    override fun onClick(v: View) {

        when (v.id) {

            R.id.btn_login -> {

                //Hide keyboard
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(content.getWindowToken(), 0)

                val user = sharedpreference.getloginShared()

                val login = logintxt.text.toString()
                val password = passwordlogintxt.text.toString()

                if(login.isEmpty()){
                    logintxt.setError(getString(R.string.msg_saisirlogin))
                    return
                }

                if(!isValidEmail(login)){
                    logintxt.setError(getString(R.string.msg_verifieremail))
                    return
                }

                if(password.isEmpty()){

                    passwordlogintxt.setError(getString(R.string.msg_saisirpassword))
                    return
                }


               loginVm.userLogin(login = login, password = password)

            }


        }
    }

}


