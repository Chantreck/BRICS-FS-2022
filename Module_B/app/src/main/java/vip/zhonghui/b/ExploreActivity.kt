package vip.zhonghui.b

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.moduleb.R
import com.example.moduleb.databinding.ActivityExploreBinding

class ExploreActivity : AppCompatActivity() {

    private val binding by lazy { ActivityExploreBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.navView.setOnItemSelectedListener {
            openFragment(it.itemId)
        }

        openFragment(R.id.explore)
    }

    private fun openFragment(id: Int): Boolean {
        var fragment: Fragment? = null

        when (id) {
            R.id.explore -> fragment = ExploreFragment()
            R.id.bookings -> fragment = BookingFragment()
            R.id.notification -> fragment = NotificationsFragment()
            R.id.profile -> fragment = AccountFragment()
        }

        fragment?.let {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(binding.container.id, it)
            }
            return true
        }

        return false
    }

    fun openHotelDetails() {
        val intent = Intent(this, HotelDetailActivity::class.java)
        startActivity(intent)
    }
}