package com.example.your_books.my_app.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.your_books.R
import com.example.your_books.my_app.models.*
import com.example.your_books.my_app.ui.adapters.LivrePofilConsAdapter
import com.example.your_books.my_app.viewmodels.LivreVM
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profilbook.*

class ProfilOfBookFragment: Fragment(),AdapterView.OnItemClickListener {

    lateinit var livreVm: LivreVM
    lateinit var listbook : List<Livre>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profilbook,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().run {
            livreVm = ViewModelProvider(this).get(LivreVM::class.java)
        }

        initobservables()

        livreVm.getlivre(livreVm.selectedBook!!.id_client)

        livreVm.userselect(livreVm.selectedBook!!.id_client)
        Log.e("messs","messs=${livreVm.selectedBook!!.id_client}")

        gridViewcAutreProfil.setOnItemClickListener(this)

    }

    fun initobservables(){

        livreVm.servicesLiveDataa.observe(viewLifecycleOwner,{
            if (it!=null){

                when(it){
                    is WebServiceResultLoginSuccess ->{

                        Log.e("user","user=${livreVm.selectedBook!!}")

                        prenomprofil.setText(it.user.prenom)
                        nomprofil.setText(it.user.nom)
                        villeprofil.setText(it.user.ville)
                        numeroprofil.setText(it.user.numerotelephone)

                        val url = getString(R.string.ipAdresse)+it.user.photo_client
                        Log.e("adresssss","adresssphotoselect=$url")

                        Picasso.with(context)
                                .load(url)
                                .into(imageprofilselect, object: com.squareup.picasso.Callback {
                                    override fun onSuccess() {
                                        //set animations here
                                    }
                                    override fun onError() {
                                    }
                                })
                    }
                    is WebServiceResultFailure ->{
                        Log.e("user","user=null")
                    }
                }
            }
        })

        livreVm.getLivreResponse.observe(viewLifecycleOwner,{
            if (it !=null){
                when(it){
                    is WebServiceResultBook ->{
                        listbook=it.livre
                        val adapter= LivrePofilConsAdapter(requireContext(),listbook)
                        gridViewcAutreProfil.adapter = adapter
                    }
                    is WebServiceResultFailureBook ->{
                        Log.e("message","message=${it.message}")
                    }
                }
            }
        })

    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        livreVm.selectedBook = listbook.get(position)

        Log.e("livreeeeeeeee", "livre=${livreVm.selectedBook}")
        findNavController().navigate(ProfilOfBookFragmentDirections.actionConsultAutreProfilToConsultAutreBook())
    }
}