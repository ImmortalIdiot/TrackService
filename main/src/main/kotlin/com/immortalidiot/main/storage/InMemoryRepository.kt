package com.immortalidiot.main.storage

import com.immortalidiot.api.dto.TrackRequest
import com.immortalidiot.api.dto.TrackResponse
import com.immortalidiot.api.dto.TrackStatus
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import kotlin.random.Random

@Component
internal class InMemoryRepository {
    private val trackIdSequence = AtomicLong(1)
    private val tracks: MutableMap<Long, TrackResponse> = ConcurrentHashMap()

    @PostConstruct
    fun init() {
        createTrack(
            TrackRequest(
                title = "Song One",
                author = "Artist1",
                byteSequence = generateRandomBytesBase64(randomBytesSize()),
                continuation = 180
            ),
            createdDate = LocalDateTime.now().minusYears(2).minusMonths(3).minusDays(5)
        )

        createTrack(
            TrackRequest(
                title = "Song Two",
                author = "Artist1",
                byteSequence = generateRandomBytesBase64(randomBytesSize()),
                continuation = 240
            ),
            createdDate = LocalDateTime.now().minusYears(3).minusMonths(1).minusDays(2)
        )

        createTrack(
            TrackRequest(
                title = "Song Three",
                author = "Singers Band",
                byteSequence = generateRandomBytesBase64(randomBytesSize()),
                continuation = 300
            ),
            createdDate = LocalDateTime.now().minusMonths(6).minusDays(12)
        )

        archiveTrack(3)
    }

    fun createTrack(req: TrackRequest, createdDate: LocalDateTime = LocalDateTime.now()): TrackResponse {
        val id = trackIdSequence.getAndIncrement()
        val response = TrackResponse(
            id = id,
            title = req.title,
            author = req.author,
            byteSequence = req.byteSequence,
            continuation = req.continuation,
            status = TrackStatus.ACTIVE,
            createdDate = createdDate
        )
        tracks[id] = response
        return response
    }

    fun getTrackById(id: Long): TrackResponse? = tracks[id]

    fun getAllTracks(): List<TrackResponse> = tracks.values.toList()

    fun archiveTrack(id: Long): TrackResponse? {
        val track = tracks[id] ?: return null
        val newTrack = track.copy(status = TrackStatus.ARCHIVED)
        tracks[id] = newTrack
        return newTrack
    }

    fun deleteTrack(id: Long): TrackResponse? {
        val track = tracks[id] ?: return null
        val newTrack = track.copy(status = TrackStatus.DELETED)
        tracks[id] = newTrack
        return newTrack
    }

    fun generateRandomBytesBase64(size: Int): String {
        val bytes = ByteArray(size)
        Random.nextBytes(bytes)
        return Base64.getEncoder().encodeToString(bytes)
    }

    private fun randomBytesSize(): Int = (700..1600).random()
}
