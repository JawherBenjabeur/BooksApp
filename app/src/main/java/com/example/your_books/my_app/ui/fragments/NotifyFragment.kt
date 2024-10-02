package com.example.your_books.my_app.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.your_books.R
import com.example.your_books.my_app.models.LivreReservation
import com.example.your_books.my_app.models.SharedPreferences
import com.example.your_books.my_app.models.WebServiceResultFailureRes
import com.example.your_books.my_app.models.WebServiceResultRes
import com.example.your_books.my_app.ui.adapters.ReservationAdapter
import com.example.your_books.my_app.viewmodels.LivreVM
import kotlinx.android.synthetic.main.fragment_notify.*

class NotifyFragment: Fragment(),ReservationAdapter.ReservationAdapterListener{

    lateinit var viewModel: LivreVM
    val reservations = ArrayList<LivreReservation>()
    private lateinit var reservationAdapter: RecyclerView.Adapter<*>

    lateinit var preferences : SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notify,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = SharedPreferences(requireContext())
        requireActivity().run { viewModel = ViewModelProvider(this).get(LivreVM::class.java) }
        viewModel.getallres(preferences.getloginShared().id_client.toInt())


        initObserver()

        reservationAdapter = ReservationAdapter( requireContext(), reservations, this)
        reservation_recyclerview.adapter = reservationAdapter
    }

    fun initObserver() {
        viewModel.getLivreResResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is WebServiceResultFailureRes -> {
                    Log.e("Failure",it.message)

                }
                is WebServiceResultRes -> {

                    val livres = it.livres as ArrayList<LivreReservation>
                    reservations.clear()
                    reservations.addAll(livres)

                    if(reservations.size>0){
                        txtnotify.visibility=View.GONE
                    }
                    reservationAdapter.notifyDataSetChanged()

                }
            }
        })
        viewModel.updateResResponse.observe(viewLifecycleOwner, Observer {

            if(it!=null){

                when (it) {
                    is WebServiceResultFailureRes -> {
                        Log.e("Failure",it.message)

                    }
                    is WebServiceResultRes -> {
                        Log.e("Success","success")
                        val livres = it.livres as ArrayList<LivreReservation>
                        reservations.clear()
                        reservations.addAll(livres)
                        reservationAdapter.notifyDataSetChanged()

                    }
                }
            }
        })

    }

    override fun OnReservationClickListener(livre: LivreReservation, position: Int, etat: String) {
        if(etat == "accepter") {
            viewModel.updateRes(livre.id, 2, preferences.getloginShared().id_client.toInt())
        }
        else {
            viewModel.updateRes(livre.id, 3, preferences.getloginShared().id_client.toInt())
        }
    }



}