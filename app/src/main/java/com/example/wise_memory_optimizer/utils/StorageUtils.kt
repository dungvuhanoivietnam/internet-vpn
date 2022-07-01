package com.example.wise_memory_optimizer.utils

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.wise_memory_optimizer.model.file.Duplicate
import com.example.wise_memory_optimizer.model.file.DuplicateFile
import com.example.wise_memory_optimizer.model.file.DuplicateKey
import com.example.wise_memory_optimizer.model.file.LowQualityFile
import java.io.File
import java.lang.Exception
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.TimeUnit


class StorageUtils {

    companion object {

        var extAllFile = arrayOf(
            ".mp4",
            ".3gp",
            ".png",
            ".jpg",
            ".jpeg",
            ".txt",
            ".docx",
            ".doc",
            ".pdf"
        )

        // Primary physical SD-CARD (not emulated)
        private val EXTERNAL_STORAGE = System.getenv("EXTERNAL_STORAGE")

        // All Secondary SD-CARDs (all exclude primary) separated by File.pathSeparator, i.e: ":", ";"
        private val SECONDARY_STORAGES = System.getenv("SECONDARY_STORAGE")

        // Primary emulated SD-CARD
        private val EMULATED_STORAGE_TARGET = System.getenv("EMULATED_STORAGE_TARGET")

        // PhysicalPaths based on phone model
        private val KNOWN_PHYSICAL_PATHS = arrayOf(
            "/storage/sdcard0",
            "/storage/sdcard1",  //Motorola Xoom
            "/storage/extsdcard",  //Samsung SGS3
            "/storage/sdcard0/external_sdcard",  //User request
            "/mnt/extsdcard",
            "/mnt/sdcard/external_sd",  //Samsung galaxy family
            "/mnt/sdcard/ext_sd",
            "/mnt/external_sd",
            "/mnt/media_rw/sdcard1",  //4.4.2 on CyanogenMod S3
            "/removable/microsd",  //Asus transformer prime
            "/mnt/emmc",
            "/storage/external_SD",  //LG
            "/storage/ext_sd",  //HTC One Max
            "/storage/removable/sdcard1",  //Sony Xperia Z1
            "/data/sdext",
            "/data/sdext2",
            "/data/sdext3",
            "/data/sdext4",
            "/sdcard1",  //Sony Xperia Z
            "/sdcard2",  //HTC One M8s
            "/storage/microsd" //ASUS ZenFone 2
        )


        //all loaded files will be here
        var allFileList: ArrayList<File> = ArrayList()

        var stat = StatFs(Environment.getExternalStorageDirectory().path)

        fun divide(a: Long, b: Long): Float {
            return a.toFloat() / b
        }

        //Get free Bytes...
        private val internalFreeSpace: Long
            get() =//Get free Bytes...
                stat.blockSizeLong * stat.availableBlocksLong

        val freeSpace: String
            get() = convertBytes(internalFreeSpace)

        //Get used Bytes
        private val usedSpace: Long
            get() =//Get used Bytes
                totalByte - internalFreeSpace
        val usagePercentage: Float
            get() = (divide(usedSpace, totalByte)) * 100

        //Get total Bytes
        val totalByte: Long
            get() =//Get total Bytes
                stat.blockSizeLong * stat.blockCountLong
        val totalSpace: String
            get() = convertBytes(totalByte)

        fun convertBytes(size: Long): String {
            val Kb = (1 * 1024).toLong()
            val Mb = Kb * 1024
            val Gb = Mb * 1024
            val Tb = Gb * 1024
            val Pb = Tb * 1024
            val Eb = Pb * 1024
            if (size < Kb) return floatForm(size.toDouble()) + " B"
            if (size in Kb until Mb) return floatForm(size.toDouble() / Kb) + " KB"
            if (size in Mb until Gb) return floatForm(size.toDouble() / Mb) + " MB"
            if (size in Gb until Tb) return floatForm(size.toDouble() / Gb) + " GB"
            if (size in Tb until Pb) return floatForm(size.toDouble() / Tb) + " TB"
            if (size in Pb until Eb) return floatForm(size.toDouble() / Pb) + " PB"
            return if (size >= Eb) floatForm(size.toDouble() / Eb) + " EB" else "anything..."
        }

        private fun floatForm(d: Double): String {
            return DecimalFormat("#.##").format(d)
        }


        fun loadDirectoryFiles(directory: File) {
            val fileList = directory.listFiles()
            if (fileList != null && fileList.isNotEmpty()) {
                for (i in fileList.indices) {
                    if (fileList[i].isDirectory) {
                        loadDirectoryFiles(fileList[i])
                    } else {
                        val name = fileList[i].name.lowercase(Locale.getDefault())
                        for (extension in extAllFile) {
                            //check the type of file
                            if (name.endsWith(extension)) {
                                allFileList.add(fileList[i])
                                //when we found file
                                break
                            }
                        }
                    }
                }
            }
        }

        fun getDurationMedia(context: Context?, file: File?): String {
            try {
                var retriever = MediaMetadataRetriever()
                retriever.setDataSource(context, Uri.fromFile(file))
                var time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                var duration = time?.toLong()
                retriever.release()

                return String.format(
                    "%d:%d",
                    duration?.let { TimeUnit.MILLISECONDS.toMinutes(it) },
                    duration?.let {
                        TimeUnit.MILLISECONDS.toSeconds(it) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(it)
                        )
                    }
                )

            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }

        fun getStorageDirectories(context: Context): Array<String?>? {
            // Final set of paths
            val availableDirectoriesSet: MutableSet<String> = HashSet()
            if (!TextUtils.isEmpty(EMULATED_STORAGE_TARGET)) {
                // Device has an emulated storage
                availableDirectoriesSet.add(getEmulatedStorageTarget())
            } else {
                // Device doesn't have an emulated storage
                availableDirectoriesSet.addAll(getExternalStorage(context)!!)
            }

            // Add all secondary storages
            Collections.addAll(availableDirectoriesSet, getAllSecondaryStorages().toString())
//            val storagesArray = arrayOfNulls<String>(availableDirectoriesSet.size)
            return availableDirectoriesSet.toTypedArray()
        }

        private fun getExternalStorage(context: Context): Set<String>? {
            val availableDirectoriesSet: MutableSet<String> = HashSet()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val files = getExternalFilesDirs(context, null)
                if (files != null) {
                    for (file in files) {
                        val applicationSpecificAbsolutePath = file.absolutePath
                        val rootPath = applicationSpecificAbsolutePath.substring(
                            0,
                            applicationSpecificAbsolutePath.indexOf("Android/data")
                        )
                        availableDirectoriesSet.add(rootPath)
                    }
                }
            } else {
                if (TextUtils.isEmpty(EXTERNAL_STORAGE)) {
                    availableDirectoriesSet.addAll(getAvailablePhysicalPaths()!!)
                } else {
                    // Device has physical external storage; use plain paths.
                    availableDirectoriesSet.add(EXTERNAL_STORAGE)
                }
            }
            return availableDirectoriesSet
        }

