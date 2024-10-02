package com.example.your_books.my_app.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.your_books.R
import com.example.your_books.my_app.models.*
import com.example.your_books.my_app.ui.adapters.BookRubriqueAdapter
import com.example.your_books.my_app.viewmodels.LivreVM
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_rubrique.view.*

class HomeFragment:Fragment(), BookRubriqueAdapter.MyOnClickListener {

    lateinit var livreVm: LivreVM
    var listrubrique : ArrayList<ListRubrique> = ArrayList()
    lateinit var listbook: List<Livre>
    var alllivres : ArrayList<Livre> = ArrayList()

    lateinit var preferences : SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().run {
            livreVm = ViewModelProvider(this).get(LivreVM::class.java)
        }
        preferences = SharedPreferences(requireContext())


        livreVm.getrubrique()



        initobservables()

        livreVm.getlivre(preferences.getloginShared().id_client)
    }

    fun initobservables(){

        livreVm.selectRubriqueResponse.observe(viewLifecycleOwner,{
            if (it!=null){
                when(it){
                    is WebServiceResultRubriques ->{
                        //all id_rubrique in one variable :allbookrubrique
                        alllivres.clear()
                        alllivres.addAll(it.rubriques)

                        livreVm.listrubrique()

                    }
                }
            }
        })

        livreVm.listRubriqueResponse.observe(viewLifecycleOwner,{
            if (it!=null){
                when(it){
                    is WebServiceResultListRubriques ->{
                        Log.e("listeRubrique","ListeRubbbbb=${it.listrubrique}")
                        listrubrique.clear()
                        list_rubrique.removeAllViews()

                        listrubrique.addAll(it.listrubrique)
                        val inflater: LayoutInflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)as LayoutInflater

                        for(i in 0..listrubrique.size-1){

                            val view = inflater.inflate(R.layout.item_rubrique,null,false)

                            view.titrerubrique.setText(listrubrique[i].titre)

                            val list1 = ArrayList<Livre>()

                            for (j in 0..alllivres.size-1){

                                if(listrubrique[i].id_rubrique==alllivres[j].id_rubrique){
                                    list1.add(alllivres[j])
                                }
                            }

                            val adapter = BookRubriqueAdapter(requireContext(),list1,this)
                            view.RecycelRubrique.adapter = adapter
                            adapter.notifyDataSetChanged()

                            list_rubrique.addView(view)

                        }

                    }
                    is WebServiceResultFailurListRubriques->{

                    }
                }
            }
        })

    }

    override fun onItemClick(livre: Livre) {

        livreVm.selectedBook = livre

        Log.e("livreeeeeeeee","livre=$livre")

        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToConsultAutreBook())

    }

}