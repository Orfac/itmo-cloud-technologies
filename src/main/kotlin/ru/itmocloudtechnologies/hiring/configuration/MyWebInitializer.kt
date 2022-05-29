package ru.itmocloudtechnologies.hiring.configuration

import org.springframework.web.filter.HiddenHttpMethodFilter
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer
import javax.servlet.ServletContext
import javax.servlet.ServletException

class MyWebInitializer :
    AbstractAnnotationConfigDispatcherServletInitializer() {
    override fun getRootConfigClasses(): Array<Class<*>>? {
        return null
    }

    override fun getServletConfigClasses(): Array<Class<*>> {
        return arrayOf(WebConfig::class.java)
    }

    override fun getServletMappings(): Array<String> {
        return arrayOf("/")
    }

//    @Throws(ServletException::class)
//    override fun onStartup(servletContext: ServletContext) {
//        super.onStartup(servletContext)
//        registerHiddenFieldFilter(servletContext)
//    }
//
//    private fun registerHiddenFieldFilter(aContext: ServletContext) {
//        aContext.addFilter(
//            "hiddenHttpMethodFilter",
//            HiddenHttpMethodFilter()).addMappingForUrlPatterns(null, true, "/*")
//    }
}