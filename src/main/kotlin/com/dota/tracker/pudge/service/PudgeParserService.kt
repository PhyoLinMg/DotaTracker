package com.dota.tracker.pudge.service

import com.dota.tracker.pudge.parser.PudgePlayParser
import com.dota.tracker.pudge.repository.HookStatsRepository
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream
import org.springframework.stereotype.Service
import skadistats.clarity.processor.runner.SimpleRunner
import skadistats.clarity.source.InputStreamSource
import java.net.HttpURLConnection
import java.net.URL

@Service
class PudgeParserService(
    private val hookStatsRepository: HookStatsRepository
) {
    fun parseReplayFromUrl(url: String): Map<String, Any> {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 30000
        connection.readTimeout = 60000

        return connection.inputStream.use { compressedStream ->
            // Create InputStreamSource from the remote stream
            val decompressedStream = BZip2CompressorInputStream(compressedStream)

            decompressedStream.use { demStream ->
                val source= InputStreamSource(demStream)
                // Create a parser to collect data
                val parser = PudgePlayParser()

                // Parse the replay using SimpleRunner
                SimpleRunner(source).runWith(parser)

                // Return the collected data
                parser.getReplayData()
            }
        }
    }
}