        private fun getEmulatedStorageTarget(): String {
            var rawStorageId = ""
            val path = Environment.getExternalStorageDirectory().absolutePath
            val folders = path.split(File.separator).toTypedArray()
            val lastSegment = folders[folders.size - 1]
            if (!TextUtils.isEmpty(lastSegment) && TextUtils.isDigitsOnly(lastSegment)) {
                rawStorageId = lastSegment
            }
            return if (TextUtils.isEmpty(rawStorageId)) {
                EMULATED_STORAGE_TARGET
            } else {
                EMULATED_STORAGE_TARGET + File.separator + rawStorageId
            }
        }

        private fun getAllSecondaryStorages(): Array<String?>? {
            return if (!TextUtils.isEmpty(SECONDARY_STORAGES)) {
                // All Secondary SD-CARDs split into array
                SECONDARY_STORAGES.split(File.pathSeparator).toTypedArray()
            } else arrayOfNulls(0)
        }


        private fun getAvailablePhysicalPaths(): List<String>? {
            val availablePhysicalPaths: MutableList<String> = ArrayList()
            for (physicalPath in KNOWN_PHYSICAL_PATHS) {
                val file = File(physicalPath)
                if (file.exists()) {
                    availablePhysicalPaths.add(physicalPath)
                }
            }
            return availablePhysicalPaths
        }

        private fun getExternalFilesDirs(context: Context, type: String?): Array<File>? {
            return if (Build.VERSION.SDK_INT >= 19) {
                context.getExternalFilesDirs(type)
            } else {
                context.getExternalFilesDir(type)?.let { arrayOf(it) }
            }
        }


