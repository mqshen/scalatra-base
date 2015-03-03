import play.twirl.sbt.SbtTwirl
import sbt.Keys._
import sbt._

object MyBuild extends Build {
  val Organization = "com.ynet"
  val Name = "scalatra-base"
  val Version = "0.0.1"
  val ScalaVersion = "2.11.2"
  val ScalatraVersion = "2.3.0"


//  import java.io.File
//  import com.yahoo.platform.yui.compressor.YUICompressor
//
//  val assetsPath = "src/main/webapp/assets/"
//  val libPath = assetsPath + "vendors/"
//  val sourceJS = Array("jquery/dist/jquery.js", "WeakMap/weakmap.js", "MutationObservers/MutationObserver.js")
//  val sourcePaths = sourceJS.map { js =>
//    new File(libPath, js).getPath
//  }
//  val dest = new File(assetsPath, "script-min.js")
//  YUICompressor.main(Array("-o", dest.getPath) ++ sourcePaths)
  
  val springVersion = "4.1.4.RELEASE"
  val hibernateVersion = "4.1.6.Final"
  val springDataVersion = "1.7.1.RELEASE"

  val springs = Seq(
    "spring-orm",
    "spring-oxm",
    "spring-jdbc",
    "spring-context-support",
    "spring-jms").map { dep =>
    "org.springframework" % dep % springVersion
  }

  val hibernate = Seq(
    "hibernate-entitymanager",
    "hibernate-ehcache",
    "hibernate-core").map { dep =>
    "org.hibernate" % dep % hibernateVersion
  }

  val depends = Seq(
    "org.scalatra" %% "scalatra" % ScalatraVersion,
    "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
    "org.scalatra" %% "scalatra-atmosphere" % ScalatraVersion,
    "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
    "org.scalatra" %% "scalatra-json" % ScalatraVersion,
    "org.json4s" %% "json4s-jackson" % "3.2.10",
    "commons-io" % "commons-io" % "2.4",
    "commons-lang" % "commons-lang" % "2.6",
    "commons-dbcp" % "commons-dbcp" % "1.4",
    "org.apache.commons" % "commons-compress" % "1.5",
    "org.apache.commons" % "commons-email" % "1.3.1",
    "commons-fileupload" % "commons-fileupload" % "1.3.1",
    "org.apache.httpcomponents" % "httpclient" % "4.3",
    "org.apache.sshd" % "apache-sshd" % "0.11.0",
    "mysql" % "mysql-connector-java" % "5.1.29",
    "net.debasishg" %% "redisclient" % "2.13",
    "org.springframework.data" % "spring-data-jpa" % springDataVersion,
    "org.hibernate.javax.persistence" % "hibernate-jpa-2.0-api" % "1.0.1.Final",
    "ch.qos.logback" % "logback-classic" % "1.0.13" % "runtime",
    "javax.servlet" % "javax.servlet-api" % "3.1.0",
    "junit" % "junit" % "4.11" % "test",
    "com.typesafe.play" %% "twirl-compiler" % "1.0.2"
  ) ++ springs ++ hibernate

  lazy val project = Project (
    "scalatra-base",
    file(".")
  )
  .settings(
    sourcesInBase := false,
    organization := Organization,
    name := Name,
    version := Version,
    scalaVersion := ScalaVersion,
    resolvers ++= Seq(
      "Local Maven Repository" at "file://"+ Path.userHome.absolutePath + "/.m2/repository",
      "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases",
      "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
      Classpaths.typesafeReleases,
      "amateras-repo" at "http://amateras.sourceforge.jp/mvn/"
    ),
    scalacOptions := Seq("-deprecation", "-language:postfixOps"),
    libraryDependencies ++= depends,
    javacOptions in compile ++= Seq("-target", "6", "-source", "6"),
    testOptions in Test += Tests.Argument(TestFrameworks.Specs2, "junitxml", "console"),
    packageOptions += Package.MainClass("JettyLauncher"),
    dependencyClasspath in Runtime ++= {
      val base = baseDirectory.value
      val baseDirectories = (base / "embed-jetty")
      val customJars = (baseDirectories ** "*.jar")
      customJars.classpath
    },
    unmanagedResourceDirectories in Compile := List(file("src/main/webapp"), file("src/main/resources"))
    ).enablePlugins(SbtTwirl)
}

