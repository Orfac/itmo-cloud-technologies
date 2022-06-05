package ru.itmocloudtechnologies.hiring.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence

@EnableWebMvc
@Configuration
@ComponentScan("ru.itmocloudtechnologies.hiring")
@EnableJpaRepositories("ru.itmocloudtechnologies.hiring")
open class WebConfig() : WebMvcConfigurer {
    @Autowired
    lateinit var applicationContext: ApplicationContext

    fun getContext() = applicationContext

    private var entityManagerFactory: EntityManagerFactory = getEMF()

    private fun getEMF(): EntityManagerFactory {
        val env = System.getenv()
        val configOverrides: MutableMap<String, String> = HashMap()
        for (envName  in env.keys) {
            if (envName.contains("SPRING_DATASOURCE_URL")) {
                configOverrides["hibernate.connection.url"]= env[envName]!!
                configOverrides["javax.persistence.jdbc.url"]= env[envName]!!
            }
            // You can put more code in here to populate configOverrides...
        }

        return Persistence.createEntityManagerFactory("worker-unit",configOverrides)
    }

    @Bean
    open fun entityManagerFactory(): EntityManagerFactory {
        return entityManagerFactory
    }

    @Bean("transactionManager")
    open fun dbTransactionManager(): PlatformTransactionManager? {
        val transactionManager = JpaTransactionManager()
        transactionManager.entityManagerFactory = entityManagerFactory
        return transactionManager
    }
}