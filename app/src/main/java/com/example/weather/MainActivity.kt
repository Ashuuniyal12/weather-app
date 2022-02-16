package com.example.weather

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    val CITY: String = "dehradun"
    val API: String = "74132965a285ba5b08e3abbd57d0f66f"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadweather()
    }

    private fun loadweather() {
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$CITY&appid=$API"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val main = response.getJSONObject("main")
                val sys = response.getJSONObject("sys")
                val wind = response.getJSONObject("wind")
                val weather = response.getJSONArray("weather").getJSONObject(0)
                val updateAt: Long = response.getLong("dt")
                val updatedAtText =
                    "Updated at: " + SimpleDateFormat("dd/mm/yyyy hh:mm a", Locale.ENGLISH).format(
                        Date(updateAt * 1000)
                    )
                val temp = ""+(main.getString("temp").toDouble()-273.15).toBigDecimal().setScale(1,
                    RoundingMode.UP).toDouble() + "°C"
                val tempMin = "Min Temp: " + (main.getString("temp_min").toDouble()-273.15).toBigDecimal().setScale(1,
                    RoundingMode.UP).toDouble() + "°C"
                val tempMax = "Max Temp: " + (main.getString("temp_max").toDouble()-273.15).toBigDecimal().setScale(1,
                    RoundingMode.UP).toDouble() + "°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")
                val sunrise: Long = sys.getLong("sunrise")
                val sunset: Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")
                val address = response.getString("name") + ", " + sys.getString("country")

                findViewById<TextView>(R.id.address).text = address
                findViewById<TextView>(R.id.updated_at).text = updatedAtText
                findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()
                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.temp_min).text = tempMin
                findViewById<TextView>(R.id.temp_max).text = tempMax
                findViewById<TextView>(R.id.sunrise).text =
                    SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(
                        Date(sunrise * 1000)
                    )
                findViewById<TextView>(R.id.sunset).text =
                    SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset * 1000))
                findViewById<TextView>(R.id.wind).text = windSpeed
                findViewById<TextView>(R.id.pressure).text = pressure
                findViewById<TextView>(R.id.humidity).text = humidity

                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
               //findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE
            },
            Response.ErrorListener { })

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
}