import sbt.*

object Dependencies {

  val compile30: Seq[ModuleID] =
    Seq(
      "org.playframework" %% "play-ws"    % "3.0.1" % "provided",
      "org.playframework" %% "play-json"  % "3.0.2" % "provided"
    )

  private val testCommon: Seq[ModuleID] =
    Seq(
      "org.scalatest"           %% "scalatest"    % "3.2.15",
      "com.vladsch.flexmark"    %  "flexmark-all" % "0.64.6",
      "org.pegdown"             %  "pegdown"      % "1.6.0"
    )

  val test30: Seq[ModuleID] =
    Seq(
      "org.playframework"       %% "play-test"          % "3.0.1",
      "org.scalatestplus.play"  %% "scalatestplus-play" % "7.0.1"
    ).++(testCommon).map(_ % "test")
}
