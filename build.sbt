val pekko = ActorLibCross("-pekko", "-pekko")
val akka = ActorLibCross("-akka", "-akka")

inThisBuild(List(
  organization := "com.okumin",
  homepage := Some(url("https://github.com/failurewall/failurewall")),
  licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
  developers := List(
    Developer(
      id = "okumin",
      name = "okumin",
      email = "git@okumin.com",
      url = url("https://okumin.com/")
    )
  )
))

publish / skip := true

name := "failurewall-root"

lazy val failurewallCore = (projectMatrix in file("failurewall-core"))
  .settings(basicSettings: _*)
  .settings(publishSettings: _*)
  .settings(
    name := "failurewall-core",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest-wordspec" % "3.2.17" % "test",
      "org.scalatestplus" %% "scalacheck-1-16" % "3.2.14.0" % "test"
    )
  )
  .jvmPlatform(
    scalaVersions = scalaVersions,
  )

lazy val failurewallActor = projectMatrix
  .defaultAxes(VirtualAxis.jvm)
  .in(file("failurewall-actor"))
  .settings(basicSettings: _*)
  .settings(publishSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "org.scalatestplus" %% "mockito-4-11" % "3.2.17.0" % "test",
    )
  )
  .jvmPlatform(
    scalaVersions = scalaVersions,
    axisValues = Seq(akka),
    settings = Def.settings(
      libraryDependencies ++= Seq(
        "com.typesafe.akka" %% "akka-actor" % "2.6.21",
      )
    )
  )
  .jvmPlatform(
    scalaVersions = scalaVersions,
    axisValues = Seq(pekko),
    settings = Def.settings(
      libraryDependencies ++= Seq(
        "org.apache.pekko" %% "pekko-actor" % "1.0.1",
      )
    )
  )
  .dependsOn(
    failurewallCore,
    failurewallCore % "test->test"
  )

lazy val failurewallPatterns = (projectMatrix in file("failurewall-patterns"))
  .settings(basicSettings: _*)
  .settings(
    name := "failurewall-patterns"
  )
  .jvmPlatform(
    scalaVersions = scalaVersions,
  )
  .dependsOn(
    failurewallCore,
    failurewallCore % "test->test",
    failurewallActor
  )

lazy val failurewallSample = (projectMatrix in file("failurewall-sample"))
  .settings(basicSettings: _*)
  .settings(
    name := "failurewall-sample"
  )
  .jvmPlatform(
    scalaVersions = scalaVersions,
  )
  .dependsOn(
    failurewallPatterns
  )

lazy val failurewallBenchmark = (projectMatrix in file("failurewall-benchmark"))
  .settings(basicSettings: _*)
  .settings(
    name := "failurewall-benchmark",
    resolvers ++= Seq(
      "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/releases"
    ),
    libraryDependencies ++= Seq(
      "com.storm-enroute" %% "scalameter" % "0.8.2" % "test"
    ),
    testFrameworks ++= Seq(
      new TestFramework("org.scalameter.ScalaMeterFramework")
    ),
    Test / parallelExecution := false
  )
  .jvmPlatform(
    scalaVersions = Seq(Scala212),
  )
  .dependsOn(
    failurewallActor
  )

def Scala212 = "2.12.18"
def Scala213 = "2.13.12"

def scalaVersions = Seq(Scala212, Scala213, "3.3.1")

lazy val basicSettings = Seq(
  scalacOptions ++= Seq("-deprecation"),
  Compile / doc / sources := {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, _)) =>
        (Compile / doc / sources).value
      case _ =>
        // disable if Scala 3
        Nil
    }
  },
  publish / skip := true
)

lazy val publishSettings = Seq(
  publish / skip := false
)
