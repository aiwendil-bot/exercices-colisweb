
scalaVersion := "2.13.5"

ThisBuild / scalafmtCheck := true
ThisBuild / scalafmtSbtCheck := true

ThisBuild / pushRemoteCacheTo := Some(
  MavenCache("local-cache", baseDirectory.value / sys.env.getOrElse("CACHE_PATH", "sbt-cache"))
)
ThisBuild / isSnapshot := true


libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.9" % Test
)

Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / scalafmtOnCompile := true
ThisBuild / scalafmtCheck := true
ThisBuild / scalafmtSbtCheck := true

