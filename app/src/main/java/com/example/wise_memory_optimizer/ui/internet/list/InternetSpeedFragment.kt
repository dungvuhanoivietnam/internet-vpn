package com.example.wise_memory_optimizer.ui.internet.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.wise_memory_optimizer.MainViewModel
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.databinding.FragmentInternetSpeedBinding
import com.example.wise_memory_optimizer.model.location_speed_test.LocationTestingModel
import com.example.wise_memory_optimizer.ui.battery.service.PowerService
import com.example.wise_memory_optimizer.ui.dialog.InputDialog
import com.example.wise_memory_optimizer.ui.internet.check.CheckInternetSpeedFragment.Companion.LOCATION_NAME
import com.example.wise_memory_optimizer.ui.internet.list.adapter.InternetPagerAdapter
import com.example.wise_memory_optimizer.utils.formatMbps
import com.example.wise_memory_optimizer.utils.setTextPing
import com.google.android.material.tabs.TabLayoutMediator


class InternetSpeedFragment : Fragment() {
    private val viewModelActivity: MainViewModel by navGraphViewModels(R.id.mobile_navigation)

    private lateinit var binding: FragmentInternetSpeedBinding

    private var pagerAdapter : InternetPagerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInternetSpeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        setupUI()
        setupEvent()
    }

    private fun initData() {
        viewModelActivity.resultTestingModel?.let {
            updateDefaultTestingData(it)
        }
    }

    private fun setupUI() {
        setupSpeedView()
        setupViewPager()

        if (viewModelActivity.listRecentlyTesting.isEmpty() && viewModelActivity.getListFavoriteTesting().isEmpty()) {
            binding.run {
                llEmpty.isVisible = true
                pager.isVisible = false
                tabLayout.isVisible = false
            }
        } else {
            binding.run {
                llEmpty.isVisible = false
                pager.isVisible = true
                tabLayout.isVisible = true
            }
        }
    }

    private fun setupViewPager() {
        pagerAdapter = InternetPagerAdapter(childFragmentManager,lifecycle)
        binding.pager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout,binding.pager){tab, position ->
            if (position == 0){
                tab.text = getString(R.string.recently)
            }else{
                tab.text = getString(R.string.favourite)
            }
        }.attach()
    }

    private fun setupEvent() {
        binding.btnCheck.setOnClickListener {
            InputDialog(
                context = requireContext(),
                title = getString(R.string.name_location),
                onPositiveClick = {
                    val data = Bundle().apply {
                        putString(LOCATION_NAME, it)
                    }
                    findNavController().navigate(
                        R.id.action_list_internet_speed_to_check_internet_speed,
                        data
                    )
                }
            ).show()
        }

        binding.imgBack.setOnClickListener {
            requireActivity().onBackPressed()
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
            maxSpeed = 100F
            sections.run {
                get(0).apply {
                    width = resources.getDimension(com.intuit.sdp.R.dimen._40sdp)
                    color = ContextCompat.getColor(requireContext(), R.color.color_violet)
                }

                get(1).apply {
                    width = resources.getDimension(com.intuit.sdp.R.dimen._40sdp)
                    color = ContextCompat.getColor(requireContext(), R.color.color_red)
                }

                get(2).apply {
                    width = resources.getDimension(com.intuit.sdp.R.dimen._40sdp)
                    color = ContextCompat.getColor(requireContext(), R.color.color_green_00)
                }
            }
        }
    }

    /**
     *  Update last testing data value
     * */
    private fun updateDefaultTestingData(defaultTestingModel: LocationTestingModel) {
        binding.run {
            defaultTestingModel.ping?.let {
                txtPing.text = it.toInt().toString()
                spvInternet.speedTo(it)
                PowerService.startMy(context)
                PowerService.setMBS(it)
                Log.e("it", it.toString())
            }

            defaultTestingModel.uploadPoint?.let {
                txtUpload.text = it.formatMbps()
            }

            defaultTestingModel.downloadPoint?.let {
                txtDownload.text = it.formatMbps()
            }
        }
    }
}