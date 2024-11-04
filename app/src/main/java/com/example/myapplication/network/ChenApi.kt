package com.example.myapplication.network

import com.example.myapplication.model.Douban
import com.example.myapplication.model.FileListResponse
import com.example.myapplication.model.Item
import com.example.myapplication.model.MediaSource
import com.example.myapplication.model.Resope
import com.example.myapplication.model.Sear
import com.example.myapplication.model.Subject
import com.example.myapplication.model.VideoQuality
import com.example.myapplication.model.Xl
import com.example.myapplication.model.xl2
import kotlinx.serialization.json.Json

class ChenApi {
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
    val networkClient = OkHttp()
    //获取热门的影视 指定类型
    suspend fun GetHotMovie(index: Int): Douban? {
        val type = when (index) {
            0 -> "movie"
            1 -> "tv"
            2 -> "日本动画"
            else -> "movie"
        }
        val response = networkClient.get("http://47.107.50.50:9002/api/api/website/hot-movie/$type")
        return response?.let {
            try {
                var resope = json.decodeFromString<Resope>(it)
                json.decodeFromString<Douban>(resope.data)
            } catch (e: Exception) {
                println("JSON parsing error: ${e.message}")
                null
            }
        }
    }
    //获取搜索的影视
    suspend fun GetSear(name: String): List<Sear>? {
        val response = networkClient.get("http://110.41.14.10:8101/sear/${name}")
        return response?.let {
            try {
                json.decodeFromString<List<Sear>>(it)
            } catch (e: Exception) {
                println("JSON parsing error: ${e.message}")
                null
            }
        }
    }
    //获取指定的影视列表
    suspend fun GetList(sear: Sear): List<MediaSource>? {
        val body = json.encodeToString(Sear.serializer(), sear)
        val response = networkClient.post("http://110.41.14.10:8101/playitems", body)
        return response?.let {
            try {
                json.decodeFromString<List<MediaSource>>(it)
            } catch (e: Exception) {
                println("JSON parsing error: ${e.message}")
                null
            }
        }
    }
    //获取指定的播放项的磁力链接
    suspend fun GetMgUrl(item: Item): Item? {
        val body = json.encodeToString(Item.serializer(), item)
        val response = networkClient.post("http://110.41.14.10:8101/playcontext", body)
        return response?.let {
            try {
                json.decodeFromString<Item>(it)
            } catch (e: Exception) {
                println("JSON parsing error: ${e.message}")
                null
            }
        }
    }
    //将任务添加到迅雷引擎并获取其返回值
    suspend fun Getupuri(mg: String): Xl? {
        val mgurl = mg.replace("magnet:?xt=urn:btih:","")
        val response = networkClient.get("http://110.41.14.10:8105/upurl/${mgurl}")
        return response?.let {
            try {
                json.decodeFromString<Xl>(it)
            } catch (e: Exception) {
                println("JSON parsing error: ${e.message}")
                null
            }
        }
    }
    //查询云任务状态
    suspend fun Getfilestr(yid: String): xl2? {
        val response = networkClient.get("http://110.41.14.10:8105/getfilestr/${yid}")
        return response?.let {
            try {
                json.decodeFromString<xl2>(it)
            } catch (e: Exception) {
                println("JSON parsing error: ${e.message}")
                null
            }
        }
    }
    //获取指定文件夹的文件列表
    suspend fun Getfiles(fid: String): FileListResponse? {
        val response = networkClient.get("http://110.41.14.10:8105/getfiles/${fid}")
        return response?.let {
            try {
                json.decodeFromString<FileListResponse>(it)
            } catch (e: Exception) {
                println("JSON parsing error: ${e.message}")
                null
            }
        }
    }
    //获取指定文件的播放列表
    suspend fun Getfileplay(fid: String): MutableList<VideoQuality>? {
        val response = networkClient.get("http://110.41.14.10:8105/getfileplay/${fid}")
        return response?.let {
            try {
                json.decodeFromString<MutableList<VideoQuality>>(it)
            } catch (e: Exception) {
                println("JSON parsing error: ${e.message}")
                null
            }
        }
    }

}

