package com.example.your_books.my_app.models

sealed class WebServiceResult

data class WebServiceResultData<T>(val status: Boolean, val message: String, val data: T?)

data class WebServiceResultLoginSuccess(val user: User):WebServiceResult()
data class WebServiceResultFailure(val message: String):WebServiceResult()

//********************* Web service catégorie qui contient liste des catégories********************
data class WebServiceResultCategorie(val categorie: List<Categorie>):WebServiceResult()
data class WebServiceResultFailureCategorie(val message: String):WebServiceResult()

//*********************
data class WebServiceResultLivre(val livre: Livre ):WebServiceResult()
data class WebServiceResultFailureLivre(val message: String):WebServiceResult()

//*******************
data class WebServiceResultBook(val livre: List<Livre> ):WebServiceResult()
data class WebServiceResultFailureBook(val message: String):WebServiceResult()

data class WebServiceResulDelete(val message: String, val status: Boolean):WebServiceResult()
data class WebServiceResultFailureDelete(val message: String):WebServiceResult()

//********************* Web service POUR AJOUTER UN COMMENTAIRE ********************
data class WebServiceResultComment(val message: String):WebServiceResult()
data class WebServiceResultFailureComment(val message: String):WebServiceResult()

//********************* Web service commentaire qui contient liste commentaire ********************
data class WebServiceResultCommentaire(val commentaire: List<CommentSelect>):WebServiceResult()
data class WebServiceResultFailureCommentaire(val message: String):WebServiceResult()

//********************* Web service likes qui contient liste des likes ********************
data class WebServiceResultLikes(val likes: List<Likes>):WebServiceResult()
data class WebServiceResultFailurLikes(val message: String):WebServiceResult()

data class WebServiceResultRubriques(val rubriques: List<Livre>):WebServiceResult()
data class WebServiceResultFailurRubriques(val message: String):WebServiceResult()

data class WebServiceResultListRubriques(val listrubrique: List<ListRubrique>):WebServiceResult()
data class WebServiceResultFailurListRubriques(val message: String):WebServiceResult()

data class WebServiceResultLikesUpdate(val like: List<Likes>):WebServiceResult()
data class WebServiceResultFailurLikesUpdates(val message: String):WebServiceResult()

data class WebServiceResultLikesInsert(val like: List<Likes>):WebServiceResult()
data class WebServiceResultFailurLikesInsert(val message: String):WebServiceResult()

data class WebServiceResultRes(val livres: ArrayList<LivreReservation>):WebServiceResult()
data class WebServiceResultFailureRes(val message: String):WebServiceResult()
data class WebServiceResultResInsert(val res: String):WebServiceResult()


