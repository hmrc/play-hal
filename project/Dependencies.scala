import sbt.*

object Dependencies {

  val compile28: Seq[ModuleID] =
    Seq(
      "com.typesafe.play" %% "play-ws"    % "2.8.18" % "provided",
      "com.typesafe.play" %% "play-json"  % "2.8.2"  % "provided"
    )

  val compile29: Seq[ModuleID] =
    Seq(
      "com.typesafe.play" %% "play-ws"    % "2.9.1" % "provided",
      "com.typesafe.play" %% "play-json"  % "2.9.4" % "provided"
    )

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

  val test28: Seq[ModuleID] =
    Seq(
      "com.typesafe.play"       %% "play-test"          % "2.8.21",
      "org.scalatestplus.play"  %% "scalatestplus-play" % "5.1.0"
    ).++(testCommon).map(_ % "test")

  val test29: Seq[ModuleID] =
    Seq(
      "com.typesafe.play"       %% "play-test"          % "2.9.1",
      "org.scalatestplus.play"  %% "scalatestplus-play" % "6.0.0"
    ).++(testCommon).map(_ % "test")

  val test30: Seq[ModuleID] =
    Seq(
      "org.playframework"       %% "play-test"          % "3.0.1",
      "org.scalatestplus.play"  %% "scalatestplus-play" % "7.0.1"
    ).++(testCommon).map(_ % "test")
}
