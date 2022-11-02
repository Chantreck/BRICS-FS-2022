package vip.zhonghui.d

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.VideoView
import vip.zhonghui.d.databinding.ActivityVideoBinding

class VideoActivity : AppCompatActivity() {
    private val binding by lazy { ActivityVideoBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        println(resources.getResourcePackageName(R.raw.video_1))

        val path = "android.resource://" + packageName + "/" + R.raw.video_1

        val file = Uri.parse(path)
        binding.videoView.setVideoURI(file)
        binding.videoView.start()
    }
}