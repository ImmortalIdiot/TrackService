package com.immortalidiot.statistics.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.AcknowledgeMode
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitConfig {

    @Bean
    fun messageConverter(): Jackson2JsonMessageConverter {
        return Jackson2JsonMessageConverter(ObjectMapper().findAndRegisterModules())
    }

    @Bean
    fun statisticsQueue() = Queue(QUEUE_NAME, true)

    @Bean
    fun statisticsExchange() = TopicExchange(EXCHANGE_NAME)

    @Bean
    fun bindingStatisticsQueue(queue: Queue, exchange: TopicExchange) =
        BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_TRACK_CREATED)

    @Bean
    fun containerFactory(connectionFactory: ConnectionFactory): SimpleRabbitListenerContainerFactory {
        val factory = SimpleRabbitListenerContainerFactory()

        factory.setConnectionFactory(connectionFactory)
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL)

        return factory
    }

    companion object {
        internal const val QUEUE_NAME: String = "statistics-queue"
        internal const val EXCHANGE_NAME: String = "tracks-exchange"
        internal const val ROUTING_KEY_TRACK_CREATED: String = "track.created"
    }
}