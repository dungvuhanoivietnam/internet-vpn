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
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.custom.MenuCustomer
import com.example.wise_memory_optimizer.databinding.FragmentHomeBinding
import com.example.wise_memory_optimizer.utils.NavigationUtils


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    var drawerLayout: DrawerLayout?= null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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
        home.setListener(object : MenuCustomer.Listener{
            override fun onClick() {
                val data = Bundle()
                data.putString("data", "Auto Optimize")
                findNavController()
                    .navigate(R.id.action_back, data)
                drawerLayout?.closeDrawers()
            }
        })

        autoOptimize.setListener(object : MenuCustomer.Listener{
            override fun onClick() {
                val data = Bundle()
                drawerLayout?.closeDrawers()
                data.putString("data", "Auto Optimize")
                findNavController()
                    .navigate(R.id.action_auto_optimize, data)
            }
        })

        notification.setListener(object : MenuCustomer.Listener{
            override fun onClick() {
                drawerLayout?.closeDrawers()
            }
        })
        language.setListener(object : MenuCustomer.Listener{
            override fun onClick() {
                val data = Bundle()
                drawerLayout?.closeDrawers()
                data.putString("data", "Auto Optimize")
                findNavController()
                    .navigate(R.id.action_nav_view_language, data)
            }
        })

        privacy.setListener(object : MenuCustomer.Listener{
            override fun onClick() {
                val data = Bundle()
                drawerLayout?.closeDrawers()
                data.putString("data", "Auto Optimize")
                findNavController()
                    .navigate(R.id.action_nav_view_privacy, data)
            }
        })

        aboutApp.setListener(object : MenuCustomer.Listener{
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
        llCpu.setOnClickListener{
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
        return root
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