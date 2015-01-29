name := "play-sentry"

version := "1.0.0"

organization := "ru.purecode"

scalaVersion := "2.11.4"

crossScalaVersions := Seq("2.11.4", "2.10.4")

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play" % "2.3.7" % "provided",
  "com.typesafe.play" %% "play-java" % "2.3.7" % "provided",
  "net.kencochrane.raven" % "raven" % "6.0.0"
)

releaseSettings
