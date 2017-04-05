scalaVersion in ThisBuild := "2.12.1"

organization := "ppl-stanford"

version := "rub-1.0"

val paradiseVersion = "2.1.0"

publishArtifact := false

scalaSource in Compile := baseDirectory(_/ "src").value

//paradise
resolvers += Resolver.sonatypeRepo("snapshots")
resolvers += Resolver.sonatypeRepo("releases")
addCompilerPlugin("org.scalamacros" % "paradise" % paradiseVersion cross CrossVersion.full)
