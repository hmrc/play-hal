import PlayCrossCompilation._
import uk.gov.hmrc.SbtArtifactory
import uk.gov.hmrc.SbtAutoBuildPlugin.autoSourceHeader

name := "play-hal"

lazy val simpleReactiveMongo = (project in file("."))
  .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning, SbtArtifactory)
  .settings(
    majorVersion := 1,
    autoSourceHeader := false,
    makePublicallyAvailableOnBintray := true,
    scalaVersion := "2.11.12",
    libraryDependencies ++= Dependencies(),
    resolvers += Resolver.typesafeRepo("releases"),
    crossScalaVersions := Seq("2.11.12"),
    playCrossCompilationSettings
  )