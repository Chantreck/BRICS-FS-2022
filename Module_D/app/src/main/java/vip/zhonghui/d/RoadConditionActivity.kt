package vip.zhonghui.d

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import vip.zhonghui.d.databinding.ActivityRoadConditionBinding
import java.io.IOException
import java.time.Instant
import java.util.*
import kotlin.concurrent.schedule

fun getRoadColor(status: Int) = when (status) {
    1 -> R.drawable.green
    2 -> R.drawable.yellow
    3 -> R.drawable.orange
    4 -> R.drawable.red
    5 -> R.drawable.darkred
    else -> R.drawable.green
}

data class Name(@SerializedName("UserName") val username: String = "user1")

data class SenceResponse(
    @SerializedName("pm2.5")
    val pm: Int,
    @SerializedName("ERRMSG")
    val error: String,
    @SerializedName("co2")
    val co: Int,
    val temperature: Int,
    val humidity: Int,
    @SerializedName("LightIntensity")
    val light: Int,
    @SerializedName("RESULT")
    val result: String
)

class RoadConditionActivity : AppCompatActivity(R.layout.activity_road_condition) {
    private val binding by lazy { ActivityRoadConditionBinding.inflate(layoutInflater) }
    private val client = OkHttpClient()
    private val count = MutableLiveData(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.menuBtn.setOnClickListener {
            binding.drawer.isVisible = true
        }

        binding.root.setOnClickListener {
            binding.drawer.isVisible = false
        }

        lifecycleScope.launch {
            Timer().schedule(5000, 15000) {
                updateRoads()
            }
        }

        lifecycleScope.launch {
            Timer().schedule(0, 500) {
                updateChel()
            }
        }

        binding.date.text = Instant.now().toString().take(10).replace("-", "/")

        binding.update.setOnClickListener {
            updateInfo()
        }

        count.observe(this) {
            if (it == 10) {
                binding.chel.isVisible = false
            }
        }

        setDrawer()
        updateInfo()
    }

    private fun updateChel() {
        val value = count.value ?: return

        if (value % 2 == 0) binding.chel.setImageResource(R.drawable.jiaojing1_1) else binding.chel.setImageResource(R.drawable.jiaojing1_2)
        count.postValue(value + 1)
    }

    private fun updateInfo() {
        val request = RequestBuilder.postRequest("get_all_sence", Name())
        try {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Toast.makeText(this@RoadConditionActivity, "Error", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()
                    val resbody = Gson().fromJson(body, SenceResponse::class.java)

                    if (resbody.result == "S") {
                        binding.temp.text = "Temperature: ${resbody.temperature}*C"
                        binding.humidity.text = "Humidity: ${resbody.humidity}%"
                        binding.pm.text = "PM2.5: ${resbody.pm}ug/m3"
                    }
                }
            })
        } catch (e: java.lang.Exception) {
            Toast.makeText(this@RoadConditionActivity, "Error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setDrawer() {
        binding.violation.setOnClickListener {
            val intent = Intent(this, ViolationInquiryActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.road.setOnClickListener {
            val intent = Intent(this, RoadConditionActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.analysis.setOnClickListener {
            val intent = Intent(this, AnalysisActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.traffic.setOnClickListener {
            val intent = Intent(this, TrafficLightSearchActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.logout.setOnClickListener {
            finish()
        }
    }

    private fun updateRoads() {
        println("updateRoads")

        val str11 = Body(1, "user1")
        val str12 = Body(2, "user1")
        val express = Body(3, "user1")
        val req11 = RequestBuilder.roadRequest(str11)
        val req12 = RequestBuilder.roadRequest(str12)
        val reqexp = RequestBuilder.roadRequest(express)

        try {
            client.newCall(req11).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Toast.makeText(this@RoadConditionActivity, "Error", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()
                    val resbody = Gson().fromJson(body, RoadResponse::class.java)

                    if (resbody.result == "S") {
                        println("11: ${resbody.status}")
                        val color = getRoadColor(resbody.status)
                        binding.str11.setBackgroundResource(color)
                    }
                }
            })
        } catch (e: java.lang.Exception) {
            Toast.makeText(this@RoadConditionActivity, "Error", Toast.LENGTH_SHORT).show()
        }

        try {
            client.newCall(req12).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Toast.makeText(this@RoadConditionActivity, "Error", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()
                    val resbody = Gson().fromJson(body, RoadResponse::class.java)

                    if (resbody.result == "S") {
                        println("12: ${resbody.status}")
                        val color = getRoadColor(resbody.status)
                        binding.str12.setBackgroundResource(color)
                    }
                }
            })
        } catch (e: java.lang.Exception) {
            Toast.makeText(this@RoadConditionActivity, "Error", Toast.LENGTH_SHORT).show()
        }

        try {
            client.newCall(reqexp).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Toast.makeText(this@RoadConditionActivity, "Error", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()
                    val resbody = Gson().fromJson(body, RoadResponse::class.java)

                    if (resbody.result == "S") {
                        println("expr: ${resbody.status}")
                        val color = getRoadColor(resbody.status)
                        binding.express1.setBackgroundResource(color)
                        binding.express2.setBackgroundResource(color)
                        binding.express3.setBackgroundResource(color)
                        binding.express4.setBackgroundResource(color)
                    }
                }
            })
        } catch (e: java.lang.Exception) {
            Toast.makeText(this@RoadConditionActivity, "Error", Toast.LENGTH_SHORT).show()
        }
    }
}

data class Body(
    @SerializedName("RoadId") val roadId: Int,
    @SerializedName("UserName") val username: String
)

data class RoadResponse(
    @SerializedName("Status")
    val status: Int,

    @SerializedName("ERRMSG")
    val msg: String,

    @SerializedName("RESULT")
    val result: String
)