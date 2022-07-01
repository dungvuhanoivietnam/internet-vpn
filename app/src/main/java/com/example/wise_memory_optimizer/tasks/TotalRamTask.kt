package com.example.wise_memory_optimizer.tasks

import android.app.ActivityManager
import android.content.Context
import android.os.AsyncTask
import java.io.IOException
import java.io.RandomAccessFile
import java.text.DecimalFormat
import java.util.regex.Pattern

class TotalRamTask(private val mContext: Context, private val mDataCallBack: IRamTaskListener?) :
    AsyncTask<Void?, Int?, Boolean?>() {
    private var totalRam: Long = 0
    private var useRam: Long = 0
    private var percentRam = 60
    private var totalRamGb: String? = null
    private var freeRamGb: String? = null

    interface IRamTaskListener {
        fun onGetRamDataCompleted(freeRam: String?, totalRam: String?, percentRam: Int)
        fun onRamPercentUpdate(percentRam: Int)
    }

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
        values[0]?.let { mDataCallBack?.onRamPercentUpdate(it) }
    }

    override fun onPostExecute(aBoolean: Boolean?) {
        super.onPostExecute(aBoolean)
        mDataCallBack?.onGetRamDataCompleted(freeRamGb, totalRamGb, percentRam)
    }

    private fun getAvaiableRam(context: Context): Long {
        val mi = ActivityManager.MemoryInfo()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager?.getMemoryInfo(mi)
        return mi.availMem
    }// Streams.close(reader);// System.out.println("Ram : " + value);

    // totRam = totRam / 1024;
    // Get the Number value from the string
    private val totalRAM: Long
        private get() {
            var reader: RandomAccessFile? = null
            var load: String? = null
            val twoDecimalForm = DecimalFormat("#.##")
            var totRam: Long = 0
            try {
                reader = RandomAccessFile("/proc/meminfo", "r")
                load = reader.readLine()

                // Get the Number value from the string
                val p = Pattern.compile("(\\d+)")
                val m = p.matcher(load)
                var value: String? = ""
                while (m.find()) {
                    value = m.group(1)
                    // System.out.println("Ram : " + value);
                }
                reader.close()
                totRam = Integer.valueOf(value).toLong()
                // totRam = totRam / 1024;
            } catch (ex: IOException) {
                ex.printStackTrace()
            } finally {
                // Streams.close(reader);
            }
            return totRam * 1024
        }

    override fun doInBackground(vararg p0: Void?): Boolean? {
        totalRam = totalRAM
        val availableRam = getAvaiableRam(mContext)
        useRam = totalRam - availableRam
        percentRam = (useRam.toDouble() / totalRam.toDouble() * 100).toInt()
        if (percentRam > 100) {
            percentRam %= 100
        }
        for (i in 0..percentRam) {
            publishProgress(i)
            try {
                Thread.sleep(20)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        // total Ram and Free ram in GB
        val totalRamFloat = totalRam.toFloat() / (1024 * 1024 * 1024)
        totalRamGb = String.format("%.02f", totalRamFloat)
        val freeRamFloat = availableRam.toFloat() / (1024 * 1024 * 1024)
        freeRamGb = String.format("%.02f", totalRamFloat - freeRamFloat)
        return null
    }
}