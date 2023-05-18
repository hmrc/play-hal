import PlayCrossCompilation._
import uk.gov.hmrc.SbtArtifactory
import uk.gov.hmrc.SbtAutoBuildPlugin.forceLicenceHeader

name := "play-hal"

lazy val root = (project in file("."))
  .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning, SbtArtifactory)
  .settings(
    majorVersion := 3,
    forceLicenceHeader := false,
    isPublicArtefact := true,
    scalaVersion := "2.13.8",
    libraryDependencies ++= Dependencies(),
    resolvers += Resolver.typesafeRepo("releases"),
    crossScalaVersions := Seq("2.12.10", "2.13.8"),
    playCrossCompilationSettings
  )