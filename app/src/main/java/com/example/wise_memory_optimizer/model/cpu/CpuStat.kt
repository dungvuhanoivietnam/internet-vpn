package com.example.wise_memory_optimizer.model.cpu

import android.util.Config.LOGV
import android.util.Log
import java.io.FileNotFoundException
import java.io.IOException
import java.io.RandomAccessFile

class CpuStat {
    companion object {

        private var statFile: RandomAccessFile? = null
        private var mCpuInfoTotal: CpuInfo? = null
        private var mCpuInfoList: ArrayList<CpuInfo>? = null
        fun update() {
            try {
                createFile()
                parseFile()
                closeFile()
            } catch (e: FileNotFoundException) {
                statFile = null
                Log.e(TAG, "cannot open /proc/stat: $e")
            } catch (e: IOException) {
                Log.e(TAG, "cannot close /proc/stat: $e")
            }
        }

        @Throws(FileNotFoundException::class)
        private fun createFile() {
            statFile = RandomAccessFile("/proc/stat", "r")
        }

        @Throws(IOException::class)
        fun closeFile() {
            if (statFile != null) statFile?.close()
        }

        private fun parseFile() {
            if (statFile != null) {
                try {
                    statFile?.seek(0)
                    var cpuLine = ""
                    var cpuId = -1
                    do {
                        cpuLine = statFile?.readLine().toString()
                        parseCpuLine(cpuId, cpuLine)
                        cpuId++
                    } while (cpuLine != null)
                } catch (e: IOException) {
                    Log.e(TAG, "Ops: $e")
                }
            }
        }

        private fun parseCpuLine(cpuId: Int, cpuLine: String?) {
            if (cpuLine != null && cpuLine.isNotEmpty()) {
                val parts = cpuLine.split("[ ]+").toTypedArray()
                val cpuLabel = "cpu"
                if (parts[0].indexOf(cpuLabel) != -1) {
                    createCpuInfo(cpuId, parts)
                }
            } else {
                Log.e(TAG, "unable to get cpu line")
            }
        }

        private fun createCpuInfo(cpuId: Int, parts: Array<String>) {
            if (cpuId == -1) {
                if (mCpuInfoTotal == null) mCpuInfoTotal = CpuInfo()
                mCpuInfoTotal!!.update(parts)
            } else {
                if (mCpuInfoList == null) mCpuInfoList = ArrayList()
                if (cpuId < mCpuInfoList?.size!!) mCpuInfoList!![cpuId].update(parts) else {
                    val info = CpuInfo()
                    info.update(parts)
                    mCpuInfoList!!.add(info)
                }
            }
        }

        fun getCpuUsage(cpuId: Int): Int {
            update()
            var usage = 0
            if (mCpuInfoList != null) {
                var cpuCount: Int = mCpuInfoList!!.size
                if (cpuCount > 0) {
                    cpuCount--
                    usage = if (cpuId == cpuCount) { // -1 total cpu usage
                        mCpuInfoList!![0].usage
                    } else {
                        if (cpuId <= cpuCount) mCpuInfoList!![cpuId].usage else -1
                    }
                }
            }
            return usage
        }

        val totalCpuUsage: Int
            get() {
                update()
                var usage = 0
                if (mCpuInfoTotal != null) usage = mCpuInfoTotal?.usage!!
                return usage
            }

        override fun toString(): String {
            update()
            val buf = StringBuffer()
            if (mCpuInfoTotal != null) {
                buf.append("Cpu Total : ")
                buf.append(mCpuInfoTotal?.usage)
                buf.append("%")
            }
            if (mCpuInfoList != null) {
                for (i in 0 until mCpuInfoList!!.size) {
                    val info = mCpuInfoList!![i]
                    buf.append(" Cpu Core($i) : ")
                    buf.append(info.usage)
                    buf.append("%")
                    info.usage
                }
            }
            return buf.toString()
        }

        class CpuInfo {
            var usage = 0
            var mLastTotal: Long = 0
            var mLastIdle: Long = 0
            fun update(parts: Array<String>) {
                // the columns are:
                //
                //      0 "cpu": the string "cpu" that identifies the line
                //      1 user: normal processes executing in user mode
                //      2 nice: niced processes executing in user mode
                //      3 system: processes executing in kernel mode
                //      4 idle: twiddling thumbs
                //      5 iowait: waiting for I/O to complete
                //      6 irq: servicing interrupts
                //      7 softirq: servicing softirqs
                //
                val idle = parts[4].toLong(10)
                var total: Long = 0
                var head = true
                for (part in parts) {
                    if (head) {
                        head = false
                        continue
                    }
                    total += part.toLong(10)
                }
                val diffIdle = idle - mLastIdle
                val diffTotal = total - mLastTotal
                usage = ((diffTotal - diffIdle).toFloat() / diffTotal * 100).toInt()
                mLastTotal = total
                mLastIdle = idle
                Log.i(TAG, "CPU total=$total; idle=$idle; usage=$usage")
            }
        }

        private const val TAG = "CpuUsage"
    }
}