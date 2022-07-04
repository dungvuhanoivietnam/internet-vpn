package com.example.wise_memory_optimizer.ui.home

import android.os.Bundle
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
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.custom.MenuCustomer
import com.example.wise_memory_optimizer.databinding.FragmentHomeBinding
import com.example.wise_memory_optimizer.ui.dialog.DialogInformationVpn
import com.example.wise_memory_optimizer.ui.dialog.DialogLoadingVpn
import com.example.wise_memory_optimizer.ui.internet.check.CheckInternetSpeedViewModel
import com.example.wise_memory_optimizer.ui.vpn.ChangeVpnViewModel
import com.example.wise_memory_optimizer.utils.NavigationUtils
import com.example.wise_memory_optimizer.utils.NetworkUtils
import com.example.wise_memory_optimizer.utils.showStateTesting
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import de.blinkt.openvpn.core.OpenVPNService
import de.blinkt.openvpn.core.OpenVPNThread


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

//        val navView: NavigationView = binding.navView
//        val navController = findNavController()
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        appBarConfiguration = AppBarConfiguration(setOf(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)

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
        binding.txtIpAddress.text = NetworkUtils.getIpAddress(context)
        binding.txtNation.text = NetworkUtils.findSSIDForWifiInfo(context)
        dialogLoadingVpn = context?.let {
            DialogLoadingVpn(it, R.style.MaterialDialogSheet) {

            }
        }
        dialogInformationVpn =
            context?.let {
                DialogInformationVpn(it,R.style.MaterialDialogSheet) {

                }
            }
        initData()
        initObserver()
        return root
    }

    private val vpnThread : OpenVPNThread = OpenVPNThread()
    private val vpnService: OpenVPNService = OpenVPNService()
    var vpnStart : Boolean = false
    private var viewModel: ChangeVpnViewModel? = null
    private var databaseReference: DatabaseReference? = null
    private var database: FirebaseDatabase? = null
    private var firebaseStorage: FirebaseStorage? = null

    fun initData() {
        viewModel = activity?.let {
            ViewModelProvider(it).get(
                ChangeVpnViewModel::class.java
            )
        }
        if (NetworkUtils.isNetworkAvailable(activity)) {
            activity?.let { FirebaseApp.initializeApp(it) }
            database = FirebaseDatabase.getInstance()
            firebaseStorage = FirebaseStorage.getInstance()
            databaseReference = database!!.reference
            if (viewModel!!.dfCity.code != null){
                internetSpeedViewModel.getPing()
                return
            }

            if (!dialogLoadingVpn!!.isShowing) {
                dialogLoadingVpn!!.show()
                dialogLoadingVpn!!.loadingInfo()
                viewModel!!.getData(databaseReference, context) { o: Any? ->
                    internetSpeedViewModel.getPing()
                    if (dialogLoadingVpn!!.isShowing) dialogLoadingVpn!!.dismiss()
                }
            }
        } else {
            if (!dialogInformationVpn!!.isShowing) {
                dialogInformationVpn!!.show()
                dialogInformationVpn!!.setState(DialogInformationVpn.TYPE_INFO.ERROR_NETWORK)
            }
        }
    }

    private fun initObserver(){
        internetSpeedViewModel.pingValue.observe(viewLifecycleOwner){ ping ->
            binding.txtPing.text = ping.toInt().toString()
            binding.ivStatePing.showStateTesting(ping)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        drawerLayout?.closeDrawer(GravityCompat.END);
    }

}