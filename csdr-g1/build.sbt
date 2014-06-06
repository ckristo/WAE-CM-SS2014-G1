name := "csdr-g1"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaJpa, 
  "org.hibernate" % "hibernate-entitymanager" % "4.2.13.Final",
  "mysql" % "mysql-connector-java" % "5.1.30",
  cache
)

play.Project.playJavaSettings
