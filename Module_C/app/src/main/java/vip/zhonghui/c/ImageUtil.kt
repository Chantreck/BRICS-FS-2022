package vip.zhonghui.c

import java.net.URL

object ImageUtil {
    fun getUrl(url: String): URL {
        return URL("http://114.119.183.115/$url")
    }
}