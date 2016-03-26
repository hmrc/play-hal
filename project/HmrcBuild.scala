import sbt.Keys._
import sbt._
import uk.gov.hmrc.SbtAutoBuildPlugin
import uk.gov.hmrc.versioning.SbtGitVersioning

object HmrcBuild extends Build {

  import SbtAutoBuildPlugin._
  
  val nameApp = "play-hal"

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

  lazy val simpleReactiveMongo = Project(nameApp, file("."))
    .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning)
    .settings(
      autoSourceHeader := false,
      scalaVersion := "2.11.8",
      libraryDependencies ++= appDependencies,
      resolvers += Resolver.typesafeRepo("releases"),
      crossScalaVersions := Seq("2.11.8")
    )
}

object Dependencies {

  object Compile {
    val play = "com.typesafe.play" %% "play" % "2.3.10" % "provided"
    val playJson = "com.typesafe.play" %% "play-json" % "2.3.10" % "provided"
  }

  sealed abstract class Test(scope: String) {
    val scalatestplusPlay = "org.scalatestplus" %% "play" % "1.2.0" % scope
    val scalaTest = "org.scalatest" %% "scalatest" % "2.2.6" % scope
    val pegdown = "org.pegdown" % "pegdown" % "1.5.0" % scope
  }

  object Test extends Test("test")

  object IntegrationTest extends Test("it")

}
