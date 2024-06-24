


scalaVersion := "2.13.12"


name := "hello-world"
organization := "ch.epfl.scala"
version := "1.0"




libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.2.0",
  "com.google.code.gson" % "gson" % "2.8.8"
)
libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "2.3.0"


