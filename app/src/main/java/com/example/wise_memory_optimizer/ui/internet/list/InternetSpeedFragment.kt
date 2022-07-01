package com.example.wise_memory_optimizer.ui.internet.list

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wise_memory_optimizer.MainViewModel
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.custom.ExtTextView
import com.example.wise_memory_optimizer.databinding.FragmentInternetSpeedBinding
import com.example.wise_memory_optimizer.model.location_speed_test.LocationTestingModel
import com.example.wise_memory_optimizer.ui.battery.service.PowerService
import com.example.wise_memory_optimizer.ui.dialog.InputDialog
import com.example.wise_memory_optimizer.ui.internet.check.CheckInternetSpeedFragment.Companion.LOCATION_NAME
import com.example.wise_memory_optimizer.utils.formatMbps
import com.example.wise_memory_optimizer.utils.setTextPing
import com.example.wise_memory_optimizer.utils.toMbps


class InternetSpeedFragment : Fragment() {
    private val viewModel: MainViewModel by navGraphViewModels(R.id.mobile_navigation)

    private lateinit var binding: FragmentInternetSpeedBinding

    private var adapterSpeedTest: InternetSpeedAdapter? = null
    private var listLocationTesting = mutableListOf<LocationTestingModel>()

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
        updateRcvData()
    }

    private fun initData() {
        viewModel.resultTestingModel?.let {
            updateDefaultTestingData(it)
        }
    }

    private fun setupUI() {
        setupSpeedView()
        setupRecycleView()

        if (listLocationTesting.isEmpty()) {
            binding.run {
                llEmpty.isVisible = true
                rcvLocationChecked.isVisible = false
            }
        } else {
            binding.run {
                llEmpty.isVisible = false
                rcvLocationChecked.isVisible = false
            }
        }
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
     *  Setup recycle view location checked
     * */
    private fun setupRecycleView() {
        adapterSpeedTest =
            InternetSpeedAdapter(listLocationTesting, ::onClickEditNameItem, ::onClickFavoriteItem)
        binding.rcvLocationChecked.apply {
            adapter = adapterSpeedTest
            layoutManager = LinearLayoutManager(requireContext())
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
            maxSpeed = 100F
            sections.run {
                get(0).width = resources.getDimension(com.intuit.sdp.R.dimen._12sdp)
                get(1).width = resources.getDimension(com.intuit.sdp.R.dimen._12sdp)
                get(2).width = resources.getDimension(com.intuit.sdp.R.dimen._12sdp)
            }
        }
    }


    /**
     *  Call back : click edit name item location checked
     * */
    private fun onClickEditNameItem(model: LocationTestingModel, position: Int, view: View) {
        showPopup(model, position, view)
    }

    /**
     *  Call back : click favorite item location checked
     * */
    @SuppressLint("NotifyDataSetChanged")
    private fun onClickFavoriteItem(
        model: LocationTestingModel,
        isFavorite: Boolean,
        position: Int
    ) {
        // unselect all favorite
        listLocationTesting.forEach { locationTestingModel ->
            if (locationTestingModel.isFavorite) {
                locationTestingModel.isFavorite = false
            }
        }

        model.isFavorite = isFavorite
        listLocationTesting.run {
            removeAt(position)
            add(0, model)
        }
        adapterSpeedTest?.notifyDataSetChanged()
    }

    /**
     *  Update last testing data value
     * */
    private fun updateDefaultTestingData(defaultTestingModel: LocationTestingModel) {
        binding.run {
            defaultTestingModel.ping?.let {
                txtPing.setTextPing(it)
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

    /**
     *  Update data of recycle view testing
     * */
    @SuppressLint("NotifyDataSetChanged")
    private fun updateRcvData() {
        if (viewModel.listSpeedTest.isNotEmpty()) {
            binding.rcvLocationChecked.isVisible = true
            binding.llEmpty.isVisible = false
            listLocationTesting.run {
                clear()
                addAll(viewModel.listSpeedTest)
            }
            adapterSpeedTest?.notifyDataSetChanged()
        } else {
            binding.rcvLocationChecked.isVisible = false
            binding.llEmpty.isVisible = true
        }
    }

    /**
     *  Show popup dialog to handle edit item location checked
     * */
    @SuppressLint("ResourceType")
    private fun showPopup(model: LocationTestingModel, position: Int, anchorView: View) {
        val inflater =
            (requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_edit_item_checked, null).apply {
            findViewById<ExtTextView>(R.id.btn_rename).setOnClickListener {
                InputDialog(
                    context = requireContext(),
                    title = getString(R.string.rename),
                    onPositiveClick = {
                        model.roomName = it
                        adapterSpeedTest?.notifyItemChanged(position)
                    }
                ).show()
                rootView.isVisible = false
            }

            findViewById<ExtTextView>(R.id.btn_delete).setOnClickListener {
                listLocationTesting.removeAt(position)
                adapterSpeedTest?.notifyItemRemoved(position)
                rootView.isVisible = false
            }
        }

        PopupWindow(requireContext()).apply {
            contentView = view
            isOutsideTouchable = true
        }.showAsDropDown(anchorView)

    }
}