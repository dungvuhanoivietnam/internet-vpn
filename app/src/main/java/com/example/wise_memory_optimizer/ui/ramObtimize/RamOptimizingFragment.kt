package com.example.wise_memory_optimizer.ui.ramObtimize

import android.Manifest
import android.content.*
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.wise_memory_optimizer.MainActivity
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.custom.AppsListItem
import com.example.wise_memory_optimizer.custom.CleanerService
import com.example.wise_memory_optimizer.databinding.FragmentRamOptimizingBinding
import com.example.wise_memory_optimizer.ui.cpu.optimizing.CpuOptimizingModel
import com.example.wise_memory_optimizer.ui.done.DoneFragment
import com.example.wise_memory_optimizer.ui.done.OptimizeType
import com.example.wise_memory_optimizer.utils.ChargeUtils
import com.example.wise_memory_optimizer.utils.ChargeUtils.*
import com.example.wise_memory_optimizer.utils.NavigationUtils
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class RamOptimizingFragment : Fragment(), CleanerService.OnActionListener {
    private var mCleanerService: CleanerService? = null
    lateinit var binding: FragmentRamOptimizingBinding
    private var mSharedPreferences: SharedPreferences? = null
    private var mCleanOnAppStartupKey: String? = null
    private var mExitAfterCleanKey: String? = null
    private var mProgressBar: View? = null
    private var mProgressBarText: TextView? = null
    private var mAlreadyCleaned = false
    private var mAlreadyScanned = false
    private val REQUEST_STORAGE = 0
    private var loadAnimation: Animation? = null
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private var mTextSwitchingHandler: Handler? = null
    private var mTextSwitchingRunnable: Runnable? = null
    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mCleanerService = (service as CleanerService.CleanerServiceBinder).service
            mCleanerService?.setOnActionListener(this@RamOptimizingFragment)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mCleanerService!!.setOnActionListener(null)
            mCleanerService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCleanOnAppStartupKey = getString(R.string.clean_on_app_startup_key)
        mExitAfterCleanKey = getString(R.string.exit_after_clean_key)

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(
            activity
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRamOptimizingBinding.inflate(inflater)
        activity?.application?.bindService(
            Intent(
                activity,
                CleanerService::class.java
            ),
            mServiceConnection, Context.BIND_AUTO_CREATE
        )
//        if (!mCleanerService?.isCleaning!! && !mCleanerService?.isScanning!!) {
//            if (mSharedPreferences?.getBoolean(mCleanOnAppStartupKey, false) == true &&
//                !mAlreadyCleaned
//            ) {
//                mAlreadyCleaned = true
        cleanCache()
//            } else if (!mAlreadyScanned) {
//                mCleanerService?.scanCache()
//            }
//        }
        setAnimation()
        updateTextOptimizing()
        (activity as? MainActivity)?.let {
            activity?.onBackPressedDispatcher?.addCallback(
                it,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        mTextSwitchingRunnable?.let { it1 ->
                            mTextSwitchingHandler?.removeCallbacks(
                                it1
                            )
                            mTextSwitchingHandler?.postDelayed(it1, 200)
                        }
                    }
                })
        }
        return binding.root
    }

    private fun setAnimation() {
        loadAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate)
        binding.ivProgress.startAnimation(loadAnimation)
    }

    private fun updateTextOptimizing() {
        if (mTextSwitchingHandler == null) {
            mTextSwitchingHandler = Handler(Looper.getMainLooper())
            mTextSwitchingRunnable = object : Runnable {
                var loadingIndex = 0
                override fun run() {
                    when (loadingIndex % 5) {
                        0 -> {
                            binding.tvOptimizing.text =
                                (activity as? MainActivity)?.resources?.getString(R.string.optimizing)
                        }
                        1 -> {
                            binding.tvOptimizing.text =
                                (activity as? MainActivity)?.resources?.getString(R.string.optimizing1)
                        }
                        2 -> {
                            binding.tvOptimizing.text =
                                (activity as? MainActivity)?.resources?.getString(R.string.optimizing2)
                        }
                        3 -> {
                            binding.tvOptimizing.text =
                                (activity as? MainActivity)?.resources?.getString(R.string.optimizing3)
                        }
                    }
                    if (loadingIndex == 20) {
                        cleanCache()
                        clearMem(context)
                    } else if (loadingIndex == 40) {
                        offBluetooth(context)

                    } else if (loadingIndex == 60) {
                        offRotate(context)
                    } else if (loadingIndex == 80) {
                        ChargeUtils.clearMem(context)
                    }
                    if (loadingIndex == 100) {
                        val executorService: ExecutorService = Executors.newSingleThreadExecutor()
                        val longRunningTaskFuture = executorService.submit(mTextSwitchingRunnable);
                        longRunningTaskFuture.cancel(true);
                        val bundle = Bundle().apply {
                            putInt(DoneFragment.TYPE_OPTIMIZE, OptimizeType.RAM.value)
                        }
                        binding.ivProgress.clearAnimation()
                        mTextSwitchingRunnable?.let { it1 ->
                            mTextSwitchingHandler?.removeCallbacks(
                                it1
                            )
                            mTextSwitchingHandler?.postDelayed(it1, 200)
                        }
                        NavigationUtils.navigate(binding.root, R.id.done_optimizer, bundle)
                        Toast.makeText(
                            activity as? MainActivity,
                            (activity as? MainActivity)?.resources?.getText(R.string.done),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    binding.percentOptimizer.text = "$loadingIndex%"
                    loadingIndex++
                    mTextSwitchingHandler?.postDelayed(this, 200)
                }
            }
            mTextSwitchingHandler?.post(mTextSwitchingRunnable!!)
        }
    }


    fun cleanCache() {
        if (!CleanerService.canCleanExternalCache(activity)) {
            if (shouldShowRequestPermissionRationale(
                    PERMISSIONS_STORAGE.get(
                        0
                    )
                )
            ) {
                showStorageRationale()
            } else {
                requestPermissions(
                    PERMISSIONS_STORAGE,
                    REQUEST_STORAGE
                )
            }
        } else {
            mCleanerService?.cleanCache()
        }
    }

    private fun showStorageRationale() {
        val dialog: AlertDialog = AlertDialog.Builder(activity as MainActivity).create()
        dialog.setTitle(R.string.rationale_title)
        dialog.setMessage("Storage permission is needed to clean external cache.")
        dialog.setButton(
            AlertDialog.BUTTON_POSITIVE, getString(R.string.ok),
            DialogInterface.OnClickListener { dialog, which -> })
        dialog.show()
    }


    override fun onScanStarted(context: Context?) {
    }

    override fun onScanProgressUpdated(context: Context?, current: Int, max: Int) {
    }

    override fun onScanCompleted(context: Context?, apps: MutableList<AppsListItem>?) {
    }

    override fun onCleanStarted(context: Context?) {
    }

    override fun onCleanCompleted(context: Context?, succeeded: Boolean) {
        if (succeeded && activity != null && !mAlreadyCleaned &&
            mSharedPreferences!!.getBoolean(mExitAfterCleanKey, false)
        ) {
//            activity?.finish()
        }
    }
}