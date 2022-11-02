package vip.zhonghui.b

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import com.example.moduleb.R
import com.example.moduleb.databinding.ActivityMainBinding
import kotlin.math.max
import kotlin.math.min

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    var x: Float = 0f
    var position = 0

    private lateinit var circles: List<View>

    private val titles =
        listOf("Luxury and Classy Hotels", "Find The Best Place To", "It's Like You Just Meet")
    private val desc = listOf(
        "The best quality hotels and staycation with international standarts are curate",
        "The largest platform to find staycations and hotels in Indonesia",
        "We guarantee that you will not disapointed with our features in this apps"
    )
    private val images =
        listOf(R.drawable.onboarding1, R.drawable.onboarding2, R.drawable.onboarding3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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

        binding.fab.setOnClickListener {
            if (position == 2) {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
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
        for (i in 0..position) {
            circles[i].isEnabled = true
        }
        for (i in position + 1..2) {
            circles[i].isEnabled = false
        }

        binding.title.text = titles[position]
        binding.slogan.text = desc[position]
        binding.image.setImageResource(images[position])
    }
}