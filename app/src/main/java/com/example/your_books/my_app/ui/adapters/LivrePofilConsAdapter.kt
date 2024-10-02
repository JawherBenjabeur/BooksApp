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

class LivrePofilConsAdapter(val context: Context, var listlivre: List<Livre> ): BaseAdapter() {
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

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view1 :View
        val viewholder: ItemHolder

        if (convertView==null){

            view1 = inflater.inflate(R.layout.item_livreofprofil,parent,false)
            viewholder =ItemHolder(view1)
            view1?.tag=viewholder
        }else{
            view1=convertView
            viewholder=view1.tag as ItemHolder
        }

        viewholder.labelauteur1.text= "Auteur:"+listlivre.get(position).auteur
        viewholder.labeltitre1.text=listlivre.get(position).titre

        val url = context.getString(R.string.ipAdresse)+listlivre.get(position).photo_livre

        Picasso.with(context)
                .load(url)
                .into(viewholder.imagelivre1,object: com.squareup.picasso.Callback{
                    override fun onSuccess() {
                        //set animation here
                    }

                    override fun onError() {

                    }
                })
        return view1
    }

    class ItemHolder(row:View?){
        val labelauteur1:TextView
        val labeltitre1:TextView
        val imagelivre1:ImageView
        init {
            labelauteur1=row?.findViewById(R.id.auteurviewbook) as TextView
            labeltitre1 = row?.findViewById(R.id.titreviewbook) as TextView
            imagelivre1 = row?.findViewById(R.id.photobook) as ImageView
        }
    }
}