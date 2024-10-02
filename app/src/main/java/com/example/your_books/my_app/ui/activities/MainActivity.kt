package com.example.your_books.my_app.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.your_books.R
import com.example.your_books.my_app.models.SharedPreferences

class MainActivity : AppCompatActivity() {


    lateinit var sharedpreference : SharedPreferences
    var first = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedpreference = SharedPreferences(this)

        val user = sharedpreference.getloginShared()

        if (user.id_client.length> 0){

            val intent = Intent(this, AcceuilActivity::class.java)
            this.startActivity(intent)
        }

        setContentView(R.layout.activity_main)


        val button_signup = findViewById<Button>(R.id.Inscription)
        val button_signin = findViewById<Button>(R.id.Connexion)


        button_signup.setOnClickListener {

            val intent = Intent(this, RegisterActivity::class.java)
            this.startActivity(intent)

        }


        button_signin.setOnClickListener {

            val intent = Intent(this, LoginActivity::class.java)
            this.startActivity(intent)

        }

    }

    override fun onResume() {
        super.onResume()

        if(first){
            first = false
        }else{
            val user = sharedpreference.getloginShared()
            if (user.id_client.length> 0){
                finish()
            }
        }

    }
}