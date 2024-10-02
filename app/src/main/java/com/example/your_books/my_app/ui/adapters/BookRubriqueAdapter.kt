package com.example.your_books.my_app.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.your_books.R
import com.example.your_books.my_app.models.Livre
import com.squareup.picasso.Picasso

data class BookRubriqueAdapter(val context: Context, var listrubrique:List<Livre>, val mOnClickListener : MyOnClickListener): RecyclerView.Adapter<BookRubriqueAdapter.ViewHolder>() {

    class ViewHolder(item_livre: View): RecyclerView.ViewHolder(item_livre) {

        val labeltitre: TextView =item_livre.findViewById(R.id.titreview)
        val labelauteur: TextView =item_livre.findViewById(R.id.auteurview)
        val imagelivre: ImageView =item_livre.findViewById(R.id.imagebook)

    }

    interface MyOnClickListener {

        fun onItemClick(livre:Livre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.item_livre,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.labeltitre.text= listrubrique[position].titre
        holder.labelauteur.text="Auteur:" + listrubrique[position].auteur

        var url =context.getString(R.string.ipAdresse)+listrubrique.get(position).photo_livre
        Picasso.with(context)
                .load(url).into(holder.imagelivre,object: com.squareup.picasso.Callback{
                    override fun onSuccess() {
                        //set animation here
                    }

                    override fun onError() {
                    }
                })

        holder.itemView.setOnClickListener {
            mOnClickListener.onItemClick(listrubrique[position])
        }
    }

    override fun getItemCount(): Int {
        return listrubrique.size
    }
}

