package vip.zhonghui.b

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.moduleb.R
import com.example.moduleb.databinding.ExploreItemBinding

data class Item(
    val id: Int,
    val image: Int
)

class ExploreRecyclerAdapter(private val listener: OnHotelClick) :
    ListAdapter<Item, ExploreRecyclerAdapter.ExploreViewHolder>(DIFF) {
    private companion object {
        val DIFF = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean = oldItem == newItem
        }
    }

    inner class ExploreViewHolder(view: View) : ViewHolder(view) {
        val binding = ExploreItemBinding.bind(view)

        init {
            view.setOnClickListener {
                listener.onClick()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.explore_item, parent, false)
        return ExploreViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExploreViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.image.setImageResource(item.image)
    }
}

interface OnHotelClick {
    fun onClick()
}