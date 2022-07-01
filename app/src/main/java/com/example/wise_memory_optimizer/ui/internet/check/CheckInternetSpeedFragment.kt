package com.example.wise_memory_optimizer.ui.internet.check

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.wise_memory_optimizer.MainViewModel
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.databinding.FragmentCheckInternetSpeedBinding
import com.example.wise_memory_optimizer.model.location_speed_test.LocationTestingModel
import com.example.wise_memory_optimizer.utils.NetWorkConnection
import com.example.wise_memory_optimizer.utils.formatMbps
import com.example.wise_memory_optimizer.utils.setTextPing
import com.example.wise_memory_optimizer.utils.toMbps
import fr.bmartel.speedtest.model.SpeedTestMode

class CheckInternetSpeedFragment : Fragment() {
    private val viewModel by lazy {
        ViewModelProvider(this).get(CheckInternetSpeedViewModel::class.java)
    }

    private val mainViewModel: MainViewModel by navGraphViewModels(R.id.mobile_navigation)

    private lateinit var binding: FragmentCheckInternetSpeedBinding

    private var locationTestingModel = LocationTestingModel()

    private val dialogNetwork by lazy {
        AlertDialog.Builder(requireContext()).apply {
            setMessage(getString(R.string.plesase_check_the_connection))
            setCancelable(true)

            setPositiveButton(
                getString(R.string.yes)
            ) { _, _ ->
                findNavController().popBackStack()
            }
        }.create()
    }

    companion object {
        var TAG = CheckInternetSpeedFragment::class.java.simpleName
        const val LOCATION_NAME = "location-name"
        const val PING_MIN_VALUE = 1F
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckInternetSpeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        setupUI()
        setupObserver()
        setupEvent()
        startTesting()
    }

    private fun initData() {
        arguments?.getString(LOCATION_NAME)?.let {
            locationTestingModel.roomName = it
            binding.txtLocation.text = locationTestingModel.roomName
        }
    }

    private fun setupUI() {
        setupSpeedView()
    }

    private fun setupObserver() {
        viewModel.reportTestDone.observe(viewLifecycleOwner) {
            when (it.speedTestMode) {
                SpeedTestMode.DOWNLOAD -> {
                    Log.e(TAG, "DOWNLOAD: ${it.transferRateOctet} - ${it.transferRateBit}")
                    val downloadMBS = it.transferRateBit.toMbps()
                    locationTestingModel.downloadPoint = downloadMBS
                    binding.txtDownload.text = downloadMBS.formatMbps()
                    binding.spvInternet.speedTo(0F)
                    viewModel.startTestUpload()
                }
                SpeedTestMode.UPLOAD -> {
                    Log.e(TAG, "UPLOAD: ${it.transferRateOctet} - ${it.transferRateBit}")
                    val uploadMBS = it.transferRateBit.toMbps()
                    locationTestingModel.uploadPoint = uploadMBS
                    binding.txtUpload.text = uploadMBS.formatMbps()
                    showResultTestSuccess()
                }
                else -> {
                    Log.d(TAG, "isTestDone ${it.speedTestMode}")
                }
            }
        }

        viewModel.pingValue.observe(viewLifecycleOwner) {
            if (it <= 0){
                locationTestingModel.ping = PING_MIN_VALUE
            }else{
                locationTestingModel.ping = it
            }

            locationTestingModel.ping?.let { ping ->
                binding.txtPing.setTextPing(ping)
                binding.txtPingResult.text = ping.toInt().toString()
            }
        }

        viewModel.speedDownloadReport.observe(viewLifecycleOwner) {
            binding.spvInternet.speedTo(it.transferRateBit.toMbps())
        }

        viewModel.speedUploadReport.observe(viewLifecycleOwner) {
            binding.spvInternet.speedTo(it.transferRateBit.toMbps())
        }

        viewModel.speedTestErrorMessage.observe(viewLifecycleOwner) {
            if (it.ordinal == 0) {
                showDialogWithErrorTesting(message = it?.message.orEmpty())
            } else {
                Log.e("=====> ", it?.message.orEmpty())
            }
        }

        NetWorkConnection(requireContext()).observe(
            viewLifecycleOwner
        ) { isConnection ->
            if (!isConnection) {
                dialogNetwork.show()
            } else {
                dialogNetwork.dismiss()
            }
        }

        viewModel.speedTestProgressPercent.observe(viewLifecycleOwner){

        }
    }

    /**
     *  Set up speed view
     * */
    private fun setupSpeedView() {
        binding.spvInternet.run {
            withTremble = true

            indicator.run {
                color = ContextCompat.getColor(requireContext(), R.color.gray_9E)
                width = resources.getDimension(com.intuit.sdp.R.dimen._6sdp)
            }
            centerCircleColor = ContextCompat.getColor(requireContext(), R.color.core_green)

            sections.run {
                get(0).width = resources.getDimension(com.intuit.sdp.R.dimen._12sdp)
                indicator
                get(1).width = resources.getDimension(com.intuit.sdp.R.dimen._12sdp)
                get(2).width = resources.getDimension(com.intuit.sdp.R.dimen._12sdp)
            }
        }
    }

    /**
     *  Show UI test success
     * */
    private fun showResultTestSuccess() {
        binding.run {
            btnDone.isEnabled = true
            spvInternet.isInvisible = true
            layoutPingResult.isVisible = true
            txtPing.isInvisible = true
            txtPingDescription.isInvisible = true
        }
    }

    private fun setupEvent() {
        binding.btnDone.setOnClickListener {
            mainViewModel.saveResultTestingModel(locationTestingModel)
            findNavController().popBackStack()
        }
    }

    /**
     *  Show dialog when testing error
     * */
    private fun showDialogWithErrorTesting(message: String) {
        AlertDialog.Builder(requireContext()).apply {
            setMessage(message)
            setCancelable(true)

            setPositiveButton(
                getString(R.string.yes)
            ) { _, _ ->
                findNavController().popBackStack()
            }
        }.create().show()
    }

    /**
     *  Start testing
     * */
    private fun startTesting() {
        viewModel.startTestDownload()
        viewModel.getPing()
    }
}