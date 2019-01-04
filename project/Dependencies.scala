import sbt._

object Dependencies {

  object Compile {
    private val playVersion = "2.5.19"
    
    val play     = "com.typesafe.play" %% "play" % playVersion % "provided"
    val playJson = "com.typesafe.play" %% "play-json" % playVersion % "provided"
  }

  sealed abstract class Test(scope: String) {
    val scalatestplusPlay = "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % scope
    val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5" % scope
    val pegdown = "org.pegdown" % "pegdown" % "1.6.0" % scope
  }

  object Test extends Test("test")

  object IntegrationTest extends Test("it")

}
