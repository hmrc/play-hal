import Dependencies.*
import sbt.*
import sbt.Keys.*

val libName = "play-hal"
val scala_3 = "3.3.4"

Global / concurrentRestrictions += Tags.limitSum(1, Tags.Test, Tags.Untagged)

ThisBuild / majorVersion      := 4
ThisBuild / isPublicArtefact  := true
ThisBuild / scalaVersion      := scala_3
ThisBuild / organization      := "uk.gov.hmrc"
ThisBuild / libraryDependencySchemes ++= Seq("org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always)

lazy val library = Project(libName, file("."))
  .settings(publish / skip := true)
  .aggregate(play30)

def copyPlay30Sources(module: Project) =
  CopySources.copySources(
    module,
    transformSource   = _.replace("org.apache.pekko", "akka"),
    transformResource = _.replace("pekko", "akka")
  )


lazy val play30 = Project(s"$libName-play-30", file(s"$libName-play-30"))
  .enablePlugins()
  .settings(
    libraryDependencies ++= compile30 ++ test30,
    crossScalaVersions := Seq(scala_3)
  )
