package com.guayaba.youtubeMusicDownload

import java.io.File
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.Date

class Logger(fileName: String) {
    private val logFile = PrintWriter(File(fileName), "UTF-8")

    fun logException(e: Exception) {
        val timeStamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
        logFile.println("[$timeStamp] Exception: ${e.message}")
        e.stackTrace.forEach { logFile.println(it) }
        logFile.flush()
    }

    fun close() {
        logFile.close()
    }
}
