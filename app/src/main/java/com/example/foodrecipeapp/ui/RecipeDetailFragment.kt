package com.example.foodrecipeapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.foodrecipeapp.R
import com.example.foodrecipeapp.data.RecipeViewModel
import com.example.foodrecipeapp.databinding.FragmentRecipeDetailBinding
import com.example.foodrecipeapp.utils.cropToBlob
import com.example.foodrecipeapp.utils.setImageBlob
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.DecimalFormat

class RecipeDetailFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentRecipeDetailBinding
    private val nav by lazy { findNavController() }
    private val vm: RecipeViewModel by activityViewModels()

    private val id by lazy { arguments?.getString("id", "") ?: "" }
    private val formatter = DecimalFormat("0.00")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)

        //binding.btnClose.setOnClickListener { dismiss() }

        // -----------------------------------------------------------------------------------------

        // TODO(31): Load a specific recipe
        val recipe = vm.get(id)!!

        binding.txtName.text  = recipe.name
        binding.txtIngredients.text  = recipe.ingredients
        binding.txtSteps.text  = recipe.steps


        // TODO(32): Display [category.name]
        //binding.txtCategory.text = product.category.name
        binding.imgPhoto.setImageBlob(recipe.photo)
        // -----------------------------------------------------------------------------------------

        binding.btnEditRecipe.setOnClickListener { nav.navigate(R.id.recipeEditFragment) }

        return binding.root
    }
}