        fun isStorageAccessPermission(context: Context, fragment: Fragment): Boolean {
            return if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                fragment.requestPermissions(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE
                    ),
                    1
                )
                try {
                    val intent = Intent()
                    intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                    val uri = Uri.fromParts("package", context.packageName, null)
                    intent.data = uri
                    context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
                false
            } else {
                true
            }
        }

        fun getDuplicateFiles(allFile: List<File?>?): List<Duplicate?>? {
            val hashmap: HashMap<DuplicateKey, Duplicate> = HashMap()
            val duplicateHashSet: HashMap<DuplicateKey, Duplicate> = HashMap()
            Log.e("Start", "Start")
            for (i in 0 until allFile!!.size) {
                val lenght: Long = allFile[i]!!.length()
                val ext: String = allFile[i]!!.extension
                val key = DuplicateKey(lenght, ext)
                if (hashmap.containsKey(key)) {
                    val original = hashmap[key]
                    if (duplicateHashSet.containsKey(key)) {
                        var fileList: MutableList<DuplicateFile?>
                        if (original != null) {
                            fileList = original.duplicateFiles
                        } else {
                            fileList = ArrayList()
                        }
                        fileList.add(
                            DuplicateFile(
                                allFile[i]!!,
                                DateUtils.getDateFormatByLong(allFile[i]!!.lastModified())
                            )
                        )
                        duplicateHashSet[key] = Duplicate(ext, fileList)
                    } else {
                        val fileList: MutableList<DuplicateFile?> = ArrayList()
                        if (original?.duplicateFiles == null) {
                            original?.duplicateFiles = ArrayList()
                        }
                        fileList.add(
                            DuplicateFile(
                                original?.duplicateFiles?.get(0)?.file,
                                DateUtils.getDateFormatByLong(allFile[i]!!.lastModified())
                            )
                        )
                        fileList.add(
                            DuplicateFile(
                                allFile[i]!!,
                                DateUtils.getDateFormatByLong(allFile[i]!!.lastModified())
                            )
                        )
                        hashmap[key] = Duplicate(ext, fileList)
                        duplicateHashSet[key] = Duplicate(ext, fileList)
                    }
                } else {
                    val fileList: MutableList<DuplicateFile?> = ArrayList()
                    fileList.add(
                        DuplicateFile(
                            allFile[i]!!,
                            DateUtils.getDateFormatByLong(allFile[i]!!.lastModified())
                        )
                    )
                    hashmap[key] = Duplicate(ext, fileList)
                }
            }

            Log.e("Start", "End")
            var list: List<Duplicate?>? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                list = ArrayList(duplicateHashSet.values)
            }
            Log.e("list", list.toString())
            return list
        }


        fun getLowQualityFiles(files: List<File?>): MutableList<LowQualityFile?> {
            var lowQualityFiles: MutableList<LowQualityFile?> = ArrayList()

            val iterator = files.iterator()
            while (iterator.hasNext()) {
                val item = iterator.next()
                var fileSize = (item!!.length() / 1024).toInt()
                if (fileSize < 100) {
                    lowQualityFiles.add(
                        LowQualityFile(
                            item,
                            DateUtils.getDateFormatByLong(item.lastModified())
                        )
                    )
                }
            }

            return lowQualityFiles
        }


        fun mapDuplicateFile(files: MutableList<DuplicateFile?>): HashMap<String, MutableList<DuplicateFile?>> {
            var hashmap: HashMap<String, MutableList<DuplicateFile?>> = HashMap()

            for (i in 0 until files.size) {
                if (hashmap.containsKey(files[i]!!.dateTime)) {
                    hashmap[files[i]!!.dateTime]!!.add(files[i])
                } else {
                    var newList: MutableList<DuplicateFile?> = ArrayList()
                    newList.add(files[i])
                    hashmap[files[i]!!.dateTime] = newList
                }
            }

            return hashmap
        }


        fun mapLowQualityFile(files: MutableList<LowQualityFile?>): HashMap<String, MutableList<LowQualityFile?>> {
            var hashmap: HashMap<String, MutableList<LowQualityFile?>> = HashMap()

            for (i in 0 until files.size) {
                if (hashmap.containsKey(files[i]!!.dateTime)) {
                    hashmap[files[i]!!.dateTime]!!.add(files[i])
                } else {
                    var newList: MutableList<LowQualityFile?> = ArrayList()
                    newList.add(files[i])
                    hashmap[files[i]!!.dateTime] = newList
                }
            }

            return hashmap
        }

    }
}
