package com.example.wise_memory_optimizer.ui.cpu.optimizing

import android.content.BroadcastReceiver
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.wise_memory_optimizer.MainActivity
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.databinding.FragmentCpuOptimizingBinding
import com.example.wise_memory_optimizer.ui.done.DoneFragment.Companion.TYPE_OPTIMIZE
import com.example.wise_memory_optimizer.ui.done.OptimizeType
import com.example.wise_memory_optimizer.utils.NavigationUtils
import com.example.wise_memory_optimizer.utils.Utils
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CpuOptimizingFragment : Fragment() {
    private val viewModel by lazy {
        ViewModelProvider(this).get(CpuOptimizingModel::class.java)
    }

    private val loadAnimation by lazy {
        AnimationUtils.loadAnimation(context, R.anim.rotate)
    }

    private lateinit var binding: FragmentCpuOptimizingBinding

    private var mTextSwitchingHandler: Handler? = null
    private var mTextSwitchingRunnable: Runnable? = null
    private lateinit var cpuReceiver: BroadcastReceiver

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCpuOptimizingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Utils.checkSystemWritePermission(activity)) {
            try {
//                if (Settings.System.getInt((activity as MainActivity).contentResolver, "screen_brightness_mode") == 1) {
                Settings.System.putInt(
                    (activity as MainActivity).contentResolver,
                    "screen_brightness_mode",
                    0
                )
                optimizing()
//                } else { Settings.System.putInt((activity as MainActivity).contentResolver, "screen_brightness_mode", 1)
//                }
            } catch (unused: Settings.SettingNotFoundException) {
            }
        } else {
            Utils.openAndroidPermissionsMenu(activity as MainActivity)
        }
        binding.ivProgress.startAnimation(loadAnimation)
        updateTextOptimizing()
    }

    fun optimizing() {
        executeTaskAfter({ viewModel.clearMem(requireContext()) }, 500)
        executeTaskAfter({ viewModel.reduceBrightness(requireContext()) }, 1000)
        executeTaskAfter({ viewModel.offBluetooth(requireContext()) }, 1500)
        executeTaskAfter({ viewModel.offRotate(requireContext()) }, 2000)
        executeTaskAfter({
            loadAnimation.cancel()
        }, 2500)
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
                    when (loadingIndex) {
                        20 -> {
                            viewModel.clearMem(requireContext())
                        }
                        40 -> {
                            viewModel.offBluetooth(requireContext())

                        }
                        60 -> {
                            viewModel.offRotate(requireContext())
                        }
                        80 -> {
                            viewModel.reduceBrightness(requireContext())
                        }
                    }
                    if (loadingIndex == 100) {
                        val executorService: ExecutorService = Executors.newSingleThreadExecutor()
                        val longRunningTaskFuture = executorService.submit(mTextSwitchingRunnable);
                        longRunningTaskFuture.cancel(true)
                        val bundle = Bundle().apply {
                            putInt(TYPE_OPTIMIZE, OptimizeType.CPU.value)
                        }
                        NavigationUtils.navigate(binding.root, R.id.done_optimizer, bundle)
                        Toast.makeText(
                            (activity as? MainActivity),
                            (activity as? MainActivity)?.resources?.getText(R.string.done),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    binding.txtPercentOptimizer.text = "$loadingIndex%"
                    loadingIndex++
                    mTextSwitchingHandler?.postDelayed(this, 200)
                }
            }
            mTextSwitchingHandler?.post(mTextSwitchingRunnable!!)
        }
    }


    private fun executeTaskAfter(runnable: Runnable, mls: Int) {
        Handler().postDelayed(runnable, mls.toLong())
    }
}