package ru.itmocloudtechnologies.hiring.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@EnableWebMvc
@Configuration
@ComponentScan("ru.itmocloudtechnologies.hiring")
@EnableJpaRepositories("ru.itmocloudtechnologies.hiring")
open class WebConfig() : WebMvcConfigurer{
    @Autowired
    lateinit var applicationContext: ApplicationContext

    fun getContext() =applicationContext
}