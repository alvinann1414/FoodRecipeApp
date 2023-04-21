package com.example.foodrecipeapp.data

import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class Admin (
    @DocumentId
    var id      : String = "",
    var email   : String = "",
    var password: String = "",
    var name    : String = "",
    var photo   : Blob   = Blob.fromBytes(ByteArray(0)),
)


data class Category(
    @DocumentId
    var id: String = "",
    var name: String = "",
) {
    // TODO(1): Additional field: [count] and [toString]
    @get:Exclude
    var count: Int = 0
    override fun toString() = name //Spinner "$id - $name" = L - Local
}


// TODO: Specify document id
// TODO: Add photo and date
data class Recipe(
    @DocumentId
    var id   : String = "",
    var name : String = "",
    var categoryId  : String = "",
    var ingredients  : String = "",
    var steps : String ="",
    var photo : Blob = Blob.fromBytes(ByteArray(0)),
){
    // TODO(2): Additional field: [category]
    @get:Exclude
    var category: Category = Category()
}

// -------------------------------------------------------------------------------------------------

// TODO(3): Shortcuts to Firestore collections [categories] and [fruits]
val ADMIN = Firebase.firestore.collection("Admin")
val CATEGORIES = Firebase.firestore.collection("categories")
val RECIPES = Firebase.firestore.collection("recipes")
val INGREDIENTS = Firebase.firestore.collection("ingredients")
val STEPS = Firebase.firestore.collection("steps")


// -------------------------------------------------------------------------------------------------

