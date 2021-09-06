import sbt._

object Dependencies {

  private val play25Version = "2.5.19"
  private val play26Version = "2.6.20"
  private val play27Version = "2.7.4"
  private val play28Version = "2.8.8"

  val compile: Seq[ModuleID] =
    PlayCrossCompilation.dependencies(
      shared = Seq(),
      play26 = Seq(
        "com.typesafe.play" %% "play-ws" % play26Version % "provided",
        "com.typesafe.play" %% "play-json" % "2.6.13" % "provided"
      ),
      play27 = Seq(
        "com.typesafe.play" %% "play-ws" % play27Version % "provided",
        "com.typesafe.play" %% "play-json" % "2.7.4" % "provided"
      ),
  play28 = Seq(
    "com.typesafe.play" %% "play-ws" % play28Version % "provided",
    "com.typesafe.play" %% "play-json" % "2.9.2" % "provided"
      )
   )

  val test: Seq[ModuleID] =
    PlayCrossCompilation.dependencies(
      shared = Seq(
        "org.scalatest"  %% "scalatest"     % "3.2.9"  % Test,
        "org.pegdown" % "pegdown" % "1.6.0" % Test,
        "com.vladsch.flexmark" % "flexmark-all" % "0.35.10"
      ),
      play26 = Seq(
        "com.typesafe.play"      %% "play-test"          % play26Version % Test,
        "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2"       % Test
      ),
      play27 = Seq(
        "com.typesafe.play"      %% "play-test"          % play27Version % Test,
        "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2"       % Test
      ),
      play28 = Seq(
        "com.typesafe.play"      %% "play-test"          % play28Version % Test,
        "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0"       % Test
      )
   )

  def apply(): Seq[ModuleID] = compile ++ test

}
