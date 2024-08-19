package com.guayaba.youtubeMusicDownload

import com.guayaba.youtubeMusicDownload.YoutubeDownloader.Companion.downloadSongsFromFile


fun main() {
    println("please insert the path for the song list (must be .txt file): ")
    val filePath = readln()
    println("please insert the output path for the songs: ")
    var outputPath = readln()

    val logger = Logger("application.log")
    downloadSongsFromFile(filePath, outputPath, logger)
}






