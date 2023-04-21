package com.example.foodrecipeapp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.foodrecipeapp.data.Category
import com.example.foodrecipeapp.data.CategoryViewModel
import com.example.foodrecipeapp.data.Recipe
import com.example.foodrecipeapp.data.RecipeViewModel
import com.example.foodrecipeapp.databinding.FragmentAddRecipeBinding
import com.example.foodrecipeapp.utils.cropToBlob
import com.example.foodrecipeapp.utils.errorDialog
import kotlinx.coroutines.launch

class AddRecipeFragment : Fragment() {
    private lateinit var binding: FragmentAddRecipeBinding
    private val nav by lazy { findNavController() }
    private val vm: RecipeViewModel by activityViewModels()
    private val cvm: CategoryViewModel by activityViewModels()


    // TODO: Get content launcher
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK) {
            binding.imgPhoto.setImageURI(it.data?.data)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAddRecipeBinding.inflate(inflater, container, false)

        reset()
        binding.imgPhoto.setOnClickListener  { select() }
        binding.btnReset.setOnClickListener  { reset()  }
        binding.btnSubmit.setOnClickListener { submit() }

        // -----------------------------------------------------------------------------------------

        val arrayAdapter = ArrayAdapter<Category>(requireContext(), android.R.layout.simple_spinner_item)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnGender.adapter = arrayAdapter


        // TODO(18): Load categories data into spinner -> launch block
        lifecycleScope.launch {
            val categories = vm.getCategories()
            arrayAdapter.addAll(categories)
        }

        // -----------------------------------------------------------------------------------------

        return binding.root
    }

    private fun select() {
        // TODO: Select file
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }

    private fun reset() {
        binding.edtId.text.clear()
        binding.edtName.text.clear()
        binding.edtIngred.text.clear()
        binding.edtSteps.text.clear()
        binding.spnGender.setSelection(0)
        binding.imgPhoto.setImageDrawable(null)
        binding.edtId.requestFocus()
    }

    private fun submit() {
        val p = Recipe(
            id   = binding.edtId.text.toString().trim(),
            name = binding.edtName.text.toString().trim(),
            categoryId = binding.spnGender.selectedItem.toString(),
            ingredients = binding.edtIngred.text.toString().trim(),
            steps = binding.edtSteps.text.toString().trim(),
            // TODO: Photo
            photo = binding.imgPhoto.cropToBlob(300, 300)
        )

        val err = vm.validate(p)
        if (err != "") {
            errorDialog(err)
            return
        }

        Toast.makeText(requireActivity(), "Recipe Added Successfully", Toast.LENGTH_SHORT).show()

        // TODO: Set (insert)
        vm.set(p)

        nav.navigateUp()
    }
}