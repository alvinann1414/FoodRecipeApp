package com.example.foodrecipeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.foodrecipeapp.databinding.FragmentRecipeHomeBinding
import com.example.foodrecipeapp.R



class RecipeHome : Fragment() {

    private lateinit var binding: FragmentRecipeHomeBinding
    private val nav by lazy { findNavController() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRecipeHomeBinding.inflate(inflater, container, false)

        binding.btnRecipeList.setOnClickListener { nav.navigate(R.id.viewRecipeFragment) }

        binding.btnAddRecipe.setOnClickListener { nav.navigate(R.id.addRecipeFragment) }

        binding.btnEditRecipe.setOnClickListener { nav.navigate(R.id.recipeListEditFragment) }

        //binding.btnCatList.setOnClickListener { nav.navigate(R.id.categoryFragment) }

        return binding.root
    }


}