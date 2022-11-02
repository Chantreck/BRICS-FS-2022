package vip.zhonghui.c

import android.content.Intent
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
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import vip.zhonghui.c.databinding.FragmentAccountBinding
import java.io.IOException

class AccountFragment : Fragment() {
    private val client = OkHttpClient()
    private val avatar = MutableLiveData<String>()
    private var username = MutableLiveData<String>()
    private var signature = MutableLiveData<String>()
    private var numberHouse = MutableLiveData<Int>()
    private lateinit var binding: FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        binding = FragmentAccountBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity ?: return

        val prefs =
            activity.getSharedPreferences("SHARED_PREFERENCES", AppCompatActivity.MODE_PRIVATE)
        val token = prefs.getString("TOKEN", null) ?: return

        println(token)

        val request = RequestBuilder.postRequest("account/accountInfo", token)
        try {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()
                    val resbody = Gson().fromJson(body, AccountResponse::class.java)

                    if (resbody.code == 200) {
                        avatar.postValue(resbody.data!!.avatar)
                        username.postValue(resbody.data.nickname)
                        signature.postValue(resbody.data.signature)

                        val bitmap = activity.lifecycleScope.async(Dispatchers.IO) {
                            val url = ImageUtil.getUrl(resbody.data.avatar)
                            val bitmap = BitmapFactory.decodeStream(url.openStream())
                            bitmap
                        }

                        activity.lifecycleScope.launch(Dispatchers.Main) {
                            try {
                                binding.image.setImageBitmap(bitmap.await())
                            } catch (e: Exception) {

                            }
                        }
                    }
                }
            })
        } catch (e: java.lang.Exception) {
            Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
        }

        val request2 = RequestBuilder.postRequest("house/listUserAllHouses", token)
        try {
            client.newCall(request2).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()
                    val resbody = Gson().fromJson(body, AllHouseResponse::class.java)

                    if (resbody.code == 200) {
                        numberHouse.postValue(resbody.data!!.size)
                    }
                }
            })
        } catch (e: java.lang.Exception) {

        }

        avatar.observe(activity) {

        }

        username.observe(activity) {
            binding.nickname.text = it
        }

        signature.observe(activity) {
            binding.signature.text = it
        }

        numberHouse.observe(activity) {
            binding.houseCount.text = it.toString()
        }

        binding.management.setOnClickListener {
            val intent = Intent(activity, HouseManagementActivity::class.java)
            startActivity(intent)
        }

        binding.logout.setOnClickListener {
            activity.finish()
        }

        binding.cache.setOnClickListener {
            binding.cacheSize.text = "Current Cache Size: 8851 KB"
            binding.dialog.isVisible = true
            binding.clear.setOnClickListener {
                binding.cacheSize.text = "Current Cache Size: 0 KB"
            }
            binding.cancel.setOnClickListener { binding.dialog.isVisible = false }
        }
    }
}

data class AccountResponse(
    val data: AccountData? = null,
    val code: Int,
    val msg: String
)

data class AccountData(
    @SerializedName("account_status")
    val accountStatus: Int,
    @SerializedName("app_version")
    val appVersion: String?,
    val avatar: String,
    val createtime: String,
    val email: String,
    val gender: String,
    val id: Int,
    @SerializedName("nick_name")
    val nickname: String,
    @SerializedName("pass_word")
    val password: String,
    val phone: String,
    @SerializedName("plain_pass_word")
    val plainPassword: String?,
    val signature: String,
    @SerializedName("user_name")
    val username: String
)

data class AllHouseResponse(
    val data: List<Any>?,
    val code: Int,
    val msg: String
)