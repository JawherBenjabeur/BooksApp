package com.example.your_books.my_app.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.your_books.R
import com.example.your_books.my_app.models.SharedPreferences
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profil.*

class ProfilFragment:Fragment(),View.OnClickListener {

    lateinit var preferences : SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profil,container,false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_logout.setOnClickListener(this)
        btn_edit.setOnClickListener (this)
        btn_Edit_password.setOnClickListener(this)

        preferences = SharedPreferences(requireContext())
        setpreferences()

    }
    override fun onClick(v: View) {

        when(v.id){
            R.id.btn_logout -> {
                Log.e("message","message")
                preferences.logout()
                requireActivity().finish()

            }

            R.id.btn_edit ->{

                Log.e("message","editProfiiiillll")
                findNavController().navigate(ProfilFragmentDirections.actionProfilFragmentToEditFragment())

            }

            R.id.btn_Edit_password -> {

                Log.e("message", "message")
                findNavController().navigate(ProfilFragmentDirections.actionProfilFragmentToEditPasswordFragment4())

            }
        }
    }

    fun setpreferences(){

       val user = preferences.getloginShared()

       firstname.setText(user.prenom)
       lastname.setText(user.nom)
       city.setText(user.ville)
       birth.setText(user.datedenaissance)
       number.setText(user.numerotelephone)
       mail.setText(user.email)

        val url = getString(R.string.ipAdresse)+user.photo_client
        Log.e("adresssss","adresss=$url")

        Picasso.with(context)
                .load(url)
                .into(photo_clientt, object: com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        //set animations here
                    }
                    override fun onError() {
                    }
                })

   }

}


