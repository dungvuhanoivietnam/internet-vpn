package com.example.wise_memory_optimizer.ui.internet.check

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wise_memory_optimizer.model.location_speed_test.ErrorTesting
import fr.bmartel.speedtest.SpeedTestReport
import fr.bmartel.speedtest.SpeedTestSocket
import fr.bmartel.speedtest.inter.IRepeatListener
import fr.bmartel.speedtest.inter.ISpeedTestListener
import fr.bmartel.speedtest.model.SpeedTestError
import fr.bmartel.speedtest.model.UploadStorageType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CheckInternetSpeedViewModel : ViewModel() {

    private val _speedDownloadReport = MutableLiveData<SpeedTestReport>()
    val speedDownloadReport: LiveData<SpeedTestReport> = _speedDownloadReport

    private val _speedUploadReport = MutableLiveData<SpeedTestReport>()
    val speedUploadReport: LiveData<SpeedTestReport> = _speedUploadReport

    private val _reportTestDone = MutableLiveData<SpeedTestReport>()
    val reportTestDone: LiveData<SpeedTestReport> = _reportTestDone

    private val _pingValue = MutableLiveData<Float>()
    val pingValue: LiveData<Float> = _pingValue

    private val _speedTestProgressPercent = MutableLiveData<Float>()
    val speedTestProgressPercent: LiveData<Float> = _speedTestProgressPercent

    private val _speedTestErrorMessage = MutableLiveData<ErrorTesting>()
    val speedTestErrorMessage: LiveData<ErrorTesting> = _speedTestErrorMessage

    private var jobTestDownload : Job? = null
    private var jobTestUpload : Job? = null
    private var jobTestPing : Job? = null

    private val speedTestSocket = SpeedTestSocket().apply {
        setProxyServer(PROXY_SERVER)
        uploadStorageType = UploadStorageType.FILE_STORAGE
        socketTimeout = SOCKET_TIMEOUT
    }

    companion object {
        const val PROXY_SERVER = ""
        const val COMMAND = "/system/bin/ping -c 1 8.8.8.8"

        const val TEST_LINK_DOWNLOAD = "http://ipv4.ikoula.testdebit.info/10M.iso"

        /**
         * link test upload
         */
        const val TEST_LINK_UPLOAD = "http://ipv4.ikoula.testdebit.info/"


        /**
         * speed test duration set to 4s.
         */
        private const val SPEED_TEST_DURATION = 4000

        /**
         * amount of time between each speed test report set to 1s.
         */
        private const val REPORT_INTERVAL = 1000

        /**
         * set socket timeout to 2s.
         */
        const val SOCKET_TIMEOUT = 5000

        /**
         * upload 1Mo file size.
         */
        private const val FILE_SIZE = 1000000
    }

    init {
        startTestInternet()
    }


    private fun startTestInternet() {
        viewModelScope.launch(Dispatchers.IO) {
            // add a listener to wait for speed test completion and progress
            speedTestSocket.addSpeedTestListener(object : ISpeedTestListener {
                override fun onCompletion(report: SpeedTestReport) {
                    // called when download/upload is complete
                    println("[COMPLETED] rate in octet/s : " + report.transferRateOctet)
                    println("[COMPLETED] rate in bit/s   : " + report.transferRateBit)
                }

                override fun onError(speedTestError: SpeedTestError, errorMessage: String) {
                    _speedTestErrorMessage.postValue(ErrorTesting(
                        name = speedTestError.name,
                        ordinal = speedTestError.ordinal,
                        message = errorMessage
                    ))
                }

                override fun onProgress(percent: Float, report: SpeedTestReport) {
                    // called to notify download/upload progress
                    _speedTestProgressPercent.postValue(percent)
                    println("[PROGRESS] progress : $percent%")
                    println("[PROGRESS] rate in octet/s : " + report.transferRateOctet)
                    println("[PROGRESS] rate in bit/s   : " + report.transferRateBit)
                }
            })
        }
    }

    fun startTestDownload() {
        if (jobTestDownload?.isActive == true){
            jobTestDownload?.cancel()
        }
        jobTestDownload = viewModelScope.launch(Dispatchers.IO) {
            speedTestSocket.startDownloadRepeat(
                TEST_LINK_DOWNLOAD,
                SPEED_TEST_DURATION,
                REPORT_INTERVAL, object : IRepeatListener {
                    override fun onCompletion(report: SpeedTestReport) {
                        _reportTestDone.postValue(report)
                    }

                    override fun onReport(report: SpeedTestReport) {
                        _speedDownloadReport.postValue(report)
                    }
                })
        }
    }

    fun startTestUpload() {
        if (jobTestUpload?.isActive == true){
            jobTestUpload?.cancel()
        }
       jobTestUpload = viewModelScope.launch(Dispatchers.IO) {
            speedTestSocket.startUploadRepeat(
                TEST_LINK_UPLOAD,
                SPEED_TEST_DURATION,
                REPORT_INTERVAL,
                FILE_SIZE, object : IRepeatListener {
                    override fun onCompletion(report: SpeedTestReport) {
                        _reportTestDone.postValue(report)
                    }

                    override fun onReport(report: SpeedTestReport) {
                        _speedUploadReport.postValue(report)
                    }
                })
        }
    }


    fun getPing() {
        if (jobTestPing?.isActive == true){
            jobTestPing?.cancel()
        }

        jobTestPing = viewModelScope.launch(Dispatchers.IO) {
            val runTime = Runtime.getRuntime()
            try {
                val a = System.currentTimeMillis() % 1000
                val ipProcess = runTime.exec(COMMAND)
                ipProcess.waitFor()
                val ping = (System.currentTimeMillis() % 1000) - a
                _pingValue.postValue(ping.toFloat())
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        jobTestDownload?.cancel()
        jobTestUpload?.cancel()
        jobTestPing?.cancel()
    }
}