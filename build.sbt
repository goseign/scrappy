name := """airbnb-scrapper"""

version in ThisBuild := "1.0-SNAPSHOT"

scalaVersion in ThisBuild := "2.11.8"

initialize := {
  val _ = initialize.value
  if (sys.props("java.specification.version") != "1.8")
    sys.error("Java 8 is required for this project.")
}

parallelExecution in (ThisBuild, Test) := true

lazy val root = project.in(file("."))
  .aggregate(
    flyway,
    api,
    slick,
    play
  )

lazy val flyway = (project in file("modules/flyway"))
  .enablePlugins(FlywayPlugin)

lazy val api = (project in file("modules/api"))
  .enablePlugins(Common)

lazy val slick = (project in file("modules/slick"))
  .enablePlugins(Common)
  .aggregate(api)
  .dependsOn(api)

lazy val play = (project in file("modules/play"))
  .enablePlugins(PlayScala)
  .aggregate(api, slick)
  .dependsOn(api, slick)

// Add any command aliases that may be useful as shortcuts
addCommandAlias("cc", ";clean;compile")