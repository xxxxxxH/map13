package cn.nba.map.live.gps.navigation.utils

import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cn.nba.map.live.gps.navigation.entity.DataEntity
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import com.srplab.www.starcore.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream

private var python: StarObjectClass? = null

private var SrvGroup: StarSrvGroupClass? = null

private var Service: StarServiceClass? = null

private var starcore: StarCoreFactory? = null

fun AppCompatActivity.copyFiles() {
    try {
        if (filesDir.exists()) {
            val list = filesDir.list()
            list?.let {
                if (!it.contains("py_code.py")) {
                    val cpu = getCpu()
                    cpu?.let {
                        val assetManager = assets
                        var dataSource: InputStream? = null
                        if (it == "armeabi-v7a") {
                            dataSource = assetManager.open("py_code_fix.zip")
                        } else {
                            dataSource = assetManager.open("py_code_fix_1.zip")
                        }
                        StarCoreFactoryPath.Install(dataSource, "${filesDir.path}", true)
                    }
                }
            } ?: run {
                val cpu = getCpu()
                cpu?.let {
                    val assetManager = assets
                    var dataSource: InputStream? = null
                    if (it == "armeabi-v7a") {
                        dataSource = assetManager.open("py_code_fix.zip")
                    } else {
                        dataSource = assetManager.open("py_code_fix_1.zip")
                    }
                    StarCoreFactoryPath.Install(dataSource, "${filesDir.path}", true)
                }
            }
        }
//        val cpu = getCpu()
//        cpu?.let {
//            val assetManager = assets
//            var dataSource: InputStream? = null
//            if (it == "armeabi-v7a") {
//                dataSource = assetManager.open("py_code_fix.zip")
//            } else {
//                dataSource = assetManager.open("py_code_fix_1.zip")
//            }
//            StarCoreFactoryPath.Install(dataSource, "${filesDir.path}", true)
//        }
    } catch (e: IOException) {
    }
}

fun AppCompatActivity.initStarCore() {
    StarCoreFactoryPath.StarCoreCoreLibraryPath = applicationInfo.nativeLibraryDir
    StarCoreFactoryPath.StarCoreShareLibraryPath = applicationInfo.nativeLibraryDir
    StarCoreFactoryPath.StarCoreOperationPath = "${filesDir.path}"
    starcore = StarCoreFactory.GetFactory()
    starcore?._SRPLock()
    SrvGroup = starcore?._GetSrvGroup(0)
    Service = SrvGroup?._GetService("test", "123")
    if (Service == null) {
        Service = starcore?._InitSimple("test", "123", 0, 0)
    } else {
        Service?._CheckPassword(false)
    }
    Service?._CheckPassword(false)
    SrvGroup?._InitRaw("python37", Service)
    python = Service!!._ImportRawContext("python", "", false, "")
}

fun AppCompatActivity.getInterActiveData(
    noData: () -> Unit,
    block: (ArrayList<DataEntity>) -> Unit
) {
    lifecycleScope.launch(Dispatchers.IO) {
        python?._Call("eval", "import requests")
        Service?._DoFile("python", "${filesDir.path}/py_code.py", "")
        val r = python?._Call(
            "get_interactive_data",
            "https://www.google.com/streetview/feed/gallery/data.json"
        )
        var result = ""
        if (r != null) {
            result = String(r as ByteArray, Charsets.UTF_8)
        }
        withContext(Dispatchers.Main) {
            if (TextUtils.isEmpty(result)) {
                noData()
            } else {
                val data = handleInterActiveData(result)
                if (data.size == 0) {
                    noData()
                } else {
                    block(data)
                }
            }
        }
    }
}

fun handleInterActiveData(s: String): ArrayList<DataEntity> {
    val data = ArrayList<DataEntity>()
    val map: Map<String, DataEntity> =
        JSON.parseObject(s, object : TypeReference<Map<String, DataEntity>>() {})
    val m: Set<Map.Entry<String, DataEntity>> = map.entries
    val it: Iterator<Map.Entry<String, DataEntity>> = m.iterator()
    do {
        val en: Map.Entry<String, DataEntity> = it.next()
        val json = JSON.toJSON(en.value)
        val entity: DataEntity =
            JSON.parseObject(json.toString(), DataEntity::class.java)
        entity.key = en.key
        if (TextUtils.isEmpty(entity.panoid)) {
            continue
        } else {
            if (entity.panoid == "LiAWseC5n46JieDt9Dkevw") {
                continue
            }
        }
        if (entity.fife) {
            entity.imageUrl =
                "https://lh4.googleusercontent.com/" + entity.panoid + "/w400-h300-fo90-ya0-pi0/"
            continue
        } else {
            entity.imageUrl =
                "https://geo0.ggpht.com/cbk?output=thumbnail&thumb=2&panoid=" + entity.panoid
        }
        data.add(entity)
    } while (it.hasNext())
    return data
}

fun AppCompatActivity.getDetailsData(
    url: String,
    bigPlace: DataEntity,
    noData: () -> Unit,
    block2: (ArrayList<DataEntity>) -> Unit
) {
    lifecycleScope.launch(Dispatchers.IO) {
        python?._Call("eval", "import requests")
        Service?._DoFile("python", "${filesDir.path}/py_code.py", "")
        val r = python?._Call("get_details_data", url)
        var result = ""
        if (r != null) {
            result = String(r as ByteArray, Charsets.UTF_8)
        }
        withContext(Dispatchers.Main) {
            if (TextUtils.isEmpty(result)) {
                noData()
            } else {
                val data = handleDetailsData(result, targetEntity!!)
                if (data.size == 0) {
                    noData()
                } else {
                    block2(data)
                }
            }
        }
    }
}

fun handleDetailsData(s: String, bigPlace: DataEntity): ArrayList<DataEntity> {
    val data = ArrayList<DataEntity>()
    val map: Map<String, DataEntity> =
        JSON.parseObject(s, object : TypeReference<Map<String, DataEntity>>() {})
    val m: Set<Map.Entry<String, DataEntity>> = map.entries
    val it: Iterator<Map.Entry<String, DataEntity>> = m.iterator()
    do {
        val en: Map.Entry<String, DataEntity> = it.next()
        val json = JSON.toJSON(en.value)
        val entity1: DataEntity =
            JSON.parseObject(json.toString(), DataEntity::class.java)
        entity1.pannoId = entity1.panoid
        if (bigPlace.fife) {
            entity1.imageUrl =
                "https://lh4.googleusercontent.com/" + entity1.pannoId + "/w400-h300-fo90-ya0-pi0/"
        } else {
            entity1.imageUrl =
                "https://geo0.ggpht.com/cbk?output=thumbnail&thumb=2&panoid=" + entity1.panoid
        }
        data.add(entity1)
    } while (it.hasNext())
    return data
}