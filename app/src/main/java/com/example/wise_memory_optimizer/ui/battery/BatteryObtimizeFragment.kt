package com.example.wise_memory_optimizer.ui.battery

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.wise_memory_optimizer.MainActivity
import com.example.wise_memory_optimizer.MyApplication
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.databinding.FragmentBatteryobtimizeBinding
import com.example.wise_memory_optimizer.ui.battery.service.BatteryService
import com.example.wise_memory_optimizer.utils.ChargeUtils
import com.example.wise_memory_optimizer.utils.NavigationUtils
import com.example.wise_memory_optimizer.utils.Utils
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.Viewport
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.util.*


class BatteryObtimizeFragment : Fragment() {
    private var batteryStatus: Intent? = null
    private var batteryReceiver: BroadcastReceiver? = null
    private var _binding: FragmentBatteryobtimizeBinding? = null
    var chart: GraphView? = null
    var series: LineGraphSeries<DataPoint>? = null
    var lastX = 0.0
    var set1: LineDataSet? = null
    var set2: LineDataSet? = null
    var set3: LineDataSet? = null
    var intExtra: Int? = null
    var historyChargeDao: HistoryChargeDao? = null
    private var timeMillsRemaining = 12
    var timeLoad: Long = 0
    var TimeReload = (120 * 1000).toLong()
    private var receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == BatteryService.ACTION_BATTERY_CHANGED_SEND) {
                val batteryInfo: BatteryInfo? =
                    intent.getParcelableExtra<Parcelable>(BatteryInfo.BATTERY_INFO_KEY) as BatteryInfo?
//                this@BatteryObtimizeFragment.initHistoryChart()
                Log.e("batteryInfo", "batteryInfo: $batteryInfo")
            }
        }
    }

    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBatteryobtimizeBinding.inflate(inflater, container, false)
        val root: View = _binding?.root!!
        chart = _binding?.todayChart
        if (timeLoad + TimeReload < System.currentTimeMillis()) {
            checkAutoOp()
            timeLoad = System.currentTimeMillis()
        } else {
            _binding?.txtAutoOp?.visibility = View.GONE
        }
        initHistoryDao()
        initReceiver()
        initHistoryChart()

        _binding?.btnOptimizer?.setOnClickListener(View.OnClickListener {
            if (Utils.checkSystemWritePermission(activity as MainActivity)) {
//                var intent = Intent(activity, BatteryOptimizingActivity::class.java)
//                startActivity(intent)
                var bundle = Bundle()
                NavigationUtils.navigate(_binding?.btnOptimizer!!, R.id.battery_optimizing, bundle)

            } else {
                Utils.openAndroidPermissionsMenu(activity as MainActivity)
            }
        })
        return root
    }

    private fun checkAutoOp() {
        if (PreferenceUtil.getBoolean(
                context,
                PreferenceUtil.SETTING_PIN,
                false
            ) && ChargeUtils.BATTERY_LEVEL >= PreferenceUtil.getInt(
                context,
                PreferenceUtil.PROGRESS_PIN
            )
        ) {
            _binding?.txtAutoOp?.visibility = View.VISIBLE
            var countDownTimer = object : CountDownTimer(5000, 1000) {
                @SuppressLint("SetTextI18n")
                override fun onTick(p0: Long) {
                    _binding?.txtAutoOp?.text =
                        context?.resources?.getString(R.string.automatically_optimize_in) + (p0 / 1000).toInt()
                }

                override fun onFinish() {
                    var bundle = Bundle()
                    NavigationUtils.navigate(
                        _binding?.btnOptimizer!!,
                        R.id.battery_optimizing,
                        bundle
                    )
                }
            }
            countDownTimer.start()
        } else {
            _binding?.txtAutoOp?.visibility = View.GONE
        }
    }

    private fun initHistoryDao() {
        val db = Room.databaseBuilder(
            requireActivity(),
            AppDataBase::class.java, "historyCharge"
        ).allowMainThreadQueries().build()
        historyChargeDao = db.historyChargeDao()

        intExtra?.let { setData(it) }
    }

    fun setData(percentRam: Int) {
        var curentTimer = System.currentTimeMillis()
        lastX += 1
        series?.appendData(DataPoint(lastX, percentRam.toDouble()), false, 5)
    }


    private fun initHistoryChart() {
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

    private fun initBatteryView(batteryLevel: Int) {
        if (batteryLevel <= 15) {
            _binding?.ivBattery?.setImageDrawable(resources.getDrawable(R.drawable.ic_pin_1))
            _binding?.percentBattery?.setTextColor(resources.getColor(R.color.red_400))
        } else if (batteryLevel in 16..35) {
            _binding?.ivBattery?.setImageDrawable(resources.getDrawable(R.drawable.ic_pin_2))
            _binding?.percentBattery?.setTextColor(resources.getColor(R.color.orange_500))
        } else if (batteryLevel in 36..55) {
            _binding?.ivBattery?.setImageDrawable(resources.getDrawable(R.drawable.ic_pin_3))
            _binding?.percentBattery?.setTextColor(resources.getColor(R.color.core_green))
        } else if (batteryLevel in 56..75) {
            _binding?.ivBattery?.setImageDrawable(resources.getDrawable(R.drawable.ic_pin_4))
            _binding?.percentBattery?.setTextColor(resources.getColor(R.color.core_green))
        } else if (batteryLevel in 76..100) {
            _binding?.ivBattery?.setImageDrawable(resources.getDrawable(R.drawable.ic_pin_5))
            _binding?.percentBattery?.setTextColor(resources.getColor(R.color.core_green))
        }

    }

    private fun initReceiver() {
        batteryReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                intExtra = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
//                if (intent.action == BatteryService.ACTION_BATTERY_CHANGED_SEND) {
                if (intent.action == "android.intent.action.BATTERY_CHANGED") {
                    updateChargingProgress()
                    setData(intExtra!!)
                    var historyCharge = HistoryCharge()
                    historyCharge.statusLevel = intExtra
                    Log.e("historyChargeDao?.getAll() ", historyChargeDao?.getItems().toString())
                    historyChargeDao?.insert(historyCharge)
                    historyChargeDao?.update(historyCharge)
                    this@BatteryObtimizeFragment.chart?.invalidate()
//                  updateBatteryInfo(intent)
                    //                    if (intExtra == 1 || intExtra == 4 || intExtra == 2) {
//                        int unused = MainActivity.this.currentPlug = intExtra;
//                    } else if (MainActivity.this.currentPlug > -2 && MainActivity.this.currentPlug != intExtra) {
//                        boolean isRestoreUnplug = HawkHelper.isRestoreUnplug();
//                        if (HawkHelper.isExitUnplug()) {
//                            MainActivity.this.finish();
//                        } else if (!MyApplication.isOptimized() || isRestoreUnplug) {
////                            updateButtonOptimize();
//                        }
//                    }
                }
                if (intent.action == "android.intent.action.ACTION_POWER_CONNECTED") {
                    ChargeUtils.BATTERY_PLUGGED =
                        intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)
                    updateChargingProgress()
                }
                if (intent.action == "android.intent.action.ACTION_POWER_DISCONNECTED") {
                    ChargeUtils.BATTERY_PLUGGED = 0
                    updateChargingProgress()
                }
            }
        }
    }

    fun updateChargingProgress() {
        updateTimeEstimateTitle()
        initBatteryView(ChargeUtils.BATTERY_LEVEL)
//        waveLoadingView.setProgressValue(ChargeUtils.BATTERY_LEVEL)
        _binding?.percentBattery?.text = ChargeUtils.BATTERY_LEVEL.toString() + "%"
        ChargeUtils.CUR_CAPACITY = Utils.getCapacity(activity as MainActivity)

        // Time left
        var batteryLevel: Int = ChargeUtils.BATTERY_LEVEL
        if (ChargeUtils.BATTERY_PLUGGED > 0 && !ChargeUtils.BATTERY_STATUS_FULL) {
            batteryLevel = 101 - ChargeUtils.BATTERY_LEVEL
            Log.e(
                "BATTERY_STATUS_FULL",
                ChargeUtils.BATTERY_PLUGGED.toString() + ":" + batteryLevel
            )
        }
        timeMillsRemaining = Utils.getTimeRemainMls(
            ChargeUtils.BATTERY_PLUGGED,
            ChargeUtils.CUR_CAPACITY,
            ChargeUtils.BATTERY_STATUS_FULL
        )
        val j: Long = this.timeMillsRemaining * batteryLevel.toLong()
        _binding?.tvHourEs?.text = String.format(
            Locale.getDefault(),
            "%02d",
            *arrayOf<Any>(java.lang.Long.valueOf(j / 3600000))
        )
        _binding?.tvMinuteEs?.text = String.format(
            Locale.getDefault(), "%02d", *arrayOf<Any>(
                java.lang.Long.valueOf(
                    j % 3600000 / 60000
                )
            )
        )

        val i: Long = this.timeMillsRemaining * ChargeUtils.BATTERY_LEVEL.toLong()
//        val i: Long = batteryLevel.toLong()
        _binding?.tvHour?.text = String.format(
            Locale.getDefault(),
            "%02d",
            *arrayOf<Any>(java.lang.Long.valueOf(i / 3600000))
        )
        _binding?.tvMinute?.text = String.format(
            Locale.getDefault(), "%02d", *arrayOf<Any>(
                java.lang.Long.valueOf(
                    i % 3600000 / 60000
                )
            )
        )
    }


    private fun updateTimeEstimateTitle() {

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.toolbar?.toolbarTitle?.text = resources.getString(R.string.tv_batter_obtimize)

        _binding?.toolbar?.ivBack?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun checkOnWindowHasFocus() {
        onWindowHasFocus()
    }


    private fun onWindowHasFocus() {
//        checkStateOptimize();
        MyApplication.showNotificationOptimize()
    }

    fun mRegisterReceiver() {
        val intentFilter = IntentFilter()
        activity?.registerReceiver(batteryReceiver, intentFilter)
    }

    override fun onResume() {
        super.onResume()
//        mRegisterReceiver()
//        val intent = Intent()
//        intent.action = BatteryService.ACTION_BATTERY_NEED_UPDATE
//        activity?.sendBroadcast(intent)
        val intentFilter = IntentFilter("android.intent.action.BATTERY_CHANGED")
        intentFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED")
        intentFilter.addAction("android.intent.action.ACTION_POWER_CONNECTED")
        batteryStatus = activity?.registerReceiver(batteryReceiver, intentFilter)
        checkOnWindowHasFocus()
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.unregisterReceiver(batteryReceiver)
    }

    class MyXaxisValueFormater(private val mValues: Array<String>) : ValueFormatter() {
        override fun getFormattedValue(f: Float, axisBase: AxisBase): String {
            return mValues[f.toInt()]
        }
    }
}