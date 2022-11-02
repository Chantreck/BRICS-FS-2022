package vip.zhonghui.c

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import vip.zhonghui.c.databinding.FragmentExploreBinding
import java.io.IOException
import java.net.URL

class ExploreFragment : Fragment() {
    private lateinit var binding: FragmentExploreBinding
    private val client = OkHttpClient()

    private val houseCount = MutableLiveData<Int>()
    private val house = MutableLiveData<Home>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_explore, container, false)
        binding = FragmentExploreBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity ?: return

        val prefs =
            activity.getSharedPreferences("SHARED_PREFERENCES", AppCompatActivity.MODE_PRIVATE)
        val token = prefs.getString("TOKEN", null) ?: return

        println(token)

        val request = RequestBuilder.postRequest("house/listUserAllHouses", token)
        try {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()
                    println(body)
                    val resbody = Gson().fromJson(body, HomeResponse::class.java)

                    if (resbody.code == 200) {
                        houseCount.postValue(resbody.data?.size)
                        val curhouse = resbody.data?.get(0) ?: return
                        house.postValue(curhouse)
                    }
                }
            })
        } catch (e: java.lang.Exception) {
            Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
        }

        houseCount.observe(activity) {
            binding.dialog.isVisible = it == 0
        }

        val adapter = ExploreRecyclerAdapter(activity)
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)

        house.observe(activity) { home ->
            val livingRoom = home.space.find { it.name == "livingroom" } ?: return@observe

            binding.temperature.text = "Temperature: ${livingRoom.temperature}*C"
            binding.humidity.text = "Humidity: ${livingRoom.humidity}%rh"
            binding.light.text = "Light: ${livingRoom.luminosity}nits"
            binding.devicesCount.text = "${livingRoom.device.size} devices"

            val devices = livingRoom.device
            adapter.submitList(devices)

            val bitmap = activity.lifecycleScope.async(Dispatchers.IO) {
                val url = ImageUtil.getUrl(livingRoom.image)
                val bitmap = BitmapFactory.decodeStream(url.openStream())
                bitmap
            }

            activity.lifecycleScope.launch(Dispatchers.Main) {
                try {
                    binding.livingBg.setImageBitmap(bitmap.await())
                } catch (e: Exception) {

                }
            }
        }
    }
}

fun URL.toBitmap(): Bitmap? {
    return try {
        BitmapFactory.decodeStream(openStream())
    } catch (e: IOException) {
        null
    }
}

data class HomeResponse(
    val data: List<Home>?,
    val code: Int,
    val msg: String
)

data class Home(
    val features: String,
    val id: Int,
    val image: String,
    @SerializedName("main_card_id")
    val mainCardId: String,
    val name: String,
    val space: List<Room>
)

data class Room(
    val device: List<Device>,
    val deviceCount: Int,
    val humidity: Int,
    val icon: String,
    val id: Int,
    val image: String,
    val luminosity: Int,
    val name: String,
    val temperature: Int
)

data class Device(
    @SerializedName("d_id")
    val dId: Int,
    @SerializedName("device_name")
    val deviceName: String,
    @SerializedName("device_type")
    val deviceType: Int,
    val function: List<Any>,
    @SerializedName("function_count")
    val functionCount: Int,
    val hide: Boolean,
    val icon: String,
    val id: Int,
    @SerializedName("offline_at")
    val offlineAt: String?,
    @SerializedName("online_at")
    val onlineAt: String?,
    val state: String,
    @SerializedName("val")
    val value: Int
)