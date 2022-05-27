package ru.itmocloudtechnologies.hiring.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer

@Configuration
open class MyWebInitializer : AbstractAnnotationConfigDispatcherServletInitializer() {
    override fun getServletMappings(): Array<String> {
        return arrayOf("/*")
    }

    override fun getRootConfigClasses(): Array<Class<*>>? {
        return null
    }

    override fun getServletConfigClasses(): Array<Class<*>>? {
        return emptyArray()
    }
}