package com.immortalidiot.statistics.listeners

import com.immortalidiot.events.TrackEvent
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.support.AmqpHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Component
import com.rabbitmq.client.Channel
import org.springframework.messaging.handler.annotation.Payload
import com.immortalidiot.statistics.config.RabbitConfig

@Component
class TrackStatisticsListener {

    private val trackMap = mutableMapOf<Long, String>()

    @RabbitListener(queues = [RabbitConfig.QUEUE_NAME])
    fun onTrackCreated(
        @Payload event: TrackEvent.TrackCreatedEvent,
        channel: Channel,
        @Header(AmqpHeaders.DELIVERY_TAG) tag: Long
    ) {
        try {
            trackMap[event.trackId] = event.title
            println("Total tracks: ${trackMap.size}")

            channel.basicAck(tag, false)
        } catch (e: Exception) {
            channel.basicNack(tag, false, false)
            println("Failed to process message: ${e.message}")
        }
    }
}
