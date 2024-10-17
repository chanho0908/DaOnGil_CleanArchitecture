package kr.techit.lion.presentation.observer

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NetworkConnectivityObserver(
    context: Context
): ConnectivityObserver {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @OptIn(DelicateCoroutinesApi::class)
    private val status: Flow<ConnectivityObserver.Status> =
        observe().stateIn(GlobalScope, WhileSubscribed(5000), ConnectivityObserver.Status.Unavailable)

    override fun getFlow(): Flow<ConnectivityObserver.Status> {
        return status
    }

    private fun observe(): Flow<ConnectivityObserver.Status> {
        return callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback(){
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch { send(ConnectivityObserver.Status.Available) }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    launch { send(ConnectivityObserver.Status.Losing) }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch { send(ConnectivityObserver.Status.Lost) }
                }

                override fun onUnavailable(){
                    super.onUnavailable()
                    launch { send(ConnectivityObserver.Status.Unavailable) }
                }
            }

            connectivityManager.registerDefaultNetworkCallback(callback)

            awaitClose{
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }.distinctUntilChanged()
    }

    companion object{

        @Volatile
        private var INSTANCE: NetworkConnectivityObserver? = null

        fun getInstance(context: Context): ConnectivityObserver{
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: NetworkConnectivityObserver(context).also { INSTANCE = it }
            }
        }
    }
}