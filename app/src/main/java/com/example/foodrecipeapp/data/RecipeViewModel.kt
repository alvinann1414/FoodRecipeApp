package com.example.foodrecipeapp.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RecipeViewModel : ViewModel() {

    private var recipes = listOf<Recipe>() // Original data
    private val result = MutableLiveData<List<Recipe>>() // Search + filter + sort result
    // TODO: Initialization
    private val col = Firebase.firestore.collection("recipes")

    private var name = ""       // Search
    private var categoryId = "" // Filter
    private var field = ""      // Sort
    private var reverse = false // Sort

    init {
        // TODO(14): Read all recipes -> launch block
        //           Populate [category] fielde
        viewModelScope.launch {
            val categories = CATEGORIES.get().await().toObjects<Category>() //Move inside snapshot listener if required

            RECIPES.addSnapshotListener { value, _ ->
                if (value == null) return@addSnapshotListener

                recipes = value.toObjects<Recipe>()

//                for(f in recipes) {
//                    f.category = categories.find { it.id == f.categoryId }!!  //?: Category() empty null
//                }

                updateResult()
            }
        }

    }

    // TODO(15): Get search + filter + sort result
    private fun updateResult() {
        var list = recipes

        // TODO(23): Search + filter
        list = list.filter {
            it.name.contains(name, true) &&
                    (categoryId == "" || categoryId == it.categoryId)
        }

        // TODO(24): Sort
        list = when (field) {
            "id"    -> list.sortedBy { it.id }
            "name"  -> list.sortedBy { it.name }
            "categoryId" -> list.sortedBy { it.categoryId }
            else    -> list
        }

        if(reverse) list = list.reversed()

        result.value = list
    }

    // ---------------------------------------------------------------------------------------------

    fun getResult() = result // Live data

    // TODO(17): Return all categories
    suspend fun getCategories(): List<Category> {
        return CATEGORIES.get().await().toObjects<Category>()
    }

    // TODO(21): Set [name] -> update result
    fun search(name: String) {
        this.name = name
        updateResult()
    }

    // TODO(22): Set [categoryId] -> update result
    fun filter(categoryId: String) {
        this.categoryId = categoryId
        updateResult()
    }

    // TODO(25): Set [field] and [reverse] -> update result
    fun sort(field: String): Boolean {
        reverse = if(this.field == field) !reverse else false

        this.field = field
        updateResult()
        return reverse
    }

    // ---------------------------------------------------------------------------------------------

    fun init() = Unit // void

    fun get(id: String) = result.value?.find { it.id == id }

    fun getAll() = result

    fun delete(id: String) {
        // TODO
        col.document(id).delete()
    }

    fun deleteAll() {
        // TODO
        result.value?.forEach { col.document(it.id).delete() }
    }

    fun set(f: Recipe) {
        // TODO
        col.document(f.id).set(f)
    }

    //----------------------------------------------------------------------------------------------

    private fun idExists(id: String)
            = result.value?.any { it.id == id } ?: false

    fun validate(p: Recipe, insert: Boolean = true): String {
        val regexId = Regex("""^[R]\d{3}$""")
        var e = ""

        if (insert) {
            e += if (p.id == "") "- Id is required.\n"
            else if (!p.id.matches(regexId)) "- Id format is invalid (format: R999).\n"
            else if (idExists(p.id)) "- Id is duplicated.\n"
            else ""
        }

        e += if (p.name == "") "- Name is required.\n"
        else if (p.name.length < 3) "- Name is too short (at least 3 letters).\n"
        else ""

//        e += if (p.categoryId == "") "- Category is required.\n"
//        else ""

        //Validates if ingredients is empty
        e += if (p.ingredients == "") "- Ingredients are required.\n"
        else ""

        //Validates if steps is empty
        e += if (p.steps == "") "- Ingredients are required.\n"
        else ""


        // TODO: Validate if photo is empty
        e += if (p.photo.toBytes().isEmpty()) "- Photo is required.\n"
        else ""

        return e
    }

}