package com.example.wise_memory_optimizer.ui.duplicate

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.constants.Constants
import com.example.wise_memory_optimizer.databinding.FragmentDuplicatePhotoBinding
import com.example.wise_memory_optimizer.utils.NavigationUtils
import com.example.wise_memory_optimizer.utils.StorageUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip
import java.io.File
import java.io.Serializable

class DuplicatePhotoFragment : Fragment() {

    private var _binding: FragmentDuplicatePhotoBinding? = null
    private var duplicateViewModel: DuplicatePhotoViewModel? = null

    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        duplicateViewModel =
            ViewModelProvider(this).get(DuplicatePhotoViewModel::class.java)

        _binding = FragmentDuplicatePhotoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        activity?.runOnUiThread {
            binding.tvCountDuplicate.text =
                String.format("%.1f", StorageUtils.usagePercentage).replace(",",".")
        }

        Log.e("totalSpace", StorageUtils.totalSpace)
        context?.let { Glide.with(it).asGif().load(R.drawable.ic_gift).into(binding.ivGift) }

        Handler(Looper.getMainLooper()).postDelayed({
        context?.let {
            var isCheck =
                StorageUtils.isStorageAccessPermission(it, this@DuplicatePhotoFragment)
            if (isCheck) {
                navigateDuplicate()
            }
        }
        }, 2000)

        return root
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode === 1) {
            if (grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                navigateDuplicate()
            }
        }

    }

    private fun navigateDuplicate() {
        findNavController().navigate(R.id.action_duplicate_to_delete)
//        NavigationUtils.navigate(binding.root, R.id.action_duplicate_to_delete, null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            1 -> if (resultCode === Activity.RESULT_OK) {
                var fileUri = data?.data
                fileUri?.let {
                    context?.contentResolver?.takePersistableUriPermission(
                        it, Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                                or Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}