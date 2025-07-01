package com.dota.tracker.pudge.repository

import com.dota.tracker.opendota.model.DotaMatch
import com.dota.tracker.pudge.model.HookStats
import com.dota.tracker.pudge.parser.PudgePlayParser
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream
import org.springframework.stereotype.Repository
import skadistats.clarity.processor.runner.SimpleRunner
import skadistats.clarity.source.InputStreamSource
import java.net.HttpURLConnection
import java.net.URL

@Repository
class PudgeParserRepository{

    fun parseReplayFromUrl(dotaMatch: DotaMatch): HookStats {
        val connection = URL(dotaMatch.replay_url).openConnection() as HttpURLConnection
        try {
            connection.requestMethod = "GET"
            connection.connectTimeout = 60000
            connection.readTimeout = 120000

            return connection.inputStream.use { compressedStream ->
                // Create InputStreamSource from the remote stream
                val decompressedStream = BZip2CompressorInputStream(compressedStream)

                decompressedStream.use { demStream ->
                    val source= InputStreamSource(demStream)
                    // Create a parser to collect data
                    val parser = PudgePlayParser(dotaMatch)

                    // Parse the replay using SimpleRunner
                    SimpleRunner(source).runWith(parser)

                    // Return the collected data
                    parser.getReplayData()
                }
            }
        } finally {
            connection.disconnect()
        }
    }
}