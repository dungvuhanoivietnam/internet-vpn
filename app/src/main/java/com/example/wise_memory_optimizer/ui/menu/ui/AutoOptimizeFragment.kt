package com.example.wise_memory_optimizer.ui.menu.ui

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.custom.SeekBarCompatCustomer
import com.example.wise_memory_optimizer.custom.SwitchCompatCustomer
import com.example.wise_memory_optimizer.custom.ToolbarCustomer
import com.example.wise_memory_optimizer.databinding.FragmentAutoOptimizeBinding
import com.example.wise_memory_optimizer.ui.battery.PreferenceUtil
import com.example.wise_memory_optimizer.ui.menu.dialog.DialogAutomaticOptimizationMode

class AutoOptimizeFragment : Fragment(R.layout.fragment_auto_optimize) {
    private var _binding: FragmentAutoOptimizeBinding? = null

    private val binding get() = _binding!!
    private var width: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAutoOptimizeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)

        val widthView: Int = displayMetrics.widthPixels
        width = widthView / 10 * 8

        loadSetting(binding.switchCpu, PreferenceUtil.SETTING_CPU)
        loadSetting(binding.switchRam, PreferenceUtil.SETTING_RAM)
        loadSetting(binding.switchPin, PreferenceUtil.SETTING_PIN)
        loadProgressSetting(binding.seekbarRam, PreferenceUtil.PROGRESS_RAM)
        loadProgressSetting(binding.seekbarCpu, PreferenceUtil.PROGRESS_CPU)
        loadProgressSetting(binding.seekbarPin, PreferenceUtil.PROGRESS_PIN)

        binding.toolbar.setListener(object : ToolbarCustomer.Listener {
            override fun onClick() {
                findNavController().popBackStack()
            }
        })

        binding.switchRam.setListener(object : SwitchCompatCustomer.Listener {
            override fun onClick() {

                val title: String = if (!binding.switchRam.getChecked()) "Automatic optimization mode has been Turned Off" else "Automatic optimization mode has been Activated"

                DialogAutomaticOptimizationMode(requireContext(), width, title).show()
            }
        })

        binding.switchCpu.setListener(object : SwitchCompatCustomer.Listener {
            override fun onClick() {
                val title: String = if (!binding.switchCpu.getChecked()) "Automatic optimization mode has been Turned Off" else "Automatic optimization mode has been Activated"
                DialogAutomaticOptimizationMode(requireContext(), width, title).show()
            }
        })

        binding.switchPin.setListener(object : SwitchCompatCustomer.Listener {
            override fun onClick() {

                val title: String = if (!binding.switchPin.getChecked()) "Automatic optimization mode has been Turned Off" else "Automatic optimization mode has been Activated"
                DialogAutomaticOptimizationMode(requireContext(), width, title).show()
            }
        })

        binding.tvSave.setOnClickListener {
            saveAll()
            findNavController().popBackStack()
        }
    }

    private fun saveAll(){
        // save ram
        PreferenceUtil.saveBoolean(
            requireContext(),
            PreferenceUtil.SETTING_RAM,
            binding.switchRam.getChecked()
        )
        val progress = if (!PreferenceUtil.getBoolean(
                requireContext(),
                PreferenceUtil.SETTING_RAM,
                false)) 0 else binding.seekbarRam.getProgress()

        // save cpu
        PreferenceUtil.saveBoolean(
            requireContext(),
            PreferenceUtil.SETTING_CPU,
            binding.switchCpu.getChecked()
        )
        val progressCPU = if (!PreferenceUtil.getBoolean(
                requireContext(),
                PreferenceUtil.SETTING_CPU,
                false)) 0 else binding.seekbarCpu.getProgress()

        // save pin
        PreferenceUtil.saveBoolean(
            requireContext(),
            PreferenceUtil.SETTING_PIN,
            binding.switchPin.getChecked()
        )
        val progressPIN = if (!PreferenceUtil.getBoolean(
                requireContext(),
                PreferenceUtil.SETTING_PIN,
                false)) 0 else binding.seekbarPin.getProgress()

        saveProgress(progress,PreferenceUtil.PROGRESS_RAM)
        saveProgress(progressCPU,PreferenceUtil.PROGRESS_CPU)
        saveProgress(progressPIN,PreferenceUtil.PROGRESS_PIN)
    }

    private fun loadSetting(switchCompatCustomer: SwitchCompatCustomer, key: String) {
        if (PreferenceUtil.getBoolean(requireContext(), key, false)) {
            switchCompatCustomer.checked(true)
        } else {
            switchCompatCustomer.checked(false)
        }
    }

    private fun saveProgress(process: Int, key: String) {
        PreferenceUtil.saveInt(requireContext(), key, process)
    }

    fun loadProgressSetting(seekbar: SeekBarCompatCustomer, key: String) {
        seekbar.setProgress(PreferenceUtil.getInt(requireContext(), key))
    }

}