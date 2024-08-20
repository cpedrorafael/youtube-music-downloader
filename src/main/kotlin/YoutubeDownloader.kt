package com.guayaba.youtubeMusicDownload

import com.guayaba.youtubeMusicDownload.UrlScraper.Companion.findVideoUrl
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.withPermit
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.PosixFilePermissions

class YoutubeDownloader {
    companion object {
        private fun setExecutablePermission(path: String) {
            val filePath = Paths.get(path)
            Files.setPosixFilePermissions(filePath, PosixFilePermissions.fromString("rwxr-xr-x"))
        }

        private fun getExecutablePath(): String {
            val url = YoutubeDownloader::class.java.getResource("/yt-dlp_macos")
            if (url != null) {
                val path = File(url.toURI()).absolutePath
                setExecutablePermission(path)
                return path
            }
            throw Exception("Yt-dlp executable not found")
        }

        private fun downloadVideo(videoUrl: String, title: String, outputPath: String, logger: Logger? = null) {
            println("\nAttempting to download $title.mp3")
            try {
                val ytDlpPath = getExecutablePath()
                // Format the output filename directly in Kotlin using the title variable
                val outputFile = "$outputPath%s.%%(ext)s".format(title.replace("/", "")) // replace slash for filename safety
                val ytDlpCmd = arrayOf(
                    ytDlpPath, videoUrl,
                    "-f", "ba", // Best audio
                    "-x", // Extract audio only
                    "--audio-format", "mp3", // Convert to mp3
                    videoUrl,
                    "-o", outputFile
                )
                val runtime = Runtime.getRuntime()
                val process = runtime.exec(ytDlpCmd)

                process.inputStream.bufferedReader().use {
                    it.lines().forEach { line -> println(line) }
                }
                process.waitFor()
                if (process.exitValue() != 0) {
                    throw RuntimeException("yt-dlp command failed with exit code ${process.exitValue()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                logger?.logException(e)
            }
        }

        private fun readLinesFromFile(filePath: String): List<String> {
            return File(filePath).readLines()
        }
        suspend fun downloadSongsFromFile(workingDir: String, logger: Logger) {

            var outputPath = workingDir
            if(workingDir.last() == '/') {
                outputPath = outputPath.substring(workingDir.indices)
            }

            val listFile = File("$outputPath/list.txt")

            if(!listFile.exists()){
                throw IllegalStateException("list.txt file cannot be found in directory")
            }

            val songs = readLinesFromFile(listFile.absolutePath)

            val jobs = songs.map { song ->
                 AsyncManager.scope.launch {
                     AsyncManager.semaphore.withPermit {
                         val songUrl = findVideoUrl(song)
                         if(songUrl.isNullOrEmpty()) {
                             logger.logException(java.lang.Exception("URL for song $song not found"))
                         }else {
                             downloadVideo(songUrl, song, "$outputPath/", logger)
                         }
                     }
                }
            }

            jobs.joinAll()
        }
    }
}