package vip.zhonghui.d

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.EMPTY_REQUEST

object RequestBuilder {
    private val base_url = "http://114.119.183.115:8081/api/v2"

    fun <T> postRequest(url: String, body: T, token: String? = null): Request {
        val requestBody = Gson().toJson(body).toRequestBody("application/json".toMediaTypeOrNull())

        return if (token == null) {
            Request.Builder()
                .url("$base_url/$url")
                .post(requestBody)
                .build()
        } else {
            Request.Builder()
                .url("$base_url/$url")
                .header("Authorization", "$token")
                .post(requestBody)
                .build()
        }
    }

    fun roadRequest(body: Body) = Request.Builder()
        .url("$base_url/get_road_status")
        .post(Gson().toJson(body).toRequestBody("application/json".toMediaTypeOrNull()))
        .build()

    fun postRequest(url: String, token: String): Request {
        return Request.Builder()
            .url("$base_url/$url")
            .header("Authorization", "$token")
            .post(EMPTY_REQUEST)
            .build()
    }
}