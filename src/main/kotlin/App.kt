package com.guayaba.youtubeMusicDownload

import com.guayaba.youtubeMusicDownload.YoutubeDownloader.Companion.downloadSongsFromFile
import com.guayaba.youtubeMusicDownload.YoutubeDownloader.Companion.downloadVideosFromFile
import kotlinx.coroutines.async


suspend fun main() {
    val logger = Logger("application.log")

    println("""
        please choos e mode (or enter to use default mode):
        1- Music (default)
        2- Videos
    """.trimIndent())
    val mode: Int = readln().toInt()

    println("please insert the working directory (must be contain a list.txt file with the song names, or video URLs): ")

    val filePath = readln()

    try {
        val deferred = AsyncManager.scope.async {
            when(mode){
                1 -> downloadSongsFromFile(filePath, logger)
                2 -> downloadVideosFromFile(filePath, logger)
                else -> throw IllegalStateException("Invalid mode")
            }
        }
        deferred.await()
    } catch (e: Exception) {
        logger.logException(e)
    } finally {
        logger.close()
    }
}






