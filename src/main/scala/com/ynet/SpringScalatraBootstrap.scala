package com.ynet


import javax.servlet.ServletContext

import com.ynet.annotations.ServletPath
import com.ynet.web.controller.BeerController
import org.scalatra.{ScalatraServlet, LifeCycle}
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext

import scala.collection.JavaConverters._
/**
 * Created by goldratio on 3/3/15.
 */
abstract class SpringScalatraBootstrap extends LifeCycle {

  def configClasses: Array[Class[_]]

  var appContext: AnnotationConfigApplicationContext = _

  def loadAppContext() = {
    appContext = new AnnotationConfigApplicationContext(configClasses: _*)
    appContext.scan("com.ynet")
  }

  override def init(context: ServletContext) {
    loadAppContext()

    //context.mount(new BeerController, "/*")
    val resources = appContext.getBeansWithAnnotation(classOf[ServletPath])
    resources.values().asScala.foreach {
      case servlet: ScalatraServlet =>
        val path = servlet.getClass.getAnnotation(classOf[ServletPath]).value()
        println(s"begin bind path $path")
        if (!path.startsWith("/"))
          context.mount(servlet, "/" + path)
        else
          context.mount(servlet, path)
      case _ =>
    }
  }

}
