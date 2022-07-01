package com.example.wise_memory_optimizer.ui.menu.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.custom.ToolbarCustomer
import com.example.wise_memory_optimizer.databinding.FragmentLanguageBinding
import com.example.wise_memory_optimizer.databinding.FragmentPrivacyBinding

class PrivacyFragment : Fragment(R.layout.fragment_privacy) {
    private var _binding: FragmentPrivacyBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrivacyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setListener(object : ToolbarCustomer.Listener {
            override fun onClick() {
                findNavController().popBackStack()

            }
        })
    }
}