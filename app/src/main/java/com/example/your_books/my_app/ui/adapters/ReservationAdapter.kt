package com.example.your_books.my_app.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.your_books.R
import com.example.your_books.my_app.models.LivreReservation
import kotlinx.android.synthetic.main.item_reservation.view.*

class  ReservationAdapter(val context: Context, val reservations : ArrayList<LivreReservation>, val reservationclickListener : ReservationAdapterListener) :
RecyclerView.Adapter<ReservationAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ReservationAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_reservation, parent, false)

        return ViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val hview = holder.itemView
        hview.livre_titre.text = reservations[position].titre

        var etat : String
        if (reservations[position].etat == 1){
            etat = "En cours"
        }
        else if (reservations[position].etat == 2){
            etat = "Réservé"
        }
        else {
            etat = "Réfusé"
        }
        hview.etat.text = etat
        hview.userName.text = reservations[position].nom+ " "+reservations[position].prenom
        hview.btnAccepter.setOnClickListener{reservationclickListener.OnReservationClickListener(reservations[position],position, "accepter")}
        hview.btnRefuser.setOnClickListener{reservationclickListener.OnReservationClickListener(reservations[position],position, "refusé")}

        if(reservations[position].etat == 2 || reservations[position].etat == 3){
            hview.btnAccepter.visibility = View.GONE
            hview.btnRefuser.visibility = View.GONE
        }





    }
    interface ReservationAdapterListener{
        fun OnReservationClickListener(livre : LivreReservation, position: Int, etat: String)
    }

    override fun getItemCount() = reservations.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {


    }

}