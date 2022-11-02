package vip.zhonghui.c

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import vip.zhonghui.c.databinding.ActivitySignInBinding
import java.io.IOException

class SignInActivity : AppCompatActivity(R.layout.activity_sign_in) {
    private val binding by lazy { ActivitySignInBinding.inflate(layoutInflater) }
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.signIn.setOnClickListener {
            val username = binding.username.text.toString()
            val password = binding.pass.text.toString()

            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Username and password must not be empty", Toast.LENGTH_SHORT).show()
            } else  {
                val body = LoginDto(username, password)
                val request = RequestBuilder.postRequest("account/login", body)

                try {
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Toast.makeText(this@SignInActivity, "Error", Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(call: Call, response: Response) {
                            val body = response.body?.string()
                            val resbody = Gson().fromJson(body, ApiResponse::class.java)
                            println(body)

                            if (resbody.code == 200) {
                                val prefs = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE)
                                prefs.edit().putString("TOKEN", resbody.data!!.token).apply()

                                val intent = Intent(this@SignInActivity, HubActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    })
                } catch (e: java.lang.Exception) {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

data class LoginDto(
    val username: String,
    val password: String
)

data class ApiResponse(
    val data: Data? = null,
    val code: Int,
    val msg: String
)

data class Data(
    val token: String
)