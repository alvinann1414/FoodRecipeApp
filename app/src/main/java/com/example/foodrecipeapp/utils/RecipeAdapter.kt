package com.example.foodrecipeapp.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrecipeapp.data.Recipe
import com.example.foodrecipeapp.databinding.ItemRecipelistBinding
import java.text.DecimalFormat

class RecipeAdapter (
    val fn: (ViewHolder, Recipe) -> Unit = { _, _ -> }
) : ListAdapter<Recipe, RecipeAdapter.ViewHolder>(DiffCallback) {

    private val formatter = DecimalFormat("0.00")

    companion object DiffCallback : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(a: Recipe, b: Recipe)    = a.id == b.id
        override fun areContentsTheSame(a: Recipe, b: Recipe) = a == b
    }


    class ViewHolder(val binding: ItemRecipelistBinding) : RecyclerView.ViewHolder(binding.root)
    //class ViewHolder2(val binding2: ItemProlistupdateBinding) : RecyclerView.ViewHolder(binding2.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecipelistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = getItem(position)

        //holder.binding.txtId.text    = recipe.id
        holder.binding.txtName.text  = recipe.name

        // TODO(13): Display [category.name]
        //holder.binding.txtCategory.text = recipe.category.name

        // TODO: Load photo blob (use extension method)
        holder.binding.imgPhoto.setImageBlob(recipe.photo)

        fn(holder, recipe)
    }
}