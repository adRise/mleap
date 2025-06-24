package ml.combust.mleap

import sbt.*
import Keys.*
import com.jsuereth.sbtpgp.SbtPgp.autoImport.*
import com.jsuereth.sbtpgp.PgpKeys.*
import com.tubitv.sbt_global_settings.TubiCodeArtifactPlugin.autoImport.codeArtifactPublish
import sbtrelease.ReleasePlugin.autoImport.*
import xerial.sbt.Sonatype.autoImport.*

object Common {
  lazy val defaultMleapSettings = defaultSettings ++ mleapSettings
  lazy val defaultBundleSettings = defaultSettings ++ bundleSettings
  lazy val defaultMleapXgboostSparkSettings = defaultMleapSettings
  lazy val defaultMleapServingSettings = defaultMleapSettings ++ noPublishSettings


  lazy val defaultSettings = buildSettings ++ sonatypeSettings

  lazy val buildSettings: Seq[Def.Setting[_]] = Seq(
    scalaVersion := "2.13.16",
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature"),
    ThisBuild / libraryDependencySchemes +=
      "org.scala-lang.modules" %% "scala-collection-compat" % VersionScheme.Always,
    resolvers += Resolver.mavenLocal,
    resolvers += Resolver.jcenterRepo,
    publish := codeArtifactPublish.value,
    javaOptions ++= Seq(
      "--add-opens java.base/java.nio=ALL-UNNAMED"
    ),
  )

  Test / javaOptions ++= Seq(
    "--add-opens java.base/java.nio=ALL-UNNAMED"
  )


  lazy val mleapSettings: Seq[Def.Setting[_]] = Seq(organization := "ml.combust.mleap")
  lazy val bundleSettings: Seq[Def.Setting[_]] = Seq(organization := "ml.combust.bundle")

  lazy val noPublishSettings: Seq[Def.Setting[_]] = Seq(
    publishSigned / publishTo := None,
    publishTo := None
  )

  lazy val sonatypeSettings: Seq[Def.Setting[_]] = Seq(
    sonatypeProfileName := "ml.combust",
    releasePublishArtifactsAction := PgpKeys.publishSigned.value,
    publishMavenStyle := true,
    publishTo := Some({
      if (isSnapshot.value) {
        Opts.resolver.sonatypeOssSnapshots.head
      } else {
        Opts.resolver.sonatypeStaging
      }
    }),
    Test / publishArtifact := false,
    pomIncludeRepository := { _ => false },
    licenses := Seq("Apache 2.0 License" -> url("http://www.apache.org/licenses/LICENSE-2.0.html")),
    homepage := Some(url("https://github.com/combust/mleap")),
    scmInfo := Some(ScmInfo(url("https://github.com/combust/mleap.git"),
      "scm:git:git@github.com:combust/mleap.git")),
    developers := List(Developer("hollinwilkins",
      "Hollin Wilkins",
      "hollinrwilkins@gmail.com",
      url("http://hollinwilkins.com")),
      Developer("seme0021",
        "Mikhail Semeniuk",
        "mikhail@combust.ml",
        url("https://www.linkedin.com/in/semeniuk")),
      Developer("ancasarb",
        "Anca Sarb",
        "sarb.anca@gmail.com",
        url("https://www.linkedin.com/in/anca-sarb")))
  )
}
