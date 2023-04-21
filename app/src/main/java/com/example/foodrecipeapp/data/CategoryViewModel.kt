package com.example.foodrecipeapp.data

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.tasks.await

class CategoryViewModel : ViewModel() {

    // Dummy function
    fun init() = Unit

    // TODO(7): Return all categories
    //          Populate [count] field
    suspend fun getAll(): List<Category> {
        val categories = CATEGORIES
            .get()
            .await()
            .toObjects<Category>()

        for(c in categories){
            c.count = RECIPES
                .whereEqualTo("categoryId", c.id)
                .get()
                .await()
                .size()
        }

        return categories
    }

    // TODO(10): Return a specific category
    suspend fun get(id: String): Category? {
        return CATEGORIES
            .document(id)
            .get()
            .await()
            .toObject<Category>()
    }

    // TODO(): Return a specific category ID
    suspend fun getID(name: String): Category? {
        return CATEGORIES
            .document(name)
            .get()
            .await()
            .toObject<Category>()
    }

    // TODO(11): Return all fruits under a specific category
    //           Populate [category] field
    suspend fun getProducts(id: String): List<Recipe> {
        val recipe = RECIPES
            .whereEqualTo("categoryId", id)
            .get()
            .await()
            .toObjects<Recipe>()

        val category = get(id)
        for(f in recipe){
            f.category = category!! // give error if null
        }

        return recipe
    }

}