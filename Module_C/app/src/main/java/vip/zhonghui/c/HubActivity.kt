package vip.zhonghui.c

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import vip.zhonghui.c.databinding.ActivityHubBinding

class HubActivity : AppCompatActivity() {
    private val binding by lazy { ActivityHubBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.navView.setOnItemSelectedListener {
            openFragment(it.itemId)
        }

        binding.navView.selectedItemId = R.id.profile

        openFragment(R.id.profile)
    }

    private fun openFragment(id: Int): Boolean {
        var fragment: Fragment? = null

        when (id) {
            R.id.explore -> fragment = ExploreFragment()
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
}