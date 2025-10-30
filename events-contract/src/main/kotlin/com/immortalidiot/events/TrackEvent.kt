package com.immortalidiot.events

sealed interface TrackEvent : Event {

    data class TrackCreatedEvent(
        val trackId: Long,
        val title: String,
        val author: String
    ) : TrackEvent

    data class TrackDeletedEvent(
        val trackId: Long
    ) : TrackEvent
}
