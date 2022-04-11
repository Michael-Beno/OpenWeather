package work.beno.weather

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import work.beno.weather.filesystem.AppData
import work.beno.weather.helpers.Config
import work.beno.weather.helpers.JsonParser
import work.beno.weather.helpers.Weather
import work.beno.weather.utils.NetworkUtils
import work.beno.weather.view.WeatherView
import java.lang.Exception
import java.net.URL
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity(), LocationListener {
    /** Api Code from https://home.openweathermap.org/ must be placed here otherwise demo api response will be used*/
    //api
    private final var apiID= ""
    private final var timeRequest:Long = 7200000 //2hrs in ms
    private var lang = "en"
    private var units = "metric"
    //api response example
    private var apiResponse = "{\"coord\":{\"lon\":-8.3873,\"lat\":51.7604},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03d\"}],\"base\":\"stations\",\"main\":{\"temp\":9.91,\"feels_like\":6.77,\"temp_min\":9.91,\"temp_max\":12.06,\"pressure\":1024,\"humidity\":53},\"visibility\":10000,\"wind\":{\"speed\":7.2,\"deg\":340},\"clouds\":{\"all\":40},\"dt\":1648976410,\"sys\":{\"type\":1,\"id\":1563,\"country\":\"IE\",\"sunrise\":1648879590,\"sunset\":1648926448},\"timezone\":3600,\"id\":2962594,\"name\":\"Dublin\",\"cod\":200}"
    //location
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    private var lat:Double = 0.0
    private var lon:Double = 0.0
    //view
    private lateinit var linMain:LinearLayout
    private lateinit var view: WeatherView
    private lateinit var tvStatus:TextView
    private var statusData:String = ""
    private var statusWifi:String = ""
    private var statusGPS:String = ""
    //internet (wifi)
    var net: NetworkUtils? = null
    //persist - (load, save)
    var file: AppData? = null
    lateinit var mainHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Weather Info"
        tvStatus = findViewById(R.id.tvStatus)
        linMain = findViewById(R.id.lin_main)
        view = WeatherView(linMain, this)
        net = NetworkUtils()
        file = AppData(applicationContext)
        mainHandler = Handler(Looper.getMainLooper())
    }
    override fun onResume() {
        super.onResume()
        initUpdate()
        Log.i("Thread", "onResume")
        mainHandler.post(updateTextTask)
    }
    override fun onPause() {
        super.onPause()
        Log.i("Thread", "onPause")
        mainHandler.removeCallbacks(updateTextTask)
    }
    private fun initUpdate() {
        //load persisted data
        val confJson = file?.load("config.json")
        val weatherJson = file?.load("data.json")
        val parse = JsonParser()
        //parse config
        val config: Config = parse.getConfig(confJson)
        //parse weather
        val weather: Weather = parse.getWeather(weatherJson)
        //test if weather is actual
        if ((System.currentTimeMillis() - config.timestamp) < timeRequest) {
            //actual weather data
            statusData = "\n"
        }else{
            //outdated weather data
            statusData = "outdated"
        }
        view.display(weather)
    }
    private val updateTextTask = object : Runnable {
        override fun run() {
            Log.i("Thread", "run...")
            runnable(this)
        }
    }
    fun runnable(param: Runnable) {
        val file = AppData(applicationContext)
        val confJson = file.load("config.json")
        val parse = JsonParser()
        val config: Config = parse.getConfig(confJson)
        if ((System.currentTimeMillis() - config.timestamp) < timeRequest) {
            //actual weather data
            statusData = "\n"
            mainHandler.removeCallbacks(updateTextTask)
        }else{
            //outdated weather data
            statusData = "outdated"
            if(net?.getNetworkState(applicationContext) == "WiFi"){
                statusWifi = ""
                if(lat == 0.0 && lon == 0.0)
                     getLocation()
            }else{
                statusWifi = "\nApp works only on WiFi"
            }
            mainHandler.postDelayed(param, 1000)
        }
        tvStatus.text = statusData+statusWifi+statusGPS
    }
    private fun getLocation() {
        statusGPS = "\nobtaining location..."
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (gpsStatus) {
            if ((ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED)
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    locationPermissionCode
                )
            }
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
            } catch (e: Exception) {
                statusGPS ="\nPermission for location denied!\n" + "Please allow access to device's location"
                Log.e("gps", e.message.toString())
            }
        }else{
            statusGPS = "\nLocation must be On"
        }
    }
    override fun onLocationChanged(location: Location) {
        lat = location.latitude
        lon =  location.longitude
        statusGPS = ""
        downloadData()
        Log.d("json", "location found")
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode && grantResults != null) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun downloadData(){
        Executors.newSingleThreadExecutor().execute {
            Log.d("json", "download...")
            try {
                apiResponse = URL("https://api.openweathermap.org/data/2.5/weather?lang=$lang&lat=$lat&lon=$lon&units=$units&appid=$apiID").readText()
            }catch (e:Exception){
                Log.e("json", e.toString())
            }

            Log.d("json", "response: " + apiResponse)
            Log.d("json", "updating done")
            val parse = JsonParser()
            val weatherObject: Weather = parse.getWeather(apiResponse)
            tvStatus.post {
                tvStatus.text = ""
                view.display(weatherObject)
                //val file = AppData(applicationContext)
                val c = Config()
                c.timestamp = System.currentTimeMillis()
                file?.save(c, "config.json")
                file?.save(apiResponse, "data.json")
            }
        }
    }
}