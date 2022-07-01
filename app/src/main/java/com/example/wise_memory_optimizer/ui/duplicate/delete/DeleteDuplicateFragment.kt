package com.example.wise_memory_optimizer.ui.duplicate.delete

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.wise_memory_optimizer.R
import com.example.wise_memory_optimizer.constants.Constants
import com.example.wise_memory_optimizer.custom.roundimage.RoundedImageView
import com.example.wise_memory_optimizer.databinding.FragmentDeleteDuplicateBinding
import com.example.wise_memory_optimizer.model.file.Duplicate
import com.example.wise_memory_optimizer.model.file.LowQualityFile
import com.example.wise_memory_optimizer.utils.NavigationUtils
import com.example.wise_memory_optimizer.utils.StorageUtils
import com.google.android.material.card.MaterialCardView
import java.io.File
import java.io.Serializable


class DeleteDuplicateFragment : Fragment() {

    private var _binding: FragmentDeleteDuplicateBinding? = null
    private var duplicateViewModel: DeleteDuplicateViewModel? = null

    // onDestroyView.
    private val binding get() = _binding!!
    private var totalFileDup: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        duplicateViewModel =
            ViewModelProvider(this).get(DeleteDuplicateViewModel::class.java)

        _binding = FragmentDeleteDuplicateBinding.inflate(inflater, container, false)
        val root: View = binding.root

        loadFiles()

        binding.tvAvailable.text = StorageUtils.freeSpace
        binding.tvCountDuplicate.text = String.format("%.1f", StorageUtils.usagePercentage).replace(",",".")
        binding.tvTotal.text = StorageUtils.totalSpace

        binding.stackedHorizontalProgressBar.progress = StorageUtils.usagePercentage.toInt()
        binding.llToolbar.toolbarTitle.text = getString(R.string.tv_title_delete_duplicate)
        binding.llToolbar.ivBack.setOnClickListener {
            NavigationUtils.back(binding.llToolbar.ivBack)
        }

