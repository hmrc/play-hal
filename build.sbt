import Dependencies.*
import sbt.*
import sbt.Keys.*

val libName = "play-hal"

val scala2_12 = "2.12.18"
val scala2_13 = "2.13.12"

Global / concurrentRestrictions += Tags.limitSum(1, Tags.Test, Tags.Untagged)

ThisBuild / majorVersion      := 4
ThisBuild / isPublicArtefact  := true
ThisBuild / scalaVersion      := scala2_13
ThisBuild / organization      := "uk.gov.hmrc"
ThisBuild / libraryDependencySchemes ++= Seq("org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always)

lazy val library = Project(libName, file("."))
  .settings(publish / skip := true)
  .aggregate(play28, play29, play30)

def copyPlay30Sources(module: Project) =
  CopySources.copySources(
    module,
    transformSource   = _.replace("org.apache.pekko", "akka"),
    transformResource = _.replace("pekko", "akka")
  )

lazy val play28 = Project(s"$libName-play-28", file(s"$libName-play-28"))
  .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning)
  .settings(
    libraryDependencies ++= compile28 ++ test28,
    crossScalaVersions := Seq(scala2_12, scala2_13),
    copyPlay30Sources(play30)
  )

lazy val play29 = Project(s"$libName-play-29", file(s"$libName-play-29"))
  .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning)
  .settings(
    libraryDependencies ++= compile29 ++ test29,
    crossScalaVersions := Seq(scala2_13),
    copyPlay30Sources(play30)
  )

lazy val play30 = Project(s"$libName-play-30", file(s"$libName-play-30"))
  .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning)
  .settings(
    libraryDependencies ++= compile30 ++ test30,
    crossScalaVersions := Seq(scala2_13)
  )
