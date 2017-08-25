import sbt._

object Dependencies {

  object Compile {
    val play = "com.typesafe.play" %% "play" % "2.5.12" % "provided"
    val playJson = "com.typesafe.play" %% "play-json" % "2.5.12" % "provided"
  }

  sealed abstract class Test(scope: String) {
    val scalatestplusPlay = "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % scope
    val scalaTest = "org.scalatest" %% "scalatest" % "3.0.0" % scope
    val pegdown = "org.pegdown" % "pegdown" % "1.5.0" % scope
  }

  object Test extends Test("test")

  object IntegrationTest extends Test("it")

}
