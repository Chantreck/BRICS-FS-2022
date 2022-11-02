package vip.zhonghui.c

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.core.view.isVisible
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import vip.zhonghui.c.databinding.ActivityMainBinding
import java.io.IOException
import kotlin.math.max
import kotlin.math.min

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    var x: Float = 0f
    var position = 0

    private lateinit var circles: List<View>
    private val images = listOf(R.drawable.splash1, R.drawable.splash2, R.drawable.splash3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val prefs: SharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE)
        val check = prefs.getBoolean("IS_FIRST_TIME", false)
        if (check) {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.circle2.isEnabled = false
        binding.circle3.isEnabled = false
        circles = listOf(binding.circle1, binding.circle2, binding.circle3)

        binding.root.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                x = motionEvent.x
                println("down: $x")
                true
            }
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                println("up: $x ${motionEvent.x}")

                if (motionEvent.x - x > 0) {
                    prev()
                } else {
                    next()
                }
            }

            true
        }

        binding.button.setOnClickListener {
            if (position == 2) {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)

                prefs.edit().putBoolean("IS_FIRST_TIME", true).apply()
            } else {
                next()
            }
        }
    }

    private fun next() {
        position = min(2, position + 1)
        invalidate()
    }

    private fun prev() {
        position = max(0, position - 1)
        invalidate()
    }

    private fun invalidate() {
        for (i in 0 until position) {
            circles[i].isEnabled = false
        }
        circles[position].isEnabled = true
        for (i in position + 1..2) {
            circles[i].isEnabled = false
        }

        binding.image.setImageResource(images[position])
        binding.button.isVisible = position == 2
    }
}