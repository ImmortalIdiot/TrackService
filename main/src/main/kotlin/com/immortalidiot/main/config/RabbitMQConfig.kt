package com.immortalidiot.main.config

import org.springframework.amqp.core.TopicExchange
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {
    @Bean
    fun tracksExchange() : TopicExchange {
        return TopicExchange(EXCHANGE_NAME)
    }

    companion object {
        const val EXCHANGE_NAME: String = "tracks-exchange"
        const val ROUTING_KEY_TRACK_CREATED: String = "track.created"
        const val ROUTING_KEY_TRACK_DELETED: String = "track.deleted"
    }
}
