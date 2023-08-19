ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name                                   := "hw7",
    libraryDependencies += "org.typelevel" %% "cats-effect" % "3.4.8",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest"                     % "3.2.15" % Test,
      "org.scalamock" %% "scalamock"                     % "5.2.0"  % Test,
      "org.typelevel" %% "cats-effect-testing-scalatest" % "1.5.0"  % Test,
      "org.typelevel" %% "cats-core"                     % "2.9.0",
      "tf.tofu"       %% "tofu-core-ce3"                 % "0.11.1"
    ),
    scalacOptions ++= Seq(
      "-deprecation",
      "-encoding",
      "utf-8",
      "-explaintypes",
      "-feature",
      "-unchecked",
      "-Xcheckinit",
      "-Xlint:adapted-args",
      "-Xlint:constant",
      "-Xlint:delayedinit-select",
      "-Xlint:inaccessible",
      "-Xlint:infer-any",
      "-Xlint:missing-interpolator",
      "-Xlint:nullary-unit",
      "-Xlint:option-implicit",
      "-Xlint:package-object-classes",
      "-Xlint:poly-implicit-overload",
      "-Xlint:private-shadow",
      "-Xlint:stars-align",
      "-Xlint:type-parameter-shadow",
      "-Ywarn-dead-code",
      "-Ywarn-extra-implicit",
      "-Ywarn-numeric-widen",
      "-Ywarn-unused:implicits",
      "-Ywarn-unused:imports",
      "-Ywarn-unused:locals",
      "-Ywarn-unused:params",
      "-Ywarn-unused:patvars",
      "-Ywarn-value-discard",
      "-Ywarn-unused:privates"
    ),
    addCompilerPlugin("org.typelevel" % "kind-projector"     % "0.13.2" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy"   %% "better-monadic-for" % "0.3.1")
  )
