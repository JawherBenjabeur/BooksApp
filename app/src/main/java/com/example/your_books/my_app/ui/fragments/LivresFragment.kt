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
import com.example.your_books.my_app.models.Livre
import com.example.your_books.my_app.models.SharedPreferences
import com.example.your_books.my_app.models.WebServiceResultBook
import com.example.your_books.my_app.models.WebServiceResultFailureBook
import com.example.your_books.my_app.ui.adapters.LivreAdapter
import com.example.your_books.my_app.viewmodels.LivreVM
import kotlinx.android.synthetic.main.fragment_livres.*

class LivresFragment:Fragment (),View.OnClickListener,AdapterView.OnItemClickListener{

    lateinit var livreVm:LivreVM
    lateinit var listbook : List<Livre>
    lateinit var preferences : SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_livres,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().run {
            livreVm = ViewModelProvider(this).get(LivreVM::class.java)
        }
        preferences = SharedPreferences(requireContext())

        initobservables()
        livreVm.getlivre(preferences.getloginShared().id_client)

        btn_add_newlivre.setOnClickListener(this)

        gridViewSearch.setOnItemClickListener(this)

    }

    fun initobservables(){
        livreVm.getLivreResponse.observe(viewLifecycleOwner,{
            if (it != null){
                when(it){
                    is WebServiceResultBook ->{

                        Log.e("Livre","Livre=$it")
                        listbook = it.livre

                        val adapter = LivreAdapter(requireContext(),listbook)
                        gridViewSearch.adapter = adapter
                    }
                    is WebServiceResultFailureBook -> {
                        Log.e("message", "message=${it.message}")
                    }
                }
            }
        })
    }

    override fun onClick(v: View) {
        when(v.id){

            R.id.btn_add_newlivre ->{
                findNavController().navigate(LivresFragmentDirections.actionLivresFragmentToAddBookFragment())
            }

        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        livreVm.selectedBook = listbook.get(position)

        Log.e("livreeeeeeeee","livre=${livreVm.selectedBook}")

        findNavController().navigate(LivresFragmentDirections.actionLivresFragmentToConsultLivre())
    }




}