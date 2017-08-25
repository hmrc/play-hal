import uk.gov.hmrc.SbtAutoBuildPlugin.autoSourceHeader

name := "play-hal"

val appDependencies = {
  import Dependencies._

  Seq(
    Compile.play,
    Compile.playJson,

    Test.scalatestplusPlay,
    Test.scalaTest,
    Test.pegdown
  )
}

lazy val simpleReactiveMongo = (project in file("."))
  .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning)
  .settings(
    autoSourceHeader := false,
    scalaVersion := "2.11.8",
    libraryDependencies ++= appDependencies,
    resolvers += Resolver.typesafeRepo("releases"),
    crossScalaVersions := Seq("2.11.8")
  )