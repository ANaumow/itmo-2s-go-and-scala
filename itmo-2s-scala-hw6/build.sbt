ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.0-RC4"

lazy val root = (project in file("."))
  .settings(
    name := "hw6",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest"                     % "3.2.15" % Test,
      "org.typelevel" %% "cats-effect-testing-scalatest" % "1.5.0"  % Test,
      "org.typelevel" %% "cats-core"                     % "2.9.0",
      "org.typelevel" %% "cats-effect"                   % "3.4.8"
    ),
    scalacOptions ++= Seq(
      "-Ykind-projector:underscores",
      "-deprecation",
      "-encoding",
      "utf-8",
      "-Werror",
      "-Wunused:imports",
      "-Wunused:privates",
      "-Wunused:locals",
      "-Wunused:explicits",
      "-Wunused:implicits",
      "-Wunused:params",
      "-Wunused:linted",
      "-explaintypes",
      "-feature",
      "-unchecked"
    )
  )
