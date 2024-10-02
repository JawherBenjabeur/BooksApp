package com.example.your_books.my_app.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.your_books.R
import com.example.your_books.my_app.models.SharedPreferences
import com.example.your_books.my_app.models.WebServiceResultFailure
import com.example.your_books.my_app.models.WebServiceResultLoginSuccess
import com.example.your_books.my_app.viewmodels.UpdatepassVM
import kotlinx.android.synthetic.main.fragment_password.*


class EditPasswordFragment:Fragment(),View.OnClickListener{

    lateinit var preferences : SharedPreferences
    lateinit var updatepassVM: UpdatepassVM


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_password,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updatepassVM = ViewModelProvider(this).get(UpdatepassVM::class.java)
        preferences = SharedPreferences(requireContext())

        initObservables()
        btn_save_password.setOnClickListener(this)
    }

    fun initObservables(){
        updatepassVM.servicesLiveData.observe(viewLifecycleOwner,{
            Log.e("message","result=$it")

            if (it!=null) {

                when (it) {
                    is WebServiceResultLoginSuccess -> {

                        preferences.setpassword(it.user.motdepass.toString())

                        requireActivity().onBackPressed()
                        updatepassVM.servicesLiveData.value =null
                    }

                    is WebServiceResultFailure -> {
                        Log.e("message", "message = Erreur")
                        updatepassVM.servicesLiveData.value =null
                    }
                }
            }
        })
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_save_password ->{

                verifChampspass()
            }
        }
    }


    fun verifChampspass(){

        val user = preferences.getloginShared()
        val password = user.motdepass

        val mot_de_pass = password_before.text.toString()
        val new_mot_de_pass = password_new.text.toString()
        val conf_new_mot_de_pass = password_new_confir.text.toString()

        if(mot_de_pass.isEmpty()){
            password_before.setError("Merci de saisir le ancien mot de pass")
            return
        }

        if(new_mot_de_pass.isEmpty()){
            password_new.setError("Merci de saisir le nouveaux mot de pass")
            return
        }

        if (conf_new_mot_de_pass.isEmpty()){
            password_new_confir.setError("Merci de saisir votre nouveaux mot de pass")
            return
        }

        if (new_mot_de_pass!=conf_new_mot_de_pass){
            password_new_confir.setError("Merci de confirmer votre mot de pass")
            return
        }

       if (password!=mot_de_pass){
            password_before.setError("Merci de verifier votre mot de pass ancien")
            return
        }
        
        updatepassVM.userUpdatepass(
                id_client = preferences.getloginShared().id_client,

                password= conf_new_mot_de_pass

        )
        Log.e("aaaaaaaaaa","aaaaaaaa=$password")
    }


}