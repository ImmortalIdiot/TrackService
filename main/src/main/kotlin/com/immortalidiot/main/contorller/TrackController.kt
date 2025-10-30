package com.immortalidiot.main.contorller

import com.immortalidiot.api.TrackApi
import com.immortalidiot.api.dto.TrackRequest
import com.immortalidiot.api.dto.TrackResponse
import com.immortalidiot.main.assembler.TrackModelAssembler
import com.immortalidiot.main.service.TrackService
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class TrackController(
    private val trackService: TrackService,
    private val trackModelAssembler: TrackModelAssembler,
    private val pagedResourceAssembler: PagedResourcesAssembler<TrackResponse>
) : TrackApi {

    override fun createTrack(trackRequest: TrackRequest): ResponseEntity<EntityModel<TrackResponse>> {
        val track = trackService.createTrack(trackRequest)
        val entityModel = trackModelAssembler.toModel(track)

        return ResponseEntity
            .created(entityModel.getRequiredLink("self").toUri())
            .body(entityModel)
    }

    override fun getTrackById(id: Long): EntityModel<TrackResponse> {
        val track = trackService.getTrackById(id)
        return trackModelAssembler.toModel(track)
    }

    override fun getAllTracks(page: Int, size: Int): PagedModel<EntityModel<TrackResponse>> {
        val trackPage = with(trackService.getAllTracks(page, size)) {
            PageImpl(
                content,
                PageRequest.of(pageNumber, pageSize),
                totalElements
            )
        }

        return pagedResourceAssembler.toModel(trackPage, trackModelAssembler)
    }

    override fun deleteTrack(id: Long) {
        trackService.deleteTrack(id)
    }

    override fun archiveTrack(id: Long): EntityModel<TrackResponse> {
        val track = trackService.archiveTrack(id)
        return trackModelAssembler.toModel(track)
    }
}
