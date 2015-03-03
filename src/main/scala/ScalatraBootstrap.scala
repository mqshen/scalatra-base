import com.ynet.{DatabaseConfig, SpringScalatraBootstrap}

/**
 * ScalatraBootstrap
 * @author sunghyouk.bae@gmail.com
 */
class ScalatraBootstrap extends SpringScalatraBootstrap {

  override def configClasses: Array[Class[_]] = {
    Array(classOf[DatabaseConfig])
  }

}
