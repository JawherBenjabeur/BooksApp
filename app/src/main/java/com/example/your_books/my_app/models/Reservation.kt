package com.example.your_books.my_app.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Reservation(

        @SerializedName("id")
        @Expose
        var id: Int,

        @SerializedName("id_livre")
        @Expose
        var id_livre: Int,

        @SerializedName("id_client_receveur")
        @Expose
        var id_client_receveur: Int?,

        @SerializedName("id_client_demandeur")
        @Expose
        var id_client_demandeur: Int?,

        @SerializedName("etat")
        @Expose
        var etat: Int?


)