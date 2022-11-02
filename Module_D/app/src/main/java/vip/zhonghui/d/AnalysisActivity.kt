package vip.zhonghui.d

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import vip.zhonghui.d.databinding.ActivityAnalysisBinding

class AnalysisActivity : AppCompatActivity(R.layout.activity_analysis) {
    private val binding by lazy { ActivityAnalysisBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.menuBtn.setOnClickListener {
            binding.drawer.isVisible = true
        }

        binding.root.setOnClickListener {
            binding.drawer.isVisible = false
        }

        setDrawer()
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
}