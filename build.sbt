name := "social-search"
version := "0.1"

lazy val akkaActorVersion = "2.5.11"

lazy val commonSettings = Seq(
  scalaVersion := "2.12.5",
)

lazy val commonDeps = Seq(
  "com.typesafe" % "config" % "1.3.1"
)

lazy val akkaActorDeps = Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaActorVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaActorVersion % Test
)

lazy val stream = project
  .settings(commonSettings)
  .settings(libraryDependencies ++= commonDeps)
  .settings(libraryDependencies ++= akkaActorDeps)
  .settings(libraryDependencies ++= Seq(
    "org.twitter4j" % "twitter4j-core" % "4.0.6"
  ))

lazy val root = (project in file("."))
  .aggregate(stream)
