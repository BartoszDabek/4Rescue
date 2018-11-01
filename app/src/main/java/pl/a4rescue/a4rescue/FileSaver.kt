package pl.a4rescue.a4rescue

import android.content.Context
import android.os.Environment
import android.os.storage.StorageVolume
import android.util.Log
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

object FileSaver {

    var buf: BufferedWriter? = null

    fun init(activity: Main2Activity) {
        val file = getPublicDownloadStorageDir(activity, "g-forces")
        val now = Date()
        val format = SimpleDateFormat("yyyy-MM-dd--HH-mm-ss", Locale.ENGLISH).format(now)
        val myFile = File(file, "$format.txt")
        buf = BufferedWriter(FileWriter(myFile))
    }

    fun getPublicDownloadStorageDir(activity: Main2Activity, albumName: String): File? {
        val sdCard = getExternalCardDirectory(activity)
        val dir = File("${sdCard?.absolutePath}/$albumName")
        if (!dir.mkdirs()) {
            Log.e("Test", "Directory not created")
        }
        return dir
    }

    fun write(text: String) {
        buf?.append(text)
        buf?.newLine()
    }

    fun close() {
        buf?.close()
    }

    private fun getExternalCardDirectory(activity: Main2Activity): File? {
        val storageManager = activity.getSystemService(Context.STORAGE_SERVICE)
        try {
            val storageVolumeClazz = Class.forName("android.os.storage.StorageVolume")
            val getVolumeList = storageManager.javaClass.getMethod("getVolumeList")
            val getPath = storageVolumeClazz.getMethod("getPath")
            val isRemovable = storageVolumeClazz.getMethod("isRemovable")
            val result = getVolumeList.invoke(storageManager) as Array<StorageVolume>
            result.forEach {
                if (isRemovable.invoke(it) as Boolean) {
                    return File(getPath.invoke(it) as String)
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null
    }
}