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
import com.example.wise_memory_optimizer.model.vpn.Server
import com.example.wise_memory_optimizer.ui.dialog.DialogInformationVpn
import com.example.wise_memory_optimizer.ui.dialog.DialogLoadingVpn
import com.example.wise_memory_optimizer.ui.internet.check.CheckInternetSpeedViewModel
import com.example.wise_memory_optimizer.ui.receiver.NetworkChangeReceiver
import com.example.wise_memory_optimizer.ui.vpn.ChangeVpnViewModel
import com.example.wise_memory_optimizer.utils.NavigationUtils
import com.example.wise_memory_optimizer.utils.NetworkUtils
import com.example.wise_memory_optimizer.utils.NetworkUtils.NETWORK_STATUS_NOT_CONNECTED
import com.example.wise_memory_optimizer.utils.Utils.Companion.checkDoubleClick
import com.example.wise_memory_optimizer.utils.showStateTesting
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.blinkt.openvpn.OpenVpnApi
import de.blinkt.openvpn.core.OpenVPNService
import de.blinkt.openvpn.core.OpenVPNThread
import java.nio.charset.StandardCharsets


class HomeFragment : Fragment() {

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
        initData()
        initObserver()
        networkReceiver = object : NetworkChangeReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                super.onReceive(context, intent)
                if (intent!!.action == "android.net.conn.CONNECTIVITY_CHANGE") {
                    val status = intent.getIntExtra("status", 0)
                    if (status != NETWORK_STATUS_NOT_CONNECTED && (viewModel == null || viewModel!!.dfCity.code == null) ) {
                        activity!!.runOnUiThread({
                            initData()
                        })
                    }
                }
            }
        }
        IntentFilter("android.net.conn.CONNECTIVITY_CHANGE").also {
            (requireActivity() as? MainActivity)?.registerReceiver(networkReceiver, it)
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

    fun initLocalNetwork(){
        binding.txtIpAddress.text = NetworkUtils.getIpAddress(context)
        binding.txtNation.text = NetworkUtils.findSSIDForWifiInfo(context)
    }

    fun initData() {
        viewModel = activity?.let {
            ViewModelProvider(it).get(
                ChangeVpnViewModel::class.java
            )
        }
        initLocalNetwork()
        if (viewModel!!.dfCity.code != null){
            internetSpeedViewModel.getPing()
            return
        }
        if (NetworkUtils.isNetworkAvailable(activity)) {
            if (dialogInformationVpn!!.isShowing) dialogInformationVpn!!.dismiss()
            activity?.let { FirebaseApp.initializeApp(it) }
            database = FirebaseDatabase.getInstance()
            firebaseStorage = FirebaseStorage.getInstance()
            storageRef = firebaseStorage!!.reference
            databaseReference = database!!.reference
            if (!dialogLoadingVpn!!.isShowing) {
                dialogLoadingVpn!!.show()
                dialogLoadingVpn!!.loadingInfo()
                viewModel!!.getData(databaseReference, context) { o: Any? ->
                    internetSpeedViewModel.getPing()
                    if (dialogLoadingVpn!!.isShowing && ! requireActivity().isFinishing) dialogLoadingVpn!!.dismiss()
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
        (requireActivity() as? MainActivity)?.unregisterReceiver(networkReceiver)
    }

    override fun onResume() {
        super.onResume()
        drawerLayout?.closeDrawer(GravityCompat.END);
        LocalBroadcastManager.getInstance(requireActivity())
            .registerReceiver(broadcastReceiver, IntentFilter("connectionState"))
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(requireActivity()!!).unregisterReceiver(broadcastReceiver)
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
                    startActivityForResult(intent, 1)
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
        if (viewModel == null || viewModel!!.dfCity.code == null)
            return
        server.country = viewModel!!.dfCity.country
        server.ovpn = viewModel!!.dfCity.code + ".ovpn"
        server.ovpnUserName = viewModel!!.dfCity.username
        server.ovpnUserPassword = viewModel!!.dfCity.pass
        // .ovpn file
        storageRef!!.child("ovpn").child(server.ovpn).getBytes(Long.MAX_VALUE)
            .addOnSuccessListener(
                { bytes: ByteArray? ->
                    try {
                        OpenVpnApi.startVpn(
                            context,
                            String(bytes!!, StandardCharsets.UTF_8),
                            server.getCountry(),
                            server.getOvpnUserName(),
                            server.getOvpnUserPassword()
                        )
                    } catch (e: RemoteException) {
                        e.printStackTrace()
                    }
                    if (dialogLoadingVpn != null && !dialogLoadingVpn!!.isShowing) dialogLoadingVpn!!.show()
                    if (dialogLoadingVpn!!.isShowing) dialogLoadingVpn!!.setStatus(
                        requireContext().getString(
                            R.string.connecting
                        )
                    )
                    vpnStart = true
                })
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

    private fun updateStatus(isOn: Boolean) {
    }

    fun setStatus(connectionState: String?) {
        if (connectionState != null) when (connectionState) {
            "DISCONNECTED" -> {
                vpnStart = false
                if (dialogLoadingVpn != null && dialogLoadingVpn!!.isShowing) dialogLoadingVpn!!.dismiss()
            }
            "CONNECTED" -> {
                vpnStart = true
                if (dialogLoadingVpn != null && dialogLoadingVpn!!.isShowing) dialogLoadingVpn!!.dismiss()
                if (dialogInformationVpn != null && !dialogInformationVpn!!.isShowing) {
                    dialogInformationVpn!!.show()
                    dialogInformationVpn!!.setState(DialogInformationVpn.TYPE_INFO.SUCCESS_VPN_HOME)
                }
                viewModel!!.serverCahce = server
                updateStatus(true)
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
                setStatus(intent.getStringExtra("state"))
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
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

}