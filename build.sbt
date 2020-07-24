import PlayCrossCompilation._
import uk.gov.hmrc.SbtArtifactory
import uk.gov.hmrc.SbtAutoBuildPlugin.forceLicenceHeader

name := "play-hal"

lazy val root = (project in file("."))
  .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning, SbtArtifactory)
  .settings(
    majorVersion := 2,
    forceLicenceHeader := false,
    makePublicallyAvailableOnBintray := true,
    scalaVersion := "2.12.10",
    libraryDependencies ++= Dependencies(),
    resolvers += Resolver.typesafeRepo("releases"),
    crossScalaVersions := Seq("2.11.12", "2.12.10"),
    playCrossCompilationSettings
  )