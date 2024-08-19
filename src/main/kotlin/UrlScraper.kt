package com.guayaba.youtubeMusicDownload

import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape

class UrlScraper {
    companion object {
        private fun formatYouTubeSearchQuery(input: String): String {
            return input
                .lowercase() // Convert the string to lower case
                .replace("[^\\w\\s]".toRegex(), "") // Remove all non-word characters except spaces
                .trim() // Remove leading and trailing spaces
                .replace("\\s+".toRegex(), "+") // Replace sequences of whitespace with '+'
        }

        fun findVideoUrl(query: String): String? {

            val searchQuery = formatYouTubeSearchQuery(input = query)

            println("Trying query: $searchQuery")
            val html: String = skrape(HttpFetcher) {
                request {
                    url = "https://www.youtube.com/results?search_query=$searchQuery"
                }
                response {
                    htmlDocument {
                        html
                    }
                }
            }

            // Regex to find all YouTube watch URLs
            val regex = """/watch\?v=[\w-]+""".toRegex()
            val matches = regex.findAll(html).map { it.value }.toSet()

            if (matches.isNotEmpty()) {
                return "https://www.youtube.com${matches.first()}"
            }

            return null
        }
    }
}