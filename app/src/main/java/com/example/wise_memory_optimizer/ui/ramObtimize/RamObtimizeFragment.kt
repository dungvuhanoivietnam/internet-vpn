package com.example.wise_memory_optimizer.ui.ramObtimize

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.ProgressDialog
import android.content.*
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.*
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.wise_memory_optimizer.MainActivity
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.custom.AppsListItem
import com.example.wise_memory_optimizer.custom.BroadcastReceiver
import com.example.wise_memory_optimizer.custom.CleanerService
import com.example.wise_memory_optimizer.custom.RamOptimizerView
import com.example.wise_memory_optimizer.databinding.FragmentRamobtimizeBinding
import com.example.wise_memory_optimizer.tasks.TotalRamTask
import com.example.wise_memory_optimizer.ui.battery.PreferenceUtil
import com.example.wise_memory_optimizer.utils.NavigationUtils
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.Viewport
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries


class RamObtimizeFragment : Fragment(), CleanerService.OnActionListener,
    RamOptimizerView.ClearCacheListener {
    companion object {
        var percentageRam: Int? = null
            get() = field
            set(value) {
                field = value
            }
        var timeLoad: Long = 0
        var TimeReload = (180 * 1000).toLong()
    }

    var countDownTimer: CountDownTimer? = null

    private var listappString: MutableList<String> = ArrayList()
    private lateinit var listAppRunning: List<ActivityManager.RunningAppProcessInfo>
    var chart: GraphView? = null
    var series: LineGraphSeries<DataPoint>? = null
    var lastX = 0.0
    private var mProgressDialog: ProgressDialog? = null


    private var _binding: FragmentRamobtimizeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var receiver: BroadcastReceiver? = null


    private fun initReceiver() {
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == "android.intent.action.BATTERY_CHANGED") {
                    percentageRam?.let { setData(it) }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mProgressDialog = ProgressDialog(activity)
        mProgressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        mProgressDialog!!.setCanceledOnTouchOutside(false)
        mProgressDialog!!.setTitle(R.string.cleaning_cache)
        mProgressDialog!!.setMessage(getString(R.string.cleaning_in_progress))
        mProgressDialog!!.setOnKeyListener { dialog, keyCode, event -> true }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRamobtimizeBinding.inflate(inflater, container, false)
        val root: View = _binding?.root!!
        chart = _binding?.testChart
        loadInfoData()
        initChart()
        initReceiver()
        return root
    }

    private fun checkAutoOp() {
        if (PreferenceUtil.getBoolean(
                context,
                PreferenceUtil.SETTING_RAM,
                false
            ) && percentageRam!! >= PreferenceUtil.getInt(context, PreferenceUtil.PROGRESS_RAM)
        ) {
            _binding?.txtAutoOp?.visibility = View.VISIBLE
            countDownTimer = object : CountDownTimer(5000, 1000) {
                @SuppressLint("SetTextI18n")
                override fun onTick(p0: Long) {
                    _binding?.txtAutoOp?.text =
                        context?.resources?.getString(R.string.automatically_optimize_in) + (p0 / 1000).toInt()
                }

                override fun onFinish() {
                    var bundle = Bundle()
                    NavigationUtils.navigate(_binding?.btnRamBoots!!, R.id.ram_optimize_to_ram_optimizing, bundle)
                }
            }
            countDownTimer?.start()

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.toolbar?.toolbarTitle?.text = resources.getString(R.string.tv_ram_obtimize)

        _binding?.toolbar?.ivBack?.setOnClickListener {
            findNavController().popBackStack()
        }

        _binding?.btnRamBoots?.setOnClickListener(View.OnClickListener {
            var bundle = Bundle()
            NavigationUtils.navigate(_binding?.btnRamBoots!!, R.id.ram_optimize_to_ram_optimizing, bundle)
            countDownTimer?.cancel()
        })
    }

    fun loadInfoData() {
        val totalRamTask = context?.let {
            TotalRamTask(it, object : TotalRamTask.IRamTaskListener {

                override fun onGetRamDataCompleted(
                    freeRam: String?,
                    totalRam: String?,
                    percentRam: Int
                ) {
                    percentageRam = percentRam
                    if (timeLoad + TimeReload < System.currentTimeMillis()) {
                        checkAutoOp()
                        timeLoad = System.currentTimeMillis()
                    } else {
                        _binding?.txtAutoOp?.visibility = View.GONE
                    }
                    _binding?.txtPercentage?.text = "$percentRam%"
                    _binding?.progressRamBoost?.progress = percentRam
                    _binding?.progressRamBoost?.secondaryProgress = resources.getColor(R.color.red)
                    _binding?.precentage?.text = "$percentRam%"
                    _binding?.progressOverall?.progress = percentRam
                    val ramTotal = "$totalRam G "
                    _binding?.txtTotalRam?.text = ramTotal
                    val mCurrentRam = "$freeRam G "
                    _binding?.txtCurrentRam?.text = mCurrentRam
                }

                override fun onRamPercentUpdate(percentRam: Int) {
                    _binding?.txtPercentage?.text = "$percentRam%"
                    _binding?.precentage?.text = "$percentRam%"
                    _binding?.progressRamBoost?.progress = percentRam
                    _binding?.progressOverall?.progress = percentRam
                    if (percentRam <= 60) {
                        activity?.resources?.getColor(R.color.core_green)?.let { it1 ->
                            _binding?.progressRamBoost?.progressTintList =
                                ColorStateList.valueOf(it1)
                            _binding?.progressOverall?.progressTintList =
                                ColorStateList.valueOf(it1)
                            _binding?.precentage?.setTextColor(it1)
                        }
                    } else if (percentRam in 61..80) {
                        activity?.resources?.getColor(R.color.orange_500)?.let { it1 ->
                            _binding?.progressRamBoost?.progressTintList =
                                ColorStateList.valueOf(it1)
                            _binding?.progressOverall?.progressTintList =
                                ColorStateList.valueOf(it1)
                            _binding?.precentage?.setTextColor(it1)
                        }
                    } else if (percentRam in 81..100) {
                        activity?.resources?.getColor(R.color.red_400)?.let { it1 ->
                            _binding?.progressRamBoost?.progressTintList =
                                ColorStateList.valueOf(it1)
                            _binding?.progressOverall?.progressTintList =
                                ColorStateList.valueOf(it1)
                            _binding?.precentage?.setTextColor(it1)
                        }
                    }
                    setData(percentRam)
                }
            })
        }
        totalRamTask?.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

    fun setData(percentRam: Int) {
        var curentTimer = System.currentTimeMillis()
        lastX += 1
        series?.appendData(DataPoint(lastX, percentRam.toDouble()), false, 5)
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
        viewport?.setMaxX(100.0)
        chart?.addSeries(series)
    }


    override fun onResume() {
        super.onResume()
//        loadInfoData()
        IntentFilter("android.intent.action.BATTERY_CHANGED").apply {
            addAction("android.intent.action.ACTION_POWER_DISCONNECTED")
            addAction("android.intent.action.ACTION_POWER_CONNECTED")
        }.also {
            (requireActivity() as? MainActivity)?.registerReceiver(receiver, it)
        }
    }

    override fun onPause() {
        super.onPause()
        (requireActivity() as? MainActivity)?.unregisterReceiver(receiver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onScanStarted(context: Context?) {
        if (isAdded) {
        }
    }

    override fun onScanProgressUpdated(context: Context?, current: Int, max: Int) {
    }

    override fun onScanCompleted(context: Context?, apps: MutableList<AppsListItem>?) {

//        if (isAdded) {
//            updateStorageUsage()
////            showProgressBar(false)
//        }
//
//        mAlreadyScanned = true
    }

    override fun onCleanStarted(context: Context?) {
    }

    override fun onCleanCompleted(context: Context?, succeeded: Boolean) {

    }


    override fun clearCache() {

    }


}