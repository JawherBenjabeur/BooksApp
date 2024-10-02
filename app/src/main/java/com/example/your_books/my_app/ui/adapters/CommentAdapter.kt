package com.example.your_books.my_app.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.your_books.R
import com.example.your_books.my_app.models.CommentSelect
import com.squareup.picasso.Picasso

class CommentAdapter(val context: Context,var listcomment:List<CommentSelect>): RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    class ViewHolder(item_comment:View):RecyclerView.ViewHolder(item_comment) {

        val labelcommentaire:TextView =item_comment.findViewById(R.id.commentajouter)
        val labelclient_name:TextView =item_comment.findViewById(R.id.client_name)
        val imageclient:ImageView=item_comment.findViewById(R.id.imageclientcomment)

        init {
            item_comment.setOnClickListener { v:View ->
                val position:Int=adapterPosition
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.item_comment,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.labelclient_name.text= listcomment[position].prenom+""+listcomment[position].nom
        holder.labelcommentaire.text= listcomment[position].commentaire

        var url =context.getString(R.string.ipAdresse)+listcomment.get(position).photo_client
        Picasso.with(context)
                .load(url).into(holder.imageclient,object: com.squareup.picasso.Callback{
                    override fun onSuccess() {
                        //set animation here
                    }

                    override fun onError() {
                    }
                })
    }

    override fun getItemCount(): Int {
        return listcomment.size
    }
}

