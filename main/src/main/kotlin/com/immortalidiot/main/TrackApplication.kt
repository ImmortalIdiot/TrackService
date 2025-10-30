package com.immortalidiot.main

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.hateoas.config.EnableHypermediaSupport

@SpringBootApplication(
    scanBasePackages = ["com.immortalidiot.main", "com.immortalidiot.api"],
    exclude = [DataSourceAutoConfiguration::class]
)
@EnableHypermediaSupport(type = [EnableHypermediaSupport.HypermediaType.HAL])
class TrackApplication

fun main(args: Array<String>) {
    runApplication<TrackApplication>(*args)
}
