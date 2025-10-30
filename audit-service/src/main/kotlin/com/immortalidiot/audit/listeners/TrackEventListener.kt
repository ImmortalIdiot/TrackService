package com.immortalidiot.audit.listeners

import com.immortalidiot.events.TrackEvent
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.Exchange
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.QueueBinding
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component


@Component
class TrackEventListener {

    @RabbitListener(
        bindings = [QueueBinding(
            value = Queue(name = QUEUE_CREATED_NAME, durable = "true"),
            exchange = Exchange(name = EXCHANGE_NAME, type = "topic"),
            key = arrayOf(ROUTING_KEY_TRACK_CREATED)
        )]
    )
    fun handleTrackCreatedEvent(event: TrackEvent.TrackCreatedEvent) {
        LOG.info("Received new track event: $event")
        // Здесь могла бы быть логика аудита или уведомлений
    }

    @RabbitListener(
        bindings = [QueueBinding(
            value = Queue(name = QUEUE_DELETED_NAME, durable = "true"),
            exchange = Exchange(name = EXCHANGE_NAME, type = "topic"),
            key = arrayOf(ROUTING_KEY_TRACK_DELETED)
        )]
    )
    fun handleTrackDeletedEvent(event: TrackEvent.TrackDeletedEvent) {
        LOG.info("Track deleted with id: ${event.trackId}")
        // Здесь могла бы быть логика аудита или уведомлений
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(TrackEventListener::class.java)

        const val QUEUE_CREATED_NAME: String = "notification-created-queue"
        const val QUEUE_DELETED_NAME: String = "notification-deleted-queue"
        const val EXCHANGE_NAME: String = "tracks-exchange"
        const val ROUTING_KEY_TRACK_CREATED: String = "track.created"
        const val ROUTING_KEY_TRACK_DELETED: String = "track.deleted"
    }
}
