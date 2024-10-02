package com.example.your_books.my_app.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.your_books.my_app.models.WebServiceResultFailureCategorie
import com.example.your_books.my_app.ui.adapters.BookAdaptersearch
import com.example.your_books.my_app.viewmodels.LivreVM
import kotlinx.android.synthetic.main.fragment_livres.gridViewSearch
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment:Fragment(), AdapterView.OnItemClickListener {

    lateinit var livreVm: LivreVM
    var listbook : ArrayList<Livre> = ArrayList()
    lateinit var preferences : SharedPreferences
    lateinit var adapter: BookAdaptersearch

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search,container,false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().run {
            livreVm = ViewModelProvider(this).get(LivreVM::class.java)
        }
        preferences = SharedPreferences(requireContext())

        initobservables()
        livreVm.selectCommentaire
        gridViewSearch.setOnItemClickListener(this)

        adapter = BookAdaptersearch(requireContext(),listbook)
        gridViewSearch.adapter = adapter

        livreVm.getallbook(search = "_")
        search.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                /////////
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (search.text.length==0){
                    livreVm.getallbook(search = "_")
                }else{
                    livreVm.getallbook(search = search.text.toString())
                }
                //livreVm.getallbook(search = searchtext.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                ////////////
            }
        })
    }


    fun initobservables(){
        livreVm.getBookResponse.observe(viewLifecycleOwner,{

            if (it != null){
                when(it){
                    is WebServiceResultBook ->{

                        Log.e("Livre","Livre=$it")

                        //vider la liste à chaque fois
                        listbook.clear()
                        listbook.addAll(it.livre)

                        adapter.notifyDataSetChanged()

                    }
                    is WebServiceResultFailureCategorie -> {
                        Log.e("message", "message=${it.message}")
                    }
                }
            }
        })

    }
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        livreVm.selectedBook = listbook.get(position)

        Log.e("livreeeeeeeee","livre=${livreVm.selectedBook}")

        findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToConsultAutreBook())
    }




}