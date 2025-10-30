package com.immortalidiot.main.graphql

import com.immortalidiot.api.dto.TrackRequest
import com.immortalidiot.api.dto.TrackResponse
import com.netflix.graphql.dgs.*
import graphql.schema.DataFetchingEnvironment
import com.immortalidiot.main.service.TrackService

@DgsComponent
class TrackDataFetcher(private val trackService: TrackService) {
    @DgsQuery
    fun trackById(@InputArgument id: Long) = trackService.getTrackById(id)

    @DgsQuery
    fun tracks(@InputArgument page: Int = 0, @InputArgument size: Int = 10) =
        trackService.getAllTracks(page, size)

    @DgsMutation
    fun createTrack(@InputArgument("input") input: Map<String, Any?>) = trackService.createTrack(
        trackRequest = TrackRequest(
            title = input.getValue("title").toString(),
            author = input.getValue("author").toString(),
            byteSequence = input.getValue("byteSequence").toString(),
            continuation = input.getValue("continuation").toString().toInt()
        )
    )

    @DgsMutation
    fun archiveTrack(@InputArgument id: Long) = trackService.archiveTrack(id)

    @DgsMutation
    fun deleteTrack(@InputArgument id: Long): Long {
        trackService.deleteTrack(id)
        return id
    }

    @DgsData(parentType = "Track", field = "author")
    fun author(dfe: DataFetchingEnvironment) = dfe.getSource<TrackResponse>()!!.author
}

