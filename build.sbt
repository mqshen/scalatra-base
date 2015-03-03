name := "scalatra-base"

version := "0.1.0-SNAPSHOT"

libraryDependencies += "javax.servlet" % "javax.servlet-api" % "3.0.1" % "provided"

tomcat(develop = true, options = new ForkOptions(runJVMOptions = Seq("-Xdebug",
  "-agentlib:jdwp=transport=dt_socket,address=5005,server=y,suspend=n")))

javacOptions ++= Seq("-g")