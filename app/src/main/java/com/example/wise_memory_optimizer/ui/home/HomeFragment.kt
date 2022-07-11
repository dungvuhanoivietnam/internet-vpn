package com.example.wise_memory_optimizer.ui.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.VpnService
import android.os.Bundle
import android.os.RemoteException
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.wise_memory_optimizer.MainActivity
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.custom.MenuCustomer
import com.example.wise_memory_optimizer.databinding.FragmentHomeBinding
import com.example.wise_memory_optimizer.model.vpn.City
import com.example.wise_memory_optimizer.model.vpn.Server
import com.example.wise_memory_optimizer.ui.dialog.DialogInformationVpn
import com.example.wise_memory_optimizer.ui.dialog.DialogLoadingVpn
import com.example.wise_memory_optimizer.ui.internet.check.CheckInternetSpeedViewModel
import com.example.wise_memory_optimizer.ui.receiver.NetworkChangeReceiver
import com.example.wise_memory_optimizer.ui.vpn.ChangeVpnViewModel
import com.example.wise_memory_optimizer.utils.NavigationUtils
import com.example.wise_memory_optimizer.utils.NetworkUtils
import com.example.wise_memory_optimizer.utils.NetworkUtils.NETWORK_STATUS_NOT_CONNECTED
import com.example.wise_memory_optimizer.utils.showStateTesting
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.blinkt.openvpn.OpenVpnApi
import de.blinkt.openvpn.core.OpenVPNService
import de.blinkt.openvpn.core.OpenVPNThread
import java.nio.charset.StandardCharsets


class HomeFragment : Fragment() {

    private var oldTimeClick = System.currentTimeMillis()
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private var _binding: FragmentHomeBinding? = null
    var drawerLayout: DrawerLayout? = null

    private val internetSpeedViewModel by lazy {
        ViewModelProvider(this).get(CheckInternetSpeedViewModel::class.java)
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var dialogLoadingVpn: DialogLoadingVpn? = null
    private var dialogInformationVpn: DialogInformationVpn? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        drawerLayout = binding.drawerLayout

        val mDrawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            activity, drawerLayout,
            null, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        ) {
            override fun onDrawerClosed(view: View) {
                super.onDrawerClosed(view)
                // Do whatever you want here

            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                // Do whatever you want here

            }
        }

        drawerLayout?.addDrawerListener(mDrawerToggle)

        val headerView = binding.navView.getHeaderView(0)
        val home: MenuCustomer = headerView.findViewById(R.id.home)
        val autoOptimize: MenuCustomer = headerView.findViewById(R.id.auto_optimize)
        val notification: MenuCustomer = headerView.findViewById(R.id.notification)
        val language: MenuCustomer = headerView.findViewById(R.id.language)
        val privacy: MenuCustomer = headerView.findViewById(R.id.privacy)
        val aboutApp: MenuCustomer = headerView.findViewById(R.id.about_app)
        home.setListener(object : MenuCustomer.Listener {
            override fun onClick() {
                val data = Bundle()
                data.putString("data", "Auto Optimize")
                findNavController()
                    .navigate(R.id.action_back, data)
                drawerLayout?.closeDrawers()
            }
        })

        autoOptimize.setListener(object : MenuCustomer.Listener {
            override fun onClick() {
                val data = Bundle()
                drawerLayout?.closeDrawers()
                data.putString("data", "Auto Optimize")
                findNavController()
                    .navigate(R.id.action_auto_optimize, data)
            }
        })

        notification.setListener(object : MenuCustomer.Listener {
            override fun onClick() {
                drawerLayout?.closeDrawers()
            }
        })
        language.setListener(object : MenuCustomer.Listener {
            override fun onClick() {
                val data = Bundle()
                drawerLayout?.closeDrawers()
                data.putString("data", "Auto Optimize")
                findNavController()
                    .navigate(R.id.action_nav_view_language, data)
            }
        })

        privacy.setListener(object : MenuCustomer.Listener {
            override fun onClick() {
                val data = Bundle()
                drawerLayout?.closeDrawers()
                data.putString("data", "Auto Optimize")
                findNavController()
                    .navigate(R.id.action_nav_view_privacy, data)
            }
        })

