package com.immortalidiot.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import com.immortalidiot.api.dto.TrackRequest
import com.immortalidiot.api.dto.TrackResponse
import com.immortalidiot.api.dto.StatusResponse
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "tracks", description = "API for working with music tracks")
@ApiResponse(
    responseCode = "400",
    description = "Error validation",
    content = [Content(mediaType = "application/json", schema = Schema(implementation = StatusResponse::class))]
)
@ApiResponse(
    responseCode = "500",
    description = "Internal error",
    content = [Content(mediaType = "application/json", schema = Schema(implementation = StatusResponse::class))]
)
@RequestMapping("/api/tracks")
interface TrackApi {

    @Operation(summary = "Create new track")
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ApiResponse(responseCode = "201", description = "Track successfully created")
    fun createTrack(@Valid @RequestBody trackRequest: TrackRequest): ResponseEntity<EntityModel<TrackResponse>>

    @Operation(summary = "Find track by id")
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "Track found")
    fun getTrackById(@PathVariable id: Long): EntityModel<TrackResponse>

    @Operation(summary = "Get all tracks")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "Tracks found")
    fun getAllTracks(
        @Parameter(description = "Page number (0..N)")
        @RequestParam(defaultValue = "0")
        page: Int,
        @Parameter(description = "Page size")
        @RequestParam(defaultValue = "10")
        size: Int,
    ): PagedModel<EntityModel<TrackResponse>>

    @Operation(summary = "Delete track (soft delete)")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204", description = "Track removed")
    @ApiResponse(responseCode = "404", description = "Track not found")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTrack(@PathVariable id: Long)

    @Operation(summary = "Archive track (set status to ARCHIVED)")
    @PostMapping("/{id}/archive")
    @ApiResponse(responseCode = "200", description = "Track successfully archived")
    fun archiveTrack(@PathVariable id: Long): EntityModel<TrackResponse>
}
