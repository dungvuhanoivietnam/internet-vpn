package com.example.wise_memory_optimizer.ui.internet.list.tab

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wise_memory_optimizer.MainViewModel
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.custom.ExtTextView
import com.example.wise_memory_optimizer.databinding.FragmentRecentlyInternetSpeedBinding
import com.example.wise_memory_optimizer.model.location_speed_test.LocationTestingModel
import com.example.wise_memory_optimizer.ui.dialog.InputDialog
import com.example.wise_memory_optimizer.ui.internet.check.CheckInternetSpeedFragment
import com.example.wise_memory_optimizer.ui.internet.list.adapter.InternetSpeedAdapter

class RecentlyInternetFragment : Fragment() {
    private val viewModelActivity: MainViewModel by navGraphViewModels(R.id.mobile_navigation)

    private lateinit var binding: FragmentRecentlyInternetSpeedBinding

    private var adapterSpeedTest: InternetSpeedAdapter? = null
    private var listLocationTesting = mutableListOf<LocationTestingModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecentlyInternetSpeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycleView()
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
        model.isFavorite = !isFavorite
        listLocationTesting.removeAt(position)
        viewModelActivity.run {
            addFavoriteInternetTesting(model)
            deleteRecentTestingModel(model)
        }

        adapterSpeedTest?.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        listLocationTesting.run {
            clear()
            addAll(viewModelActivity.listRecentlyTesting)
        }
        updateRcvData()
    }


    /**
     *  Update data of recycle view testing
     * */
    @SuppressLint("NotifyDataSetChanged")
    private fun updateRcvData() {
        if (listLocationTesting.isNotEmpty()) {
            binding.rcvLocationChecked.isVisible = true
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
            /**
             *   Handle rename location testing
             * */
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

            /**
             *   Handle delete location testing
             * */
            findViewById<ExtTextView>(R.id.btn_delete).setOnClickListener {
                listLocationTesting.removeAt(position)
                viewModelActivity.deleteRecentTestingModel(model)
                adapterSpeedTest?.notifyItemRemoved(position)
                rootView.isVisible = false
            }

            /**
             *   Handle recheck location testing
             * */
            findViewById<ExtTextView>(R.id.btn_recheck).setOnClickListener {
                listLocationTesting.removeAt(position)
                viewModelActivity.deleteRecentTestingModel(model)
                val data = Bundle().apply {
                    putString(CheckInternetSpeedFragment.LOCATION_NAME, model.roomName)
                }
                findNavController().navigate(
                    R.id.action_list_internet_speed_to_check_internet_speed,
                    data
                )
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