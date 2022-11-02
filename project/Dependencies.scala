import sbt._

object Dependencies {

  private val play25Version = "2.5.19" //not used
  private val play26Version = "2.6.20" //not used
  private val play27Version = "2.7.4"
  private val play28Version = "2.8.8"

  val compile: Seq[ModuleID] =
    PlayCrossCompilation.dependencies(
      shared = Seq(),
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
      play27 = Seq(
        "com.typesafe.play"      %% "play-test"          % play27Version % Test,
        "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3"       % Test
      ),
      play28 = Seq(
        "com.typesafe.play"      %% "play-test"          % play28Version % Test,
        "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0"       % Test
      )
   )

  def apply(): Seq[ModuleID] = compile ++ test

}
