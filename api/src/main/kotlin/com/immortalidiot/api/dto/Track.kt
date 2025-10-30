package com.immortalidiot.api.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation
import java.time.LocalDateTime

data class TrackRequest(
    @NotBlank val title: String,
    @NotBlank val author: String,
    @NotBlank val byteSequence: String,
    @Min(10) val continuation: Int,
)

data class TrackStatusChangeRequest(
    @NotNull val trackId: Long,
    @NotNull val status: TrackStatus,
)

@Relation(collectionRelation = "tracks", itemRelation = "track")
data class TrackResponse(
    val id: Long,
    val title: String,
    val author: String,
    val byteSequence: String,
    val continuation: Int,
    var status: TrackStatus,
    val createdDate: LocalDateTime,
) : RepresentationModel<TrackResponse>()
