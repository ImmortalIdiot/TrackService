package com.immortalidiot.events

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

sealed interface TrackEvent : Event {
    data class TrackCreatedEvent @JsonCreator constructor(
        @JsonProperty("trackId") val trackId: Long,
        @JsonProperty("title") val title: String,
        @JsonProperty("author") val author: String
    ) : TrackEvent

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class TrackDeletedEvent @JsonCreator constructor(
        @JsonProperty("trackId") val trackId: Long
    ) : TrackEvent
}
