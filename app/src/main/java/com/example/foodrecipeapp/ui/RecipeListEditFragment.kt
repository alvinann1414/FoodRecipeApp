package com.example.foodrecipeapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.foodrecipeapp.R
import com.example.foodrecipeapp.data.RecipeViewModel
import com.example.foodrecipeapp.databinding.FragmentRecipeListEditBinding
import com.example.foodrecipeapp.utils.RecipeAdapter


class RecipeListEditFragment : Fragment() {
    private lateinit var binding: FragmentRecipeListEditBinding
    private val nav by lazy { findNavController() }
    private val vm: RecipeViewModel by activityViewModels()

    private lateinit var adapter: RecipeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentRecipeListEditBinding.inflate(inflater, container, false)

        binding.btnInsert.setOnClickListener { nav.navigate(R.id.addRecipeFragment) }
        binding.btnDeleteAll.setOnClickListener { deleteAll() }

        adapter = RecipeAdapter { holder, friend ->
            // Item click -> navigate to UpdateFragment (id)
            holder.binding.root.setOnClickListener {
                nav.navigate(R.id.recipeEditFragment, bundleOf("id" to friend.id))
            }
            // Delete button click -> delete record
            //older.binding.btnDelete.setOnClickListener {
            //    delete(friend.id)
            //}
        }
        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        // TODO: Get all
        vm.getAll().observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.txtCount.text = "${it.size} record(s)"
        }

        return binding.root
    }

    private fun deleteAll() {
        // TODO: Delete all
        vm.deleteAll()
    }

    private fun delete(id: String) {
        // TODO: Delete
        vm.delete(id)
    }
}