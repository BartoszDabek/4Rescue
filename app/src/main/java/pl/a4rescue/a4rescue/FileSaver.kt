package pl.a4rescue.a4rescue

import android.os.Environment
import android.util.Log
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.*

object FileSaver {

    var buf: BufferedWriter? = null

    fun init() {
        val file = getPublicDownloadStorageDir("g-forces")
        val myFile = File(file, Calendar.getInstance().time.toString() + ".txt")
        buf = BufferedWriter(FileWriter(myFile))
    }

    fun getPublicDownloadStorageDir(albumName: String): File? {
        val file = File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), albumName)
        if (!file.mkdirs()) {
            Log.e("Test", "Directory not created")
        }
        return file
    }

    fun write(text: String) {
        buf?.append(text)
        buf?.newLine()
    }

    fun close() {
        buf?.close()
    }
}