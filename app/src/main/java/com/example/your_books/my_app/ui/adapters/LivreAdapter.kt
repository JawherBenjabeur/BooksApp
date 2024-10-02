package com.example.your_books.my_app.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.your_books.R
import com.example.your_books.my_app.models.Livre
import com.squareup.picasso.Picasso

class LivreAdapter(val context: Context, var listlivre: List<Livre> ):BaseAdapter() {

    private val inflater : LayoutInflater =context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return listlivre.size
    }

    override fun getItem(position: Int): Any {

        return listlivre[position]
    }

    override fun getItemId(position: Int): Long {

       return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?) :View{

        val view: View
        val vh: ItemHolder

        if (convertView == null) {

            view = inflater.inflate(R.layout.item_livre,parent,false)
            vh = ItemHolder(view)
            view?.tag = vh

        }else{

            view = convertView
            vh= view.tag as ItemHolder
        }
        vh.labelauteur.text = "Auteur : "+listlivre.get(position).auteur
        vh.labeltitre.text = listlivre.get(position).titre

        val url = context.getString(R.string.ipAdresse)+listlivre.get(position).photo_livre

        Picasso.with(context)
            .load(url)
            .into(vh.imagelivre, object: com.squareup.picasso.Callback {
                override fun onSuccess() {
                    //set animations here
                }
                override fun onError() {
                }
            })
        return view

    }
    class ItemHolder(row:View?) {
        val labelauteur: TextView
        val labeltitre:TextView
        val imagelivre: ImageView
        init {
            labelauteur = row?.findViewById(R.id.auteurview) as TextView
            labeltitre=row?.findViewById(R.id.titreview) as TextView
            imagelivre = row?.findViewById(R.id.imagebook) as ImageView
        }
    }

}