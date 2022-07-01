package com.example.wise_memory_optimizer.ui.done

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.wise_memory_optimizer.MainActivity
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.databinding.FragmentDoneOptimizerBinding
import com.example.wise_memory_optimizer.utils.NavigationUtils

class DoneFragment : Fragment() {

    val type by lazy {
        arguments?.getInt(TYPE_OPTIMIZE, OptimizeType.PIN.value)
    }

    companion object {
        const val TYPE_OPTIMIZE = "title"
    }

    lateinit var binding: FragmentDoneOptimizerBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDoneOptimizerBinding.inflate(inflater)
        binding.lottie.setOnClickListener(View.OnClickListener {
            val bundle = Bundle()
            NavigationUtils.navigate(binding.root, R.id.backtoHome, bundle)
        })
        onBackpress()
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackpress()
//        binding.lottie.setOnClickListener {
//            onBackpress()
//        }
    }

    private fun onBackpress() {
        (activity as? MainActivity)?.let {
            activity?.onBackPressedDispatcher?.addCallback(
                it,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        val bundle = Bundle()
                        NavigationUtils.navigate(binding.root, R.id.backtoHome, bundle)
                    }
                })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (type) {
            OptimizeType.RAM.value -> binding.title.text = getString(R.string.txt_option_ram)
            OptimizeType.PIN.value -> binding.title.text = getString(R.string.txt_option_pin)
            OptimizeType.CPU.value -> binding.title.text = getString(R.string.txt_option_cpu)
        }
    }

}

enum class OptimizeType(var value: Int) {
    RAM(0),
    PIN(1),
    CPU(2)
}