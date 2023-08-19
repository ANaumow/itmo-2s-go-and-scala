ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

scalafmtOnCompile := true

lazy val root = (project in file("."))
  .settings(
    name := "test",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % Test
  )


