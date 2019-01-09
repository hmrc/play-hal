import sbt._

object Dependencies {

  private val play25Version = "2.5.19"
  private val play26Version = "2.6.20"

  val compile: Seq[ModuleID] =
    PlayCrossCompilation.dependencies(
      shared = Seq(),
      play25 = Seq(
        "com.typesafe.play" %% "play-ws" % play25Version % "provided",
        "com.typesafe.play" %% "play-json" % play25Version % "provided"
      ),
      play26 = Seq(
        "com.typesafe.play" %% "play-ws" % play26Version % "provided",
        // there doesn't seem to be a later version of play-json than 2.6.13
        "com.typesafe.play" %% "play-json" % "2.6.13" % "provided"
      )
    )

  val test: Seq[ModuleID] =
    PlayCrossCompilation.dependencies(
      shared = Seq(
        "org.scalatest"  %% "scalatest"     % "3.0.5"  % Test,
        "org.pegdown" % "pegdown" % "1.6.0" % Test
      ),
      play25 = Seq(
        "com.typesafe.play"      %% "play-test"          % play25Version % Test,
        "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.1"       % Test
      ),
      play26 = Seq(
        "com.typesafe.play"      %% "play-test"          % play26Version % Test,
        "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2"       % Test
      )
    )

  def apply(): Seq[ModuleID] = compile ++ test

  sealed abstract class Test(scope: String) {
    val scalatestplusPlay = "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % scope
    val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5" % scope
    val pegdown = "org.pegdown" % "pegdown" % "1.6.0" % scope
  }

}
