package work.beno.weather.view

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import work.beno.weather.helpers.Weather

/**Class that is used for displaying view*/
class WeatherView(private var linMain: LinearLayout?, mainActivity: Context) {

    private var ctx: Context = mainActivity
    private var tvLocation:TextView = TextView(ctx)
    private var tvWDesc:TextView
    private var tvTemp:TextView
    private var tvTempFeel:TextView
    private var tvTempMin:TextView
    private var tvTempMax:TextView
    private var tvHumidity:TextView
    private var tvPressure:TextView
    private var textView:TextView

    init {
        /**Location City, Country*/
        tvLocation.textSize = 20f
        tvLocation.textAlignment = View.TEXT_ALIGNMENT_CENTER
        this.linMain?.addView(tvLocation)
        /**Description (sunny, clouds)*/
        tvWDesc = TextView(ctx)
        tvWDesc.textSize = 15f
        tvWDesc.textAlignment = View.TEXT_ALIGNMENT_CENTER
        this.linMain?.addView(tvWDesc)
        /**Temperature*/
        tvTemp = TextView(ctx)
        tvTemp.textSize = 30f
        tvTemp.textAlignment = View.TEXT_ALIGNMENT_CENTER
        this.linMain?.addView(tvTemp)
        /**Feels like*/
        tvTempFeel = TextView(ctx)
        tvTempFeel.setPadding(10, 10, 0, 0)
        this.linMain?.addView(tvTempFeel)
        /**Temperature min*/
        tvTempMin = TextView(ctx)
        tvTempMin.setPadding(10, 0, 0, 0)
        this.linMain?.addView(tvTempMin)
        /**Temperature max*/
        tvTempMax = TextView(ctx)
        tvTempMax.setPadding(10, 0, 0, 0)
        this.linMain?.addView(tvTempMax)
        /**Humidity*/
        tvHumidity = TextView(ctx)
        tvHumidity.setPadding(10, 10, 0, 0)
        this.linMain?.addView(tvHumidity)
        /**Pressure*/
        tvPressure = TextView(ctx)
        tvPressure.setPadding(10, 0, 0, 0)
        this.linMain?.addView(tvPressure)
        /**Wind*/
        textView = TextView(ctx)
        textView.setPadding(10, 10, 0, 0)
        this.linMain?.addView(textView)

    }

    fun display(w: Weather) {
        var deg = ""
        val units = "metric"
        if(units == "metric"){ deg = "C"}
        /**Location City, Country*/
        tvLocation.text = w.name+", "+w.country
        /**Description (sunny, clouds)*/
        tvWDesc.text = w.desc
        /**Temperature*/
        tvTemp.text = w.temp.toString()+"° "+deg
        /**Feels like*/
        tvTempFeel.text = "Feels like: "+w.tempFeel.toString()+"° "+deg
        /**Temperature min*/
        tvTempMin.text = "Min: "+w.tempMin.toString()+"° "+deg
        /**Temperature max*/
        tvTempMax.text = "Max: "+w.tempMax.toString()+"° "+deg
        /**Humidity*/
        tvHumidity.text = "Humidity: "+w.humidity.toString()+" %"
        /**Pressure*/
        tvPressure.text = "Pressure: "+w.pressure.toString()+" hPa"
        /**Wind*/
        textView.text = "Wind speed: "+w.windSpeed.toString()+" m/s\nDirection: "+w.windDeg+"° "

    }
}
