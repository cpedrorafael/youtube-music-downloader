package com.guayaba.youtubeMusicDownload

import com.guayaba.youtubeMusicDownload.YoutubeDownloader.Companion.downloadSongsFromFile
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


suspend fun main() {
    val logger = Logger("application.log")

    println("please insert the working directory (must be contain a list.txt file with the song names): ")

    val filePath = readln()
    try {
        val deferred = AsyncManager.scope.async {
            downloadSongsFromFile(filePath, logger)
        }
        deferred.await()
    } catch (_: Exception) {

    } finally {
        logger.close()
    }

}