        return root
    }

    private fun loadFiles() {
        context?.let {
            var storagePaths = StorageUtils.getStorageDirectories(it)
            for (path in storagePaths!!) {
                var storage = File(path)
                duplicateViewModel?.getAllFiles(storage)
                    ?.observe(viewLifecycleOwner) { files ->

                        duplicateViewModel?.getDuplicateFiles(files)
                            ?.observe(viewLifecycleOwner) { fileDupImage ->

                                val filterAllImages = files!!.filter { s ->
                                    s.extension.contains(Constants.KEY_EXT_JPG) || s.extension.contains(
                                        Constants.KEY_EXT_PNG
                                    ) || s.extension.contains(Constants.KEY_EXT_JPEG)
                                }

                                duplicateViewModel?.getLowQualityFiles(filterAllImages)
                                    ?.observe(viewLifecycleOwner) { qualityFiles ->

                                        val filterDupImages = fileDupImage!!.filter { s ->
                                            s!!.extendsion.contains(Constants.KEY_EXT_JPG) || s.extendsion.contains(
                                                Constants.KEY_EXT_PNG
                                            ) || s.extendsion.contains(Constants.KEY_EXT_JPEG)
                                        }

                                        val filterDupVideo = fileDupImage.filter { s ->
                                            s!!.extendsion.contains(Constants.KEY_EXT_MP4) || s.extendsion.contains(
                                                Constants.KEY_EXT_3GP
                                            )
                                        }

                                        val filterDupFiles = fileDupImage.filter { s ->
                                            s!!.extendsion.contains(Constants.KEY_EXT_TXT) || s.extendsion.contains(
                                                Constants.KEY_EXT_DOC
                                            ) || s.extendsion.contains(
                                                Constants.KEY_EXT_DOCX
                                            ) || s.extendsion.contains(Constants.KEY_EXT_PDF)
                                        }

                                        Handler(Looper.getMainLooper()).postDelayed({
                                            context?.let {
                                                binding.progressBar.visibility = GONE
                                                binding.nestRoot.visibility = VISIBLE

                                                showAllFileDup(
                                                    filterDupVideo,
                                                    binding.rlSameVideoEmpty,
                                                    binding.llAllSameVideo,
                                                    binding.thumVideo1,
                                                    binding.thumVideo2,
                                                    binding.thumVideo3,
                                                    binding.thumVideo4,
                                                    binding.tvTotalSameVideo,
                                                    binding.tvTotalSizeDupVideo,
                                                    binding.cardVideo3,
                                                    binding.cardVideo4
                                                )
                                                qualityFiles?.let { it1 -> showPhotoLowQuality(it1) }
                                                showTotalSameFile(filterDupFiles)
                                                showAllFileDup(
                                                    filterDupImages,
                                                    binding.rlSamePhotoEmpty,
                                                    binding.llAllSamePhoto,
                                                    binding.rvImage1,
                                                    binding.rvImage2,
                                                    binding.rvImage3,
                                                    binding.rvImage4,
                                                    binding.tvTotalSamePhoto,
                                                    binding.tvTotalSizeDupImage,
                                                    binding.cardImage3,
                                                    binding.cardImage4
                                                )

                                                binding.llDeleteImage.visibility = VISIBLE
                                                binding.llDeleteImage.setOnClickListener {
                                                    navigateView(
                                                        Constants.KEY_DELETE_IMAGE,
                                                        filterDupImages,
                                                        binding.llDeleteImage,
                                                        R.id.action_delete_duplicate_to_same_photo
                                                    )
                                                }

                                                binding.llDeleteFile.setOnClickListener {
                                                    navigateView(
                                                        Constants.KEY_DUPLICATE_FILES,
                                                        filterDupFiles,
                                                        binding.llDeleteFile,
                                                        R.id.action_delete_duplicate_to_same_file
                                                    )
                                                }

                                                binding.llDeleteVideo.setOnClickListener {
                                                    navigateView(
                                                        Constants.KEY_DUPLICATE_VIDEO,
                                                        filterDupVideo,
                                                        binding.llDeleteVideo,
                                                        R.id.action_delete_duplicate_to_same_video
                                                    )
                                                }

                                                binding.llAllSamePhoto.setOnClickListener {
                                                    navigateView(
                                                        Constants.KEY_DELETE_IMAGE,
                                                        filterDupImages,
                                                        binding.llAllSamePhoto,
                                                        R.id.action_delete_duplicate_to_same_photo
                                                    )
                                                }

                                                binding.llAllLowQuality.setOnClickListener {
                                                    qualityFiles?.let { it1 ->
                                                        navigateLowQuality(
                                                            Constants.KEY_LOW_QUALITY,
                                                            it1,
                                                            binding.llAllLowQuality,
                                                            R.id.action_delete_duplicate_to_low_quality
                                                        )
                                                    }
                                                }


                                                binding.llAllSameVideo.setOnClickListener {
                                                    navigateView(
                                                        Constants.KEY_DUPLICATE_VIDEO,
                                                        filterDupVideo,
                                                        binding.llAllSameVideo,
                                                        R.id.action_delete_duplicate_to_same_video
                                                    )
                                                }

                                                binding.llSameFile.setOnClickListener {
                                                    navigateView(
                                                        Constants.KEY_DUPLICATE_FILES,
                                                        filterDupFiles,
                                                        binding.llSameFile,
                                                        R.id.action_delete_duplicate_to_same_file
                                                    )
                                                }


                                            }
                                        }, 12000)
                                    }
                            }


                    }
            }

        }
    }

    private fun navigateView(key: String, filterDupFiles: List<Duplicate?>, view: View, action: Int) {
        var bundle = Bundle()
        bundle.putSerializable(
            key,
            filterDupFiles as Serializable
        )
        NavigationUtils.navigate(
            view,
            action,
            bundle
        )
    }

    private fun navigateLowQuality(key: String, lowQualitys: MutableList<LowQualityFile?>, view: View, action: Int) {
        var bundle = Bundle()
        bundle.putSerializable(
            key,
            lowQualitys as Serializable
        )
        NavigationUtils.navigate(
            view,
            action,
            bundle
        )
    }

    private fun showTotalSameFile(sameFiles: List<Duplicate?>) {
        var lstLenghtSameFile: MutableList<Long> = ArrayList()
        for (i in sameFiles.indices) {
            var d = sameFiles[i]!!.duplicateFiles.map { dup ->
                dup?.file?.length()
            } as MutableList<Long>
            lstLenghtSameFile.addAll(d)
        }

        if (lstLenghtSameFile.isEmpty()) {
            binding.tvTotalSameFile.text = "0"
        } else {
            binding.tvTotalSameFile.text =
                StorageUtils.convertBytes(lstLenghtSameFile.sum())
            binding.tvTotalSizeDupFile.text =
                StorageUtils.convertBytes(lstLenghtSameFile.sum())
        }
    }

    private fun showPhotoLowQuality(file: MutableList<LowQualityFile?>) {
        if (file.isEmpty()) {
            binding.tvTotalLowQuality.text = "0"
            binding.llAllLowQuality.visibility = GONE
            binding.rlAllLowQualityEmpty.visibility = VISIBLE
        } else {
            binding.llAllLowQuality.visibility = VISIBLE
            binding.rlAllLowQualityEmpty.visibility = GONE
            if (file.size == 1) {
                binding.cardLowQuality2.visibility = GONE
                binding.cardLowQuality1.visibility = GONE
                binding.cardLowQuality4.visibility = GONE
                binding.cardLowQuality3.visibility = VISIBLE
                context?.let {
                    Glide.with(it)
                        .load(file[0]?.file)
                        .into(binding.rvLowQuality3)
                }
            } else if (file.size == 2) {
                binding.cardLowQuality3.visibility = GONE
                binding.cardLowQuality4.visibility = GONE
                binding.cardLowQuality1.visibility = VISIBLE
                binding.cardLowQuality2.visibility = VISIBLE
                context?.let {
                    Glide.with(it)
                        .load(file[0]?.file)
                        .into(binding.rvLowQuality1)

                    Glide.with(it)
                        .load(file[1]?.file)
                        .into(binding.rvLowQuality2)
                }
            } else if (file.size == 3) {
                binding.cardLowQuality4.visibility = GONE
                binding.cardLowQuality1.visibility = VISIBLE
                binding.cardLowQuality2.visibility = VISIBLE
                binding.cardLowQuality3.visibility = VISIBLE
                context?.let {
                    Glide.with(it)
                        .load(file[0]?.file)
                        .into(binding.rvLowQuality1)

                    Glide.with(it)
                        .load(file[1]?.file)
                        .into(binding.rvLowQuality2)

                    Glide.with(it)
                        .load(file[2]?.file)
                        .into(binding.rvLowQuality3)
                }
            } else {
                binding.cardLowQuality2.visibility = VISIBLE
                binding.cardLowQuality3.visibility = VISIBLE
                binding.cardLowQuality4.visibility = VISIBLE
                context?.let {
                    Glide.with(it)
                        .load(file[0]?.file)
                        .into(binding.rvLowQuality1)

                    Glide.with(it)
                        .load(file[1]?.file)
                        .into(binding.rvLowQuality2)

                    Glide.with(it)
                        .load(file[2]?.file)
                        .into(binding.rvLowQuality3)

                    Glide.with(it)
                        .load(file[3]?.file)
                        .into(binding.rvLowQuality4)
                }
            }

            var lstLenghtDup: MutableList<Long> = ArrayList()
            for (i in file.indices) {
                var d = file[i]!!.file!!.length()
                lstLenghtDup.add(d)
            }

            binding.tvTotalLowQuality.text =
                StorageUtils.convertBytes(lstLenghtDup.sum())
            totalFileDup += lstLenghtDup.sum()
        }
    }

    private fun showAllFileDup(
        dups: List<Duplicate?>,
        vEmpty: View,
        viewData: View,
        rv1: RoundedImageView,
        rv2: RoundedImageView,
        rv3: RoundedImageView,
        rv4: RoundedImageView,
        viewTotal: TextView,
        viewTotalSize: TextView,
        card3: MaterialCardView,
        card4: MaterialCardView
    ) {
        if (dups.isEmpty()) {
            viewTotal.text = "0"
            vEmpty.visibility = VISIBLE
            viewData.visibility = GONE
        } else {
            vEmpty.visibility = GONE
            viewData.visibility = VISIBLE
            if (dups.size > 1) {
                context?.let {
                    Glide.with(it)
                        .load(dups[0]!!.duplicateFiles[0]?.file)
                        .into(rv1)

                    Glide.with(it)
                        .load(dups[0]!!.duplicateFiles[1]?.file)
                        .into(rv2)

                    Glide.with(it)
                        .load(dups[1]!!.duplicateFiles[0]?.file)
                        .into(rv3)

                    Glide.with(it)
                        .load(dups[1]!!.duplicateFiles[1]?.file)
                        .into(rv4)

                }
            } else {
                context?.let {
                    Glide.with(it)
                        .load(dups[0]!!.duplicateFiles[0]?.file)
                        .into(rv1)

                    Glide.with(it)
                        .load(dups[0]!!.duplicateFiles[1]?.file)
                        .into(rv2)
                    card3.visibility = GONE
                    card4.visibility = GONE
                }
            }

            var lstLenghtDup: MutableList<Long> = ArrayList()
            for (i in dups.indices) {
                var d = dups[i]!!.duplicateFiles.map { dup ->
                    dup?.file?.length()
                } as MutableList<Long>
                lstLenghtDup.addAll(d)
            }

            viewTotal.text =
                StorageUtils.convertBytes(lstLenghtDup.sum())
            viewTotalSize.text = StorageUtils.convertBytes(lstLenghtDup.sum())
            totalFileDup += lstLenghtDup.sum()

            var dupPercentage = StorageUtils.divide(totalFileDup, StorageUtils.totalByte)
            binding.stackedHorizontalProgressBar.secondaryProgress = dupPercentage.toInt()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}