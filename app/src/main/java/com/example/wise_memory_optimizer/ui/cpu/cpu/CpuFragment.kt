package com.example.wise_memory_optimizer.ui.cpu.cpu

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.wise_memory_optimizer.MainActivity
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.databinding.FragmentCpuBoosterBinding
import com.example.wise_memory_optimizer.ui.battery.PreferenceUtil
import com.example.wise_memory_optimizer.utils.NavigationUtils
import com.example.wise_memory_optimizer.utils.Utils
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.Viewport
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries

class CpuFragment : Fragment() {

    var chart: GraphView? = null
    var series: LineGraphSeries<DataPoint>? = null
    var lastX = 0.0
    private var batteryReceiver: BroadcastReceiver? = null
    lateinit var binding: FragmentCpuBoosterBinding
    var timeLoad: Long = 0
    var TimeReload = (120 * 1000).toLong()
    var temperature: Int? = null
    var countDownTimer: CountDownTimer? = null
    private val viewModel by lazy {
        ViewModelProvider(this).get(CpuViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCpuBoosterBinding.inflate(inflater)
        chart = binding.testChart

        initReceiver()
        initChart()
        return binding.root
    }

    private fun checkAutoOp() {
        if (PreferenceUtil.getBoolean(
                context,
                PreferenceUtil.SETTING_CPU,
                false
            ) && temperature!! >= PreferenceUtil.getInt(context, PreferenceUtil.PROGRESS_CPU)
        ) {
            binding.txtAutoOp.visibility = View.VISIBLE
            countDownTimer = object : CountDownTimer(5000, 1000) {
                @SuppressLint("SetTextI18n")
                override fun onTick(p0: Long) {
                    binding.txtAutoOp.text =
                        context?.resources?.getString(R.string.automatically_optimize_in) + (p0 / 1000).toInt()
                }

                override fun onFinish() {
                    if (Utils.checkSystemWritePermission(activity as MainActivity)) {
                        findNavController().navigate(R.id.action_cpu_obtimize_to_cpu_obtimizing)
                    } else {
                        Utils.openAndroidPermissionsMenu(activity as MainActivity)
                    }
                }
            }
            countDownTimer?.start()
        } else {
            binding.txtAutoOp.visibility = View.GONE
        }
    }

    private fun initChart() {
        series = LineGraphSeries()
        series?.color = Color.WHITE;
        series?.title = "hello"
        series?.isDrawDataPoints = true
        series?.isDrawBackground = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            series?.backgroundColor = resources.getColor(R.color.core_green_graph)
        }
        var viewport: Viewport? = chart?.viewport
        viewport?.setMinY(0.0)
        viewport?.setMaxX(10.0)
        chart?.addSeries(series)
    }

    fun setData(percentRam: Int) {
        var curentTimer = System.currentTimeMillis()
        lastX += 1
        series?.appendData(DataPoint(lastX, percentRam.toDouble() / 10), false, 5)
    }


    private fun initReceiver() {
        batteryReceiver = object : BroadcastReceiver() {
            @SuppressLint("SetTextI18n")
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == "android.intent.action.BATTERY_CHANGED") {
                    temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)
                    binding.tvTemp.text = temperature?.toFloat()?.div(10)
                        .toString() + resources.getString(R.string.celsius)
                    setData(temperature!!)
                    if (timeLoad + TimeReload < System.currentTimeMillis()) {
                        checkAutoOp()
                        timeLoad = System.currentTimeMillis()
                    } else {
                        binding.txtAutoOp.visibility = View.GONE
                    }
                    Log.e("binding.tvTemp :", ((temperature!! % 10).toFloat()).toString())
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initTemp(intent: Intent) {


//        val processBuilder: ProcessBuilder
//        var cpuDetails = ""
//        val DATA = arrayOf("/system/bin/cat", "/proc/cpuinfo")
//        val `is`: InputStream
//        val process: Process
//        val bArray = ByteArray(1024)
//
//        try {
//            processBuilder = ProcessBuilder(*DATA)
//            process = processBuilder.start()
//            `is` = process.inputStream
//            while (`is`.read(bArray) != -1) {
//                cpuDetails += String(bArray) //Stroing all the details in cpuDetails
//            }
//            `is`.close()
//        } catch (ex: IOException) {
//            ex.printStackTrace()
//        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.toolbarTitle.text = resources.getString(R.string.tv_cpu_booster)

        binding.toolbar.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnOptimizer.setOnClickListener {
            if (Utils.checkSystemWritePermission(activity as MainActivity)) {
                findNavController().navigate(R.id.action_cpu_obtimize_to_cpu_obtimizing)
            } else {
                Utils.openAndroidPermissionsMenu(activity as MainActivity)
            }
        }

        setupObserver()
        viewModel.startCheckCpu()
    }

    override fun onResume() {
        super.onResume()
        IntentFilter("android.intent.action.BATTERY_CHANGED").apply {
            addAction("android.intent.action.ACTION_POWER_DISCONNECTED")
            addAction("android.intent.action.ACTION_POWER_CONNECTED")
        }.also {
            (requireActivity() as? MainActivity)?.registerReceiver(batteryReceiver, it)
        }
    }

    private fun setupObserver() {
        viewModel.cpuUsedPercent.observe(viewLifecycleOwner) {
            binding.run {
                txtCurrentCpu.text = "$it %"
                precentage.text = "$it %"
                progressOverall.progress = it
                txtCpuFree.text = "${100 - it} %"
                if (it <= 60) {
                    activity?.resources?.getColor(R.color.core_green)?.let { it1 ->
                        progressOverall.progressTintList =
                            ColorStateList.valueOf(it1)
                    }
                } else if (it in 61..80) {
                    activity?.resources?.getColor(R.color.orange_500)?.let { it1 ->
                        progressOverall.progressTintList =
                            ColorStateList.valueOf(it1)
                    }
                } else if (it in 81..100) {
                    activity?.resources?.getColor(R.color.red_400)?.let { it1 ->
                        progressOverall.progressTintList =
                            ColorStateList.valueOf(it1)
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        (requireActivity() as? MainActivity)?.unregisterReceiver(batteryReceiver)
    }
}