        aboutApp.setListener(object : MenuCustomer.Listener {
            override fun onClick() {
                val data = Bundle()
                drawerLayout?.closeDrawers()
                data.putString("data", "Auto Optimize")
                findNavController()
                    .navigate(R.id.action_nav_view_about_app, data)
            }
        })

        binding.txtFaster.setOnClickListener({
            if (!checkDoubleClick()) return@setOnClickListener

            if (vpnStart) {
                stopVpn()
            } else {
                if (NetworkUtils.isConnectVpn()) {
                    stopVpn()
                }
                prepareVpn()
            }
        })

        binding.home.ivRight.setOnClickListener {
            drawerLayout?.openDrawer(Gravity.RIGHT)
        }

        val llRam: LinearLayout = binding.llRam
        val llCpu: LinearLayout = binding.llCpu
        val llBattery: LinearLayout = binding.llBattery
        val llChangeVpn: LinearLayout = binding.llChangeVpn
        val llDelete: LinearLayout = binding.llDelete
        val llInternet: LinearLayout = binding.llInternet

        llRam.setOnClickListener {
            val data = Bundle()
            data.putString("data", "RAM")
            Navigation.findNavController(binding.llRam)
                .navigate(R.id.action_homeFragment_to_ram_Obtimize, data)
        }
        llCpu.setOnClickListener {
            val data = Bundle()
            data.putString("data", "CPU")
            Navigation.findNavController(binding.llCpu)
                .navigate(R.id.action_homeFragment_to_cpu_booster, data)
        }
        llBattery.setOnClickListener {
            val data = Bundle()
            data.putString("data", "RAM")
            Navigation.findNavController(binding.llBattery)
                .navigate(R.id.action_homeFragment_to_battery_Obtimize, data)
        }

        llChangeVpn.setOnClickListener {
            val data = Bundle()
            data.putString("data", "VPN")
            Navigation.findNavController(binding.llChangeVpn)
                .navigate(R.id.action_homeFragment_to_change_vpn, data)
        }

        llDelete.setOnClickListener {
            val data = Bundle()
            data.putString("data", "VPN")
            NavigationUtils.navigate(
                binding.llDelete,
                R.id.action_homeFragment_to_duplicate_delete,
                data
            )
//            Navigation.findNavController(binding.llDelete)
//                .navigate(R.id.action_homeFragment_to_duplicate_delete, data)
        }

        llInternet.setOnClickListener {
            Navigation.findNavController(binding.llInternet)
                .navigate(R.id.action_nav_home_to_list_internet_speed)
        }
        dialogLoadingVpn = context?.let {
            DialogLoadingVpn(it, R.style.MaterialDialogSheet) {
                if (dialogLoadingVpn!!.isShowing) dialogLoadingVpn!!.dismiss()
                if (!it) stopVpn()
            }
        }
        dialogInformationVpn =
            context?.let {
                DialogInformationVpn(it, R.style.MaterialDialogSheet) {
                    if (it == DialogInformationVpn.TYPE_INFO.ERROR_NETWORK) {
                        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        requireActivity().startActivityForResult(intent, 50)
                    }
                }
            }

        activity?.let { FirebaseApp.initializeApp(it) }
        database = FirebaseDatabase.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        firebaseAnalytics = Firebase.analytics
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN,Bundle())

        initData()
        initObserver()
        networkReceiver = object : NetworkChangeReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                super.onReceive(context, intent)
                if (intent!!.action == "android.net.conn.CONNECTIVITY_CHANGE") {
                    val status = intent.getIntExtra("status", 0)
                    if (status != NETWORK_STATUS_NOT_CONNECTED) {
                        initData()
                    } else if (status == NETWORK_STATUS_NOT_CONNECTED) {
                        requireActivity().runOnUiThread({
                            if (!dialogInformationVpn!!.isShowing) {
                                dialogInformationVpn!!.show()
                                dialogInformationVpn!!.setState(DialogInformationVpn.TYPE_INFO.ERROR_NETWORK)
                            }
                        })
                    }
                }
            }
        }
        IntentFilter("android.net.conn.CONNECTIVITY_CHANGE").also {
            (requireActivity() as? MainActivity)?.registerReceiver(networkReceiver, it)
        }
        viewModel = activity?.let {
            ViewModelProvider(it).get(
                ChangeVpnViewModel::class.java
            )
        }
        return root
    }

    var networkReceiver: NetworkChangeReceiver? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        initData()
    }

    private val vpnThread: OpenVPNThread = OpenVPNThread()
    private val vpnService: OpenVPNService = OpenVPNService()
    var vpnStart: Boolean = false
    private var viewModel: ChangeVpnViewModel? = null
    private var databaseReference: DatabaseReference? = null
    private var database: FirebaseDatabase? = null
    private var firebaseStorage: FirebaseStorage? = null
    private var storageRef: StorageReference? = null
    private val server = Server()

    fun initLocalNetwork() {
        binding.txtIpAddress.text = NetworkUtils.getIpAddress(context)
        binding.txtNation.text =
            if (NetworkUtils.findSSIDForWifiInfo(context) != null) NetworkUtils.findSSIDForWifiInfo(
                context
            ) else getString(R.string.noname)
    }

    fun initData() {
        if (activity == null || viewModel == null)
            return

        if (dialogInformationVpn!!.isShowing) dialogInformationVpn!!.dismiss()
        initLocalNetwork()
        if (viewModel!!.dfCity.code != null) {
            internetSpeedViewModel.getPing()
            return
        }
        if (NetworkUtils.isNetworkAvailable(activity)) {
            storageRef = firebaseStorage!!.reference
            databaseReference = database!!.reference
            if (dialogLoadingVpn != null && !dialogLoadingVpn!!.isShowing) {
                dialogLoadingVpn!!.show()
                dialogLoadingVpn!!.loadingInfo()
            }
            viewModel!!.getData(databaseReference, context) { o: Any? ->
                internetSpeedViewModel.getPing()
                if (dialogLoadingVpn == null)
                    return@getData
                if (dialogLoadingVpn!!.isShowing && !requireActivity().isFinishing) dialogLoadingVpn!!.dismiss()
            }
        } else {
            if (!dialogInformationVpn!!.isShowing) {
                dialogInformationVpn!!.show()
                dialogInformationVpn!!.setState(DialogInformationVpn.TYPE_INFO.ERROR_NETWORK)
            }
        }
    }

    private fun initObserver() {
        internetSpeedViewModel.pingValue.observe(viewLifecycleOwner) { ping ->
            binding.txtPing.text = ping.toInt().toString()
            binding.ivStatePing.showStateTesting(ping)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        dialogLoadingVpn?.dismiss()
        dialogLoadingVpn = null
        (requireActivity() as? MainActivity)?.unregisterReceiver(networkReceiver)
    }

    override fun onResume() {
        super.onResume()
        if (drawerLayout != null)
            drawerLayout?.closeDrawer(GravityCompat.END);
        LocalBroadcastManager.getInstance(requireActivity())
            .registerReceiver(broadcastReceiver, IntentFilter("connectionState"))
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(requireActivity()!!).unregisterReceiver(broadcastReceiver)
        vpnStart = false
        super.onPause()
    }

    /**
     * Prepare for vpn connect with required permission
     */
    private fun prepareVpn() {
        if (!vpnStart) {
            if (getInternetStatus()) {
                val intent = VpnService.prepare(context)
                if (intent != null) {
                    startActivityForResult(intent, 100)
                } else startVpn()
            } else {

                // No internet connection available
//                showToast("you have no internet connection !!");
            }
        } else if (stopVpn()) {

            // VPN is stopped, show a Toast message.
//            showToast("Disconnect Successfully");
        }
    }

    fun getInternetStatus(): Boolean {
        return NetworkUtils.isNetworkAvailable(activity)
    }

    /**
     * Start the VPN
     */
    private fun startVpn() {
        if (viewModel == null || viewModel!!.cities == null || viewModel!!.cities.size == 0)
            return
        if (dialogLoadingVpn != null && !dialogLoadingVpn!!.isShowing) dialogLoadingVpn!!.show()
        if (dialogLoadingVpn!!.isShowing) dialogLoadingVpn!!.setStatus(
            requireContext().getString(
                R.string.connecting
            )
        )
        var city: City? = null
        viewModel!!.cities.forEach {
            if (city == null) {
                city = it
            } else if (city!!.ping != 0) {
                if (it.ping < city!!.ping) {
                    city = it
                }
            }
        }
        server.country = city!!.country
        server.ovpn = city!!.code
        server.ovpnUserName = city!!.username
        server.ovpnUserPassword = city!!.pass
        // .ovpn file
        if (storageRef == null)
            return
        storageRef!!.child("ovpn").listAll().addOnSuccessListener {
            for (item in it.items) {
                if (item.name.contains(server.ovpn)) {
                    item.getBytes(Long.MAX_VALUE).addOnSuccessListener {
                        try {
                            OpenVpnApi.startVpn(
                                requireContext(),
                                String(it, StandardCharsets.UTF_8),
                                server.getCountry(),
                                server.getOvpnUserName(),
                                server.getOvpnUserPassword(), MainActivity::class.java
                            )
                        } catch (e: RemoteException) {
                            e.printStackTrace()
                        }
                        vpnStart = true

                    }
                    break
                }
            }
        }
    }

    fun stopVpn(): Boolean {
        try {
            vpnThread.stopProcess()
            vpnStart = false
            updateStatus(false)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun updateStatus(isOn: Boolean) {
        requireActivity().runOnUiThread({
            binding.txtFaster.setText(getString(if (isOn) R.string.cancel else R.string.faster))
            binding.txtContentSuccess.setText(getString(if (isOn) R.string.optimization_time_is_maintained_in else R.string.optimize_current_network_speed))
            binding.txtCountDown.visibility = if (!isOn) View.GONE else View.VISIBLE
        })
    }

    fun setStatus(connectionState: String?) {
        if (connectionState != null) when (connectionState) {
            "DISCONNECTED" -> {
                vpnStart = false
                updateStatus(false)
                if (dialogLoadingVpn != null && dialogLoadingVpn!!.isShowing) dialogLoadingVpn!!.dismiss()
            }
            "CONNECTED" -> {
                vpnStart = true
                updateStatus(true)
                requireActivity().runOnUiThread({
                    if (dialogLoadingVpn != null && dialogLoadingVpn!!.isShowing) dialogLoadingVpn!!.dismiss()
                    if (dialogInformationVpn != null && !dialogInformationVpn!!.isShowing) {
                        dialogInformationVpn!!.show()
                        dialogInformationVpn!!.setState(DialogInformationVpn.TYPE_INFO.SUCCESS_VPN_HOME)
                    }
                })
                if (viewModel != null)
                    viewModel!!.serverCahce = server

            }
            "WAIT" -> if (dialogLoadingVpn != null && dialogLoadingVpn!!.isShowing) dialogLoadingVpn!!.setStatus(
                requireContext().getString(R.string.waiting_for_server_connection)
            )
            "AUTH" -> if (dialogLoadingVpn != null && dialogLoadingVpn!!.isShowing) dialogLoadingVpn!!.setStatus(
                requireContext().getString(R.string.server_authenticating)
            )
            "RECONNECTING" -> if (dialogLoadingVpn != null && dialogLoadingVpn!!.isShowing) dialogLoadingVpn!!.setStatus(
                requireContext().getString(R.string.reconnecting)
            )
            "NONETWORK" -> if (dialogLoadingVpn != null && dialogLoadingVpn!!.isShowing) dialogLoadingVpn!!.setStatus(
                requireContext().getString(R.string.no_network_connection)
            )
        }
    }

    var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                requireActivity().runOnUiThread {
                    setStatus(intent.getStringExtra("state"))
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            try {
                var duration = intent.getStringExtra("duration")
                var lastPacketReceive = intent.getStringExtra("lastPacketReceive")
                var byteIn = intent.getStringExtra("byteIn")
                var byteOut = intent.getStringExtra("byteOut")
                if (duration == null) duration = "00:00:00"
                if (lastPacketReceive == null) lastPacketReceive = "0"
                if (byteIn == null) byteIn = ""
                if (byteOut == null) byteOut = ""
                if ("".equals(duration))
                    updateStatus(false)
                binding.txtCountDown.setText("" + duration)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    fun checkDoubleClick(): Boolean {
        val tmp = System.currentTimeMillis()
        if (tmp - oldTimeClick >= 1000) {
            oldTimeClick = tmp
            return true
        }
        return false
    }

}