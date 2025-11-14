package com.immortalidiot.audit.listeners

import com.immortalidiot.events.TrackEvent
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.*
import org.springframework.amqp.support.AmqpHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component


@Component
class TrackEventListener {

    @RabbitListener(
        bindings = [
            QueueBinding(
                value = Queue(
                    name = QUEUE_NAME,
                    durable = "true",
                    arguments = [
                        Argument(name = "x-dead-letter-exchange", value = DLX_EXCHANGE),
                        Argument(name = "x-dead-letter-routing-key", value = DLQ_ROUTING_KEY)
                    ]
                ),
                exchange = Exchange(
                    name = EXCHANGE_NAME,
                    type = "topic",
                    durable = "true"
                ),
                key = [ROUTING_KEY_TRACK_CREATED]
            )
        ]
    )
    fun handleTrackCreatedEvent(
        @Payload event: TrackEvent.TrackCreatedEvent,
        channel: com.rabbitmq.client.Channel,
        @Header(AmqpHeaders.DELIVERY_TAG) deliveryTag: Long
    ) {
        try {
            log.info("Received TrackCreatedEvent: $event")

            if (event.title.equals("CRASH", ignoreCase = true)) {
                throw RuntimeException("Simulating error for DLQ test")
            }

            log.info("Notification sent for new track '${event.title}'!")
            channel.basicAck(deliveryTag, false)

        } catch (e: Exception) {
            log.error("Failed to process event: $event. Sending to DLQ.", e)
            channel.basicNack(deliveryTag, false, false)
        }
    }

    @RabbitListener(
        bindings = [
            QueueBinding(
                value = Queue(
                    name = QUEUE_NAME,
                    durable = "true",
                    arguments = [
                        Argument(name = "x-dead-letter-exchange", value = DLX_EXCHANGE),
                        Argument(name = "x-dead-letter-routing-key", value = DLQ_ROUTING_KEY)
                    ]
                ),
                exchange = Exchange(
                    name = EXCHANGE_NAME,
                    type = "topic",
                    durable = "true"
                ),
                key = [ROUTING_KEY_TRACK_DELETED]
            )
        ]
    )
    fun handleTrackDeletedEvent(
        @Payload event: TrackEvent.TrackDeletedEvent,
        channel: com.rabbitmq.client.Channel,
        @Header(AmqpHeaders.DELIVERY_TAG) deliveryTag: Long
    ) {
        try {
            log.info("Received TrackDeletedEvent: $event")

            log.info("Notifications cancelled for deleted trackId ${event.trackId}!")
            channel.basicAck(deliveryTag, false)

        } catch (e: Exception) {
            log.error("Failed to process event: $event. Sending to DLQ.", e)
            channel.basicNack(deliveryTag, false, false)
        }
    }

    @RabbitListener(
        bindings = [
            QueueBinding(
                value = Queue(
                    name = DLQ_QUEUE_NAME,
                    durable = "true"
                ),
                exchange = Exchange(
                    name = DLX_EXCHANGE,
                    type = "topic",
                    durable = "true"
                ),
                key = [DLQ_ROUTING_KEY]
            )
        ]
    )
    fun handleDlqMessages(failedMessage: Any) {
        log.error("!!! Received message in DLQ: {}", failedMessage)
        // Логика оповещения администраторов
    }

    companion object {
        private val log = LoggerFactory.getLogger(TrackEventListener::class.java)

        const val EXCHANGE_NAME = "tracks-exchange"
        const val QUEUE_NAME = "notification-queue"

        const val ROUTING_KEY_TRACK_CREATED = "track.created"
        const val ROUTING_KEY_TRACK_DELETED = "track.deleted"

        const val DLQ_QUEUE_NAME = "notification-queue.dlq"
        const val DLX_EXCHANGE = "dlx-exchange"
        const val DLQ_ROUTING_KEY = "dlq.notifications"
    }
}
