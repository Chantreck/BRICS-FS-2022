package vip.zhonghui.b

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moduleb.R
import com.example.moduleb.databinding.FragmentExploreBinding

class ExploreFragment : Fragment() {
    private lateinit var binding: FragmentExploreBinding

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

        if (activity == null) return

        val items = listOf(
            Item(0, R.drawable.image1),
            Item(1, R.drawable.image2),
            Item(2, R.drawable.image3),
            Item(3, R.drawable.image4),
            Item(4, R.drawable.image5),
            Item(5, R.drawable.image1),
            Item(6, R.drawable.image2),
            Item(7, R.drawable.image3),
            Item(8, R.drawable.image4),
            Item(9, R.drawable.image5)
        )

        val adapter = ExploreRecyclerAdapter(object : OnHotelClick {
            override fun onClick() {
                (activity as ExploreActivity).openHotelDetails()
            }
        })

        binding.search.setOnClickListener {
            val intent = Intent(context, SearchActivity::class.java)
            startActivity(intent)
        }

        binding.recycler.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.recycler.adapter = adapter

        adapter.submitList(items)
    }
}