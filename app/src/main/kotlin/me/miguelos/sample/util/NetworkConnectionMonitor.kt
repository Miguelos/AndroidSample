package me.miguelos.sample.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import timber.log.Timber

class NetworkConnectionMonitor(
    var context: Context,
    private var callback: ConnectionNetworkCallback? = null
) : LiveData<Boolean>() {

    fun unregisterDefaultNetworkCallback() {
        callback?.let {
            (context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)
                ?.unregisterNetworkCallback(it)
        }
    }

    fun registerDefaultNetworkCallback() {
        try {
            (context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.let {
                postValue(checkConnection(it))
                it.registerDefaultNetworkCallback(ConnectionNetworkCallback())
            }
        } catch (e: Exception) {
            Timber.tag(TAG).d("Connection: Exception in registerDefaultNetworkCallback")
            postValue(false)
        }
    }

    private fun checkConnection(connectivityManager: ConnectivityManager): Boolean {
        val network = connectivityManager.activeNetwork
        return if (network == null) {
            false
        } else {
            val actNw = connectivityManager.getNetworkCapabilities(network)
            (actNw != null
                    && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)))
        }
    }

    inner class ConnectionNetworkCallback : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            postValue(true)
            Timber.tag(TAG).d("Connection: onAvailable")
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            postValue(false)
            Timber.tag(TAG).d("Connection: onLost")
        }

        override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
            super.onBlockedStatusChanged(network, blocked)
            Timber.tag(TAG).d("Connection: onBlockedStatusChanged")
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            Timber.tag(TAG).d("Connection: onCapabilitiesChanged")
        }

        override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
            super.onLinkPropertiesChanged(network, linkProperties)
            Timber.tag(TAG).d("Connection: onLinkPropertiesChanged")
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            super.onLosing(network, maxMsToLive)
            Timber.tag(TAG).d("Connection: onLosing")
        }

        override fun onUnavailable() {
            super.onUnavailable()
            Timber.tag(TAG).d("Connection: onUnavailable")
        }
    }
}
