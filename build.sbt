ThisBuild / scalaVersion := "2.13.16"

val http4sVersion = "0.23.12"
lazy val root = (project in file("."))
  .settings(
    name := "todo-app",
    version := "0.1.0",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "org.http4s" %% "http4s-circe"        % http4sVersion,
      "org.http4s" %% "http4s-dsl"          % http4sVersion,
      "io.circe"   %% "circe-generic"       % "0.14.15",
      "io.circe"   %% "circe-generic-extras"       % "0.14.4",
      "ch.qos.logback" % "logback-classic"  % "1.5.19",
      
      // Doobie + SQLite
      "org.tpolecat" %% "doobie-core"       % "1.0.0-RC4",
      "org.tpolecat" %% "doobie-hikari"     % "1.0.0-RC4",
      "org.xerial"   %  "sqlite-jdbc"       % "3.43.2.0", // Native SQLite JDBC

      // Logging
      "ch.qos.logback" % "logback-classic" % "1.4.11",

      // Test dependencies
      "org.typelevel" %% "munit-cats-effect-3" % "1.0.7" % Test,
      "org.http4s" %% "http4s-client" % http4sVersion % Test
    )
  )
