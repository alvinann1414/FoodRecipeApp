package com.example.foodrecipeapp.ui

import android.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.foodrecipeapp.data.*
import com.example.foodrecipeapp.databinding.FragmentRecipeEditBinding
import com.example.foodrecipeapp.utils.cropToBlob
import com.example.foodrecipeapp.utils.errorDialog
import com.example.foodrecipeapp.utils.setImageBlob
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class RecipeEditFragment : Fragment() {

    private lateinit var binding: FragmentRecipeEditBinding
    private val nav by lazy { findNavController() }
    private val vm: RecipeViewModel by activityViewModels()

    private val id by lazy { arguments?.getString("id") ?: "" }
    private val formatter = SimpleDateFormat("dd MMMM yyyy '-' hh:mm:ss a", Locale.getDefault())

    private val launcher = registerForActivityResult(StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.imgPhoto.setImageURI(it.data?.data)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentRecipeEditBinding.inflate(inflater, container, false)

        reset()
        binding.imgPhoto.setOnClickListener  { select() }
        binding.btnReset.setOnClickListener  { reset()  }
        binding.btnSubmit.setOnClickListener { submit() }
        binding.btnDelete.setOnClickListener { delete() }

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
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }

    private fun reset() {
        // TODO: Get
        val f = vm.get(id)

        if (f == null) {
            nav.navigateUp()
            return
        }

        binding.txtId.text = f.id
        binding.edtName.setText(f.name)
        binding.spnGender.setSelection(0)
        binding.edtIngred.setText(f.ingredients)
        binding.edtSteps.setText(f.steps)

        // TODO: Load photo and date
        binding.imgPhoto.setImageBlob(f.photo)

        binding.edtName.requestFocus()
    }

    private fun submit() {
        val f = Recipe(
            id   = binding.txtId.text.toString().trim(),
            name = binding.edtName.text.toString().trim(),
            //price  = binding.edtPrice.text.toString().toDoubleOrNull() ?: 0.00,
            //categoryId = (binding.spnGender.selectedItem.toString())[0].toString(),
            // TODO: Photo
            photo = binding.imgPhoto.cropToBlob(300, 300)
        )

        val err = vm.validate(f, false)  //no validation for id because this is update
        if (err != "") {
            errorDialog(err)
            return
        }

        // TODO: Set (update)
        vm.set(f)

        Toast.makeText(requireActivity(), "Edit Success", Toast.LENGTH_SHORT).show()

        nav.navigateUp()
    }

    private fun delete() {
        // TODO: Delete
        vm.delete(id)

        nav.navigateUp()
    }


}