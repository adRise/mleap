package ml.combust.mleap

import sbt.Keys._
import sbt._

object MleapProject {


  var rootSettings = Release.settings ++
    Common.buildSettings ++
    Common.sonatypeSettings ++
    Seq(publishArtifact := false)

  lazy val root = Project(
    id = "mleap",
    base = file(".")
  ).aggregate(baseProject,
      tensor,
      tensorflow,
      bundleMl,
      bundleHdfs,
      core,
      runtime,
      sparkBase,
      sparkTestkit,
      spark,
      sparkExtension,
      executor,
      executorTestKit,
      grpc,
      grpcServer,
      repositoryS3)
    .settings(rootSettings)

  lazy val baseProject = Project(
    id = "mleap-base",
    base = file("mleap-base")
  )

  lazy val tensor = Project(
    id = "mleap-tensor",
    base = file("mleap-tensor")
  ).dependsOn(baseProject)

  lazy val bundleMl = Project(
    id = "bundle-ml",
    base = file("bundle-ml")
  ).dependsOn(baseProject, tensor)

  lazy val bundleHdfs = Project(
    id = "bundle-hdfs",
    base = file("bundle-hdfs")
  ).dependsOn(bundleMl)

  lazy val core = Project(
    id = "mleap-core",
    base = file("mleap-core")
  ).dependsOn(baseProject, tensor)

  lazy val runtime = Project(
    id = "mleap-runtime",
    base = file("mleap-runtime")
  ).dependsOn(core, bundleMl)

  lazy val sparkBase = Project(
    id = "mleap-spark-base",
    base = file("mleap-spark-base")
  ).dependsOn(runtime, bundleHdfs)

  lazy val sparkTestkit = Project(
    id = "mleap-spark-testkit",
    base = file("mleap-spark-testkit")
  ).dependsOn(sparkBase)

  lazy val spark = Project(
    id = "mleap-spark",
    base = file("mleap-spark")
  ).dependsOn(sparkBase, sparkTestkit % "test")

  lazy val sparkExtension = Project(
    id = "mleap-spark-extension",
    base = file("mleap-spark-extension")
  ).dependsOn(spark, sparkTestkit % "test")

  lazy val tensorflow = Project(
    id = "mleap-tensorflow",
    base = file("mleap-tensorflow")
  ).dependsOn(runtime)

  lazy val xgboostRuntimeSettings = inConfig(Test)(Defaults.testSettings) ++ Seq(
    // xgboost has trouble with multi-threading so avoid parallel executions.
    Test / parallelExecution := false,
  )

  lazy val executor = Project(
    id = "mleap-executor",
    base = file("mleap-executor")
  ).dependsOn(runtime)

  lazy val executorTestKit = Project(
    id = "mleap-executor-testkit",
    base = file("mleap-executor-testkit")
  ).dependsOn(executor)

  private val executorTestSettings = inConfig(Test)(Defaults.testSettings) ++ Seq(
    // Supports classloading "magic" from the sbt.
    Test / classLoaderLayeringStrategy := ClassLoaderLayeringStrategy.Flat
  )
  lazy val executorTests = Project(
    id = "mleap-executor-tests",
    base = file("mleap-executor-tests")
  ).dependsOn(executor, executorTestKit % "test")
    .settings(executorTestSettings)

  lazy val grpc = Project(
    id = "mleap-grpc",
    base = file("mleap-grpc")
  ).dependsOn(`executor`)

  private val grpcServerSettings = inConfig(Test)(Defaults.testSettings) ++ Seq(
    // Supports classloading "magic" from the sbt.
    Test / classLoaderLayeringStrategy := ClassLoaderLayeringStrategy.Flat
  )
  lazy val grpcServer = Project(
    id = "mleap-grpc-server",
    base = file("mleap-grpc-server")
  ).dependsOn(grpc, executorTestKit % "test").settings(grpcServerSettings)

  lazy val repositoryS3 = Project(
    id = "mleap-repository-s3",
    base = file("mleap-repository-s3")
  ).dependsOn(executor)

  private val springBootSettings = inConfig(Test)(Defaults.testSettings) ++ Seq(
    // spring-boot: avoiding tomcat's java.lang.Error: factory already defined
    // refer to https://github.com/spring-projects/spring-boot/issues/21535
    Test / fork := true,
  )
}
