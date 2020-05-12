import sbt.Keys.publishTo
import sbt._
import sbtassembly.AssemblyPlugin.autoImport.assemblyMergeStrategy

name := "Microservice"

ThisBuild / scalaVersion := "2.12.10"
ThisBuild / organization := "co.s4n.practice"
ThisBuild / version := "1.0.9"

ThisBuild / useCoursier := true
ThisBuild / turbo := true
ThisBuild / useSuperShell := false
Global / onChangedBuildSource := ReloadOnSourceChanges

Revolver.settings.settings

lazy val commonSettings = Seq(
  libraryDependencies ++= {
    val akkaHttpVersion = "10.1.10"
    val akkaVersion = "2.5.25"
    val akkaCirceVersion = "1.22.0"
    val circeVersion = "0.9.3"
    val sttpVersion = "1.7.2"
    val akkHttpCorsVersion = "0.3.1"
    val silencerVersion = "1.4.4"
    val jacksonVersion = "2.10.0"

    Seq(
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      "ch.megard" %% "akka-http-cors" % akkHttpCorsVersion,
      "com.typesafe" %% "ssl-config-core" % "0.4.0",
      ("de.heikoseeberger" %% "akka-http-circe" % akkaCirceVersion)
        .exclude("com.typesafe.akka", "akka-actor_2.12"),
      ("de.heikoseeberger" %% "akka-log4j" % "1.6.1")
        .exclude("com.typesafe.akka", "akka-actor_2.12"),
      "org.apache.logging.log4j" % "log4j-1.2-api" % "2.8.2",
      "org.apache.logging.log4j" % "log4j-api" % "2.11.1",
      "org.apache.logging.log4j" % "log4j-core" % "2.11.1",
      "org.apache.logging.log4j" % "log4j-jcl" % "2.11.1",
      "org.apache.logging.log4j" % "log4j-jul" % "2.11.1",
      "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.11.1",
      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % "test",
      "com.typesafe.akka" %% "akka-testkit" % "2.5.25" % Test,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,
      compilerPlugin(
        "com.github.ghik" % "silencer-plugin" % silencerVersion cross CrossVersion.full
      ),
      "com.github.ghik" % "silencer-lib" % silencerVersion % Provided cross CrossVersion.full,
      "com.softwaremill.sttp" %% "core" % sttpVersion,
      ("com.softwaremill.sttp" %% "async-http-client-backend-monix" % sttpVersion)
        .exclude("io.monix", "monix_2.12")
        .exclude("io.netty", "netty-handler 4.1.13.Final")
        .exclude("org.reactivestreams", "reactive-streams"),
      "com.github.pureconfig" %% "pureconfig" % "0.9.1",
      "io.monix" %% "monix" % "3.1.0",
      "com.github.swagger-akka-http" %% "swagger-akka-http" % "1.0.0",
      "com.github.swagger-akka-http" %% "swagger-scala-module" % "2.0.5",
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion,
      "com.sun.xml.messaging.saaj" % "saaj-impl" % "1.4.0",
      "com.softwaremill.quicklens" %% "quicklens" % "1.4.11",
      "org.scalatest" %% "scalatest" % "3.0.5" % "test",
      "org.scala-lang" % "scala-compiler" % "2.12.10"
    )
  },
  excludeDependencies ++= Seq(
    "log4j" % "log4j",
    "org.apache.logging.log4j" % "log4j-to-slf4j",
    "org.slf4j" % "log4j-over-slf4j",
    "ch.qos.logback" % "logback-core",
    "ch.qos.logback" % "logback-classic",
    "org.slf4j" % "jcl-over-slf4j",
    "org.slf4j" % "jul-to-slf4j"
  ),
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding",
    "UTF-8",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-language:experimental.macros",
    "-unchecked",
    "-Xfatal-warnings",
    "-Xlint:private-shadow",
    "-Xlint:unsound-match",
    "-Xlint:constant",
    "-Yno-adapted-args",
    "-Ypartial-unification",
    "-Ywarn-dead-code",
    "-Ywarn-inaccessible",
    "-Ywarn-nullary-unit",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Ywarn-unused:imports",
    "-Ywarn-unused:locals",
    "-Ywarn-unused:params",
    "-Ywarn-unused:privates",
    "-Xfuture"
  ),
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", "io.netty.versions.dtos") =>
      MergeStrategy.discard
    case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
    case PathList("META-INF", x) if x.endsWith(".DSA") || x.endsWith(".SF") =>
      MergeStrategy.discard
    case "reference.conf"   => MergeStrategy.concat
    case "application.conf" => MergeStrategy.concat
    case x                  => MergeStrategy.first
  },
  test in assembly := {},
  wartremoverErrors ++= Warts.unsafe
)

lazy val core = {
  project
    .settings(
      commonSettings,
      coverageMinimum := 69,
      coverageFailOnMinimum := true,
      coverageHighlighting := true,
      name := "microservice-core"
    )
}

lazy val ports = {
  project
    .enablePlugins(BuildInfoPlugin,
                   DockerPlugin,
                   JavaAppPackaging,
                   AshScriptPlugin)
    .settings(
      commonSettings,
      name := "microservice-ports",
      organization := "co.s4n.practice",
      mainClass in (Compile, run) := Option("co.s4n.practice.Main"),
      mainClass in assembly := Some("co.s4n.practice.Main"),
      buildInfoKeys := Seq[BuildInfoKey](version),
      buildInfoPackage := "co.s4n.practice",
      dockerUpdateLatest := true,
      dockerBaseImage := "openjdk:8u212-jre-alpine"
    )
    .dependsOn(core)
}

scalafmtOnCompile in ThisBuild := true

addCommandAlias(
  "validate",
  ";compile;scalafmt::test;coverage;test;coverageReport"
)
