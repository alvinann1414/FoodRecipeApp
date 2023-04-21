package com.example.foodrecipeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodrecipeapp.R
import com.example.foodrecipeapp.data.Category
import com.example.foodrecipeapp.data.RecipeViewModel
import com.example.foodrecipeapp.databinding.FragmentViewRecipeBinding
import com.example.foodrecipeapp.utils.RecipeAdapter
import kotlinx.coroutines.launch

class ViewRecipeFragment : Fragment() {

    private lateinit var binding: FragmentViewRecipeBinding
    private val nav by lazy { findNavController() }
    private val vm: RecipeViewModel by activityViewModels()

    private lateinit var adapter: RecipeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentViewRecipeBinding.inflate(inflater, container, false)

        // -----------------------------------------------------------------------------------------

        // TODO(29): Default search, filter and sort
        vm.search("")
        vm.filter("")
        sort("id")

        // -----------------------------------------------------------------------------------------

        adapter = RecipeAdapter(){ holder, recipe ->
            // Item click -> navigate to DetailsFragment (id)
            holder.binding.root.setOnClickListener {
                nav.navigate(R.id.recipeDetailFragment, bundleOf("id" to recipe.id))
            }
        }



        val layoutManager = GridLayoutManager(context, 2) // 2 columns
        binding.rv.layoutManager = layoutManager
        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))

        // TODO(16): Load product data into recycler view
        vm.getResult().observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.txtCount.text = "${it.size} Record(s)"
        }

        // -----------------------------------------------------------------------------------------


        val arrayAdapter = ArrayAdapter<Category>(requireContext(), android.R.layout.simple_spinner_item)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spn.adapter = arrayAdapter

        // TODO(18): Load categories data into spinner -> launch block
        lifecycleScope.launch {
            val categories = vm.getCategories()
            arrayAdapter.add(Category("", "All"))
            arrayAdapter.addAll(categories)
        }

        // -----------------------------------------------------------------------------------------

        binding.sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(name: String) = true
            override fun onQueryTextChange(name: String): Boolean {
                // TODO(19): Search by [name] -> vm.search(...)
                vm.search(name)

                return true
            }
        })

        // -----------------------------------------------------------------------------------------

        binding.spn.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // TODO(20): Filter by [categoryId] -> vm.filter(...)
                val category = binding.spn.selectedItem as Category
                vm.filter(category.id)
            }
        }

        // -----------------------------------------------------------------------------------------

        //binding.btnId.setOnClickListener { sort("id") }
        binding.btnName.setOnClickListener { sort("name") }
        //binding.btnPrice.setOnClickListener { sort("price") }
        binding.btnCategory.setOnClickListener { sort("categoryId") } //
        // ---------------------------------------------------------------------Bana--------------------

        return binding.root
    }

    private fun sort(field: String) {
        // TODO(26): Sort by [field] -> vm.sort(...)
        val reverse = vm.sort(field)

        // TODO(27): Remove icon -> all buttons
        //binding.btnId.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        binding.btnName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        //binding.btnPrice.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        binding.btnCategory.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        // TODO(28): Set icon -> specific button
        val res = if (reverse) R.drawable.ic_down else R.drawable.ic_up

        when (field) {
            //"id"    -> binding.btnId.setCompoundDrawablesWithIntrinsicBounds(0, 0, res, 0)
            "name"  -> binding.btnName.setCompoundDrawablesWithIntrinsicBounds(0, 0, res, 0)
            //"price" -> binding.btnPrice.setCompoundDrawablesWithIntrinsicBounds(0, 0, res, 0)
            "categoryId" -> binding.btnCategory.setCompoundDrawablesWithIntrinsicBounds(0, 0, res, 0)
        }
    }

}