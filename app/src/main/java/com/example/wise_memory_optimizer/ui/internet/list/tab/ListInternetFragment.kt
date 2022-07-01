package com.example.wise_memory_optimizer.ui.internet.list.tab

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wise_memory_optimizer.MainViewModel
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.custom.ExtTextView
import com.example.wise_memory_optimizer.databinding.FragmentListInternetBinding
import com.example.wise_memory_optimizer.model.location_speed_test.LocationTestingModel
import com.example.wise_memory_optimizer.ui.dialog.InputDialog
import com.example.wise_memory_optimizer.ui.internet.list.adapter.InternetSpeedAdapter

class ListInternetFragment(
    var isFavorite : Boolean = false
) : Fragment() {
    private val viewModel: MainViewModel by navGraphViewModels(R.id.mobile_navigation)

    private lateinit var binding: FragmentListInternetBinding

    private var adapterSpeedTest: InternetSpeedAdapter? = null
    private var listLocationTesting = mutableListOf<LocationTestingModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListInternetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycleView()
        updateRcvData()
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
     *  Update data of recycle view testing
     * */
    @SuppressLint("NotifyDataSetChanged")
    private fun updateRcvData() {
        if (viewModel.listSpeedTest.isNotEmpty()) {
            binding.rcvLocationChecked.isVisible = true
            listLocationTesting.run {
                clear()
                addAll(viewModel.listSpeedTest)
            }
            Log.e("=======>", "updateRcvData ${listLocationTesting.size}" )
            adapterSpeedTest?.notifyDataSetChanged()
        } else {
            binding.rcvLocationChecked.isVisible = false
        }
    }

    /**
     *  Call back : click edit name item location checked
     * */
    private fun onClickEditNameItem(model: LocationTestingModel, position: Int, view: View) {
        showPopup(model, position, view)
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