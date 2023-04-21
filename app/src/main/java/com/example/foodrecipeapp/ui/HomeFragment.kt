package com.example.foodrecipeapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.foodrecipeapp.data.AuthViewModel
import com.example.foodrecipeapp.databinding.FragmentHomeBinding
import com.example.foodrecipeapp.R

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val nav by lazy { findNavController() }
    private val auth: AuthViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        //binding.btnProduct.setOnClickListener { nav.navigate(R.id.productHome) }
        //binding.btnCustomer.setOnClickListener { nav.navigate(R.id.customerListFragment) }
        //binding.btnCharity.setOnClickListener { nav.navigate(R.id.salesAdminFragment) }

//        if(auth.getUser() == null){
//            binding.btnProduct.visibility = View.INVISIBLE
//            binding.btnCustomer.visibility = View.INVISIBLE
//            //binding.btnCharity.visibility = View.INVISIBLE
//        }


        return binding.root
    }
}