package com.example.wise_memory_optimizer.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.LiveData

class NetWorkConnection(
    private val context: Context
) : LiveData<Boolean>() {

   private var connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private lateinit var  netWorkCallback : ConnectivityManager.NetworkCallback

    override fun onActive() {
        super.onActive()
        updateConnection()
        context.registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
//        when{
//            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
//                context.registerReceiver(networkReceiver,IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
//                connectivityManager.registerDefaultNetworkCallback(connectivityManagerCallBack())
//            }
//            Build.VERSION.SDK_INT == Build.VERSION_CODES.M -> {
//                mNetWorkRequest()
//            }
//            else -> {
//                context.registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
//            }
//        }
    }

    override fun onInactive() {
        super.onInactive()
        try {
            context.unregisterReceiver(networkReceiver)
        }catch (ex : Exception){
            Log.e("===>","$ex")
        }
    }

    private fun mNetWorkRequest(){
        val requestBuilder = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)

        connectivityManager.registerNetworkCallback(requestBuilder.build(),connectivityManagerCallBack())
    }


    private fun connectivityManagerCallBack() : ConnectivityManager.NetworkCallback{
        netWorkCallback = object  : ConnectivityManager.NetworkCallback(){
            override fun onLost(network: Network) {
                super.onLost(network)
                postValue(false)
            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                postValue(true)
            }
        }

        return netWorkCallback
    }

    private val networkReceiver = object  : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            updateConnection()
        }
    }

    private fun updateConnection(){
        val activeNetWork =  connectivityManager.activeNetworkInfo
        postValue(activeNetWork?.isConnected == true)
    }
}