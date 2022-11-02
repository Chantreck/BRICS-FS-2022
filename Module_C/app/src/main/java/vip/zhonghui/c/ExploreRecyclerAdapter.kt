package vip.zhonghui.c

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import vip.zhonghui.c.databinding.ItemDeviceBinding

class ExploreRecyclerAdapter(private val activity: FragmentActivity) :
    ListAdapter<Device, ExploreRecyclerAdapter.ExploreViewHolder>(DIFF) {
    private companion object {
        val DIFF = object : DiffUtil.ItemCallback<Device>() {
            override fun areItemsTheSame(oldItem: Device, newItem: Device): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Device, newItem: Device): Boolean =
                oldItem == newItem
        }
    }

    inner class ExploreViewHolder(view: View) : ViewHolder(view) {
        private val binding = ItemDeviceBinding.bind(view)

        fun bind(item: Device) {
            binding.deviceTitle.text = item.deviceName

            val bitmap = activity.lifecycleScope.async(Dispatchers.IO) {
                val url = ImageUtil.getUrl(item.icon)
                val bitmap = BitmapFactory.decodeStream(url.openStream())
                bitmap
            }

            activity.lifecycleScope.launch(Dispatchers.Main) {
                try {
                    binding.deviceImage.setImageBitmap(bitmap.await())
                } catch (e: Exception) {

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_device, parent, false)
        return ExploreViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExploreViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}