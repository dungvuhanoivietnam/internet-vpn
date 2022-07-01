package com.example.wise_memory_optimizer.ui.battery

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.wise_memory_optimizer.MainActivity
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.databinding.FragmentBatteryOptimizingBinding
import com.example.wise_memory_optimizer.ui.battery.service.HawkHelper
import com.example.wise_memory_optimizer.ui.done.DoneFragment
import com.example.wise_memory_optimizer.ui.done.OptimizeType
import com.example.wise_memory_optimizer.utils.ChargeUtils
import com.example.wise_memory_optimizer.utils.NavigationUtils
import com.example.wise_memory_optimizer.utils.Utils
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class BatteryOptimizingFragment : Fragment() {

    private var loadAnimation: Animation? = null
    private var mTextSwitchingHandler: Handler? = null
    private var mTextSwitchingRunnable: Runnable? = null
    private lateinit var binding: FragmentBatteryOptimizingBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBatteryOptimizingBinding.inflate(inflater)
        if (Utils.checkSystemWritePermission(activity)) {
            try {
//                if (Settings.System.getInt((activity as MainActivity).contentResolver, "screen_brightness_mode") == 1) {
                Settings.System.putInt(
                    (activity as MainActivity).contentResolver,
                    "screen_brightness_mode",
                    0
                )
                optimizeing()
//                } else { Settings.System.putInt((activity as MainActivity).contentResolver, "screen_brightness_mode", 1)
//                }
            } catch (unused: Settings.SettingNotFoundException) {
            }
        } else {
            Utils.openAndroidPermissionsMenu(activity as MainActivity)
        }
        setAnimation()
        updateTextOptimizing()
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
                            binding.icPin.setImageDrawable(
                                (activity as? MainActivity)?.resources?.getDrawable(
                                    R.drawable.ic_pin_1
                                )
                            )
                        }
                        1 -> {
                            binding.tvOptimizing.text =
                                (activity as? MainActivity)?.resources?.getString(R.string.optimizing1)
                            binding.icPin.setImageDrawable(
                                (activity as? MainActivity)?.resources?.getDrawable(
                                    R.drawable.ic_pin_2
                                )
                            )
                        }
                        2 -> {
                            binding.tvOptimizing.text =
                                (activity as? MainActivity)?.resources?.getString(R.string.optimizing2)
                            binding.icPin.setImageDrawable(
                                (activity as? MainActivity)?.resources?.getDrawable(
                                    R.drawable.ic_pin_3
                                )
                            )
                        }
                        3 -> {
                            binding.icPin.setImageDrawable(
                                (activity as? MainActivity)?.resources?.getDrawable(
                                    R.drawable.ic_pin_4
                                )
                            )
                            binding.tvOptimizing.text =
                                (activity as? MainActivity)?.resources?.getString(R.string.optimizing3)
                        }
                        4 -> {
                            binding.icPin.setImageDrawable(
                                (activity as? MainActivity)?.resources?.getDrawable(
                                    R.drawable.ic_pin_5
                                )
                            )

                        }
                    }
                    if (loadingIndex == 20) {
                        clearMem()
                    } else if (loadingIndex == 40) {
                        offBluetooth()

                    } else if (loadingIndex == 60) {
                        offRotate()
                    } else if (loadingIndex == 80) {
                        reduceBrightness()
                    }
                    if (loadingIndex == 100) {
                        val executorService: ExecutorService = Executors.newSingleThreadExecutor()
                        val longRunningTaskFuture = executorService.submit(mTextSwitchingRunnable);
                        longRunningTaskFuture.cancel(true);
                        val bundle = Bundle().apply {
                            putInt(DoneFragment.TYPE_OPTIMIZE, OptimizeType.PIN.value)
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

    fun optimizeing() {
        executeTaskAfter(Runnable { clearMem() }, 500)
        executeTaskAfter(Runnable { reduceBrightness() }, 1000)
        executeTaskAfter(Runnable { offBluetooth() }, 1500)
        executeTaskAfter(Runnable { offRotate() }, 2000)
        executeTaskAfter(Runnable {
            loadAnimation!!.cancel()
        }, 2500)
    }

    private fun executeTaskAfter(runnable: Runnable, mls: Int) {
        Handler().postDelayed(runnable, mls.toLong())
    }

    private fun clearMem() {
        if (HawkHelper.isClearMem()) {
            ChargeUtils.clearMem(context)
        }
    }


    private fun offBluetooth() {
        if (HawkHelper.isBlueTooth()) {
//            showTextWhenOptimize(R.string.function_off_bluetooth);
            ChargeUtils.offBluetooth(context)
        }
    }


    private fun offRotate() {
        if (HawkHelper.isRotate()) {
//            showTextWhenOptimize(R.string.function_off_rotate);
            ChargeUtils.offRotate(context)
        }
    }

    private fun reduceBrightness() {
        if (HawkHelper.isBrightness()) {
//            showTextWhenOptimize(R.string.function_min_brightness);
            ChargeUtils.reduceBrightness(context)
            Toast.makeText(context, resources.getString(R.string.kill_process), Toast.LENGTH_SHORT)
                .show()
        }
        HawkHelper.setKeyCountRate(HawkHelper.getKeyCountRate() + 1)
    }
}