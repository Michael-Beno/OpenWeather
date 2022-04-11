package work.beno.weather.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
/**this class handling network*/
class NetworkUtils() {

    fun getNetworkState(context: Context): String {
        var result = "NO NETWORK"
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return "NO NETWORK"
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return "NO NETWORK"
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WiFi"
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Cellular"
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Ethernet"
                else -> "NO NETWORK"
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI ->  "WiFi"
                    ConnectivityManager.TYPE_MOBILE -> "Cellular"
                    ConnectivityManager.TYPE_ETHERNET -> "Ethernet"
                    else -> "NO NETWORK"
                }
            }
        }
        return result
    }
}
