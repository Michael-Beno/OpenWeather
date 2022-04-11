package work.beno.weather.helpers


import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

/**Class used for parsing JSON to object*/
class JsonParser {

    fun getWeather(jsonObject: String?): Weather {
        val w = Weather()
        Log.d("json", "full:"+jsonObject)
        if (jsonObject != null) {
            if(jsonObject.isNotEmpty())
                try {
                    val obj = JSONObject(jsonObject)
                    /**COORD block*/
                    val coord = JSONObject(obj.getString("coord"))
                    Log.d("json", "coord:" + coord.toString())
                    /**WEATHER block*/
                    val weather = JSONObject(obj.getJSONArray("weather")[0].toString())
                    Log.d("json", "weather:"+weather.toString())
                    w.desc = weather.getString("description")
                    /**MAIN block*/
                    val main = JSONObject(obj.get("main").toString())
                    Log.d("json", "main:" + main.toString())
                    w.temp = main.getDouble("temp")
                    w.tempFeel = main.getDouble("feels_like")
                    w.tempMin = main.getDouble("temp_min")
                    w.tempMax = main.getDouble("temp_max")
                    w.pressure = main.getInt("pressure")
                    w.humidity = main.getInt("humidity")
                    /**VISIBILITY block*/
                    val visibility = obj.getInt("visibility")
                    Log.d("json", "visibility:" + visibility)
                    w.visibility = visibility
                    /**WIND block*/
                    val wind = JSONObject(obj.get("wind").toString())
                    Log.d("json", "wind:" + wind.toString())
                    w.windSpeed = wind.getDouble("speed")
                    w.windDeg = wind.getInt("deg")
                    /**Clouds block*/
                    val clouds = JSONObject(obj.get("clouds").toString())
                    Log.d("json", "clouds:" + clouds.toString())
                    w.cloudsPercentage = clouds.getInt("all")
                    /**Date Time block*/
                    val time = obj.getLong("dt")
                    w.dateTime = time
                    /**Timezone block*/
                    val timezone = obj.getLong("timezone")
                    w.timeZone = timezone
                    /**SYS block*/
                    val sys = JSONObject(obj.get("sys").toString())
                    w.country = sys.getString("country")
                    /**NAME block*/
                    val name = obj.getString("name")
                    w.name = name
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
        }
        return w
    }
    fun getConfig(jsonObject: String?): Config {
        val c = Config()
        Log.d("json","config: "+jsonObject)
        if (jsonObject != null) {
            if(jsonObject.isNotEmpty())
                try {
                    val obj = JSONObject(jsonObject)
                    c.timestamp = obj.getLong("timestamp")
                    Log.d("json",obj.toString())
                }catch (e:Exception){
                    e.printStackTrace()
                }
        }
       return c
    }
}
class Weather {
    var name: String = ""
    var country: String = ""
    var desc: String = ""
    var temp: Double = 0.0
    var tempFeel: Double = 0.0
    var tempMin: Double = 0.0
    var tempMax: Double = 0.0
    var humidity: Int = 0
    var pressure: Int = 0
    var windDeg: Int = 0
    var windSpeed: Double = 0.0
    var visibility: Int = 0
    var cloudsPercentage: Int = 0
    var timeZone: Long = 0
    var dateTime: Long = 0
}
class Config{
    var timestamp: Long = 0
}
