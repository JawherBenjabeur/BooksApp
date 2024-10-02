package com.example.your_books.my_app.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.your_books.R
import com.example.your_books.my_app.models.Categorie

class CategorieAdapter(val context: Context, var listcategorie:List<Categorie>) : BaseAdapter  () {

    private val inflater : LayoutInflater=context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {

        return listcategorie.size
    }

    override fun getItem(position: Int): Any {

        return listcategorie[position]
    }

    override fun getItemId(position: Int): Long {

        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        val vh: ItemHolder

        if (convertView == null) {

            view = inflater.inflate(R.layout.custom_spinner_item,parent,false)
            vh = ItemHolder(view)
            view?.tag = vh

        }else{

            view = convertView
            vh= view.tag as ItemHolder

        }

        vh.label.text = listcategorie.get(position).nom_categorie

        return view
    }

    class ItemHolder(row:View?) {
    val label: TextView
    init { label = row?.findViewById(R.id.textspinner) as TextView }
    }
}
