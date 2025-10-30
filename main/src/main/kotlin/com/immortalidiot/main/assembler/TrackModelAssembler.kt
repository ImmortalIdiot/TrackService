package com.immortalidiot.main.assembler

import com.immortalidiot.api.dto.TrackResponse
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.core.DummyInvocationUtils.methodOn
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Component

@Component
class TrackModelAssembler : RepresentationModelAssembler<TrackResponse, EntityModel<TrackResponse>> {
    override fun toModel(job: TrackResponse): EntityModel<TrackResponse> {
        return EntityModel.of(
            job,
            linkTo(methodOn(com.immortalidiot.main.contorller.TrackController::class.java).getTrackById(job.id)).withSelfRel(),
            linkTo(methodOn(com.immortalidiot.main.contorller.TrackController::class.java).getAllTracks(0, 10)).withRel("collection"),
        )
    }
}
