package ml.combust.mleap

import sbt.*
import Keys.*

import scala.util.matching.Regex

object Dependencies {

  import DependencyHelpers._

  val sparkVersion = "4.0.0"
  val scalaTestVersion = "3.2.19"
  val scalaPbJson4sVersion = "0.11.1"
  val junitVersion = "5.9.2"
  val akkaVersion = "2.6.21" // Stay below akka v2.7.0 since they swapped to a BSL license
  val akkaHttpVersion = "10.2.10" // Stay below akka-http v10.3.0 since they swapped to a BSL license
  val springBootVersion = "2.7.0"
  lazy val logbackVersion = "1.2.3"
  lazy val loggingVersion = "3.9.5"
  lazy val slf4jVersion = "2.0.17"
  lazy val awsSdkVersion = "1.12.470"
  lazy val scalaCollectionCompat = "2.12.0"
  val tensorflowJavaVersion = "1.1.0" // Match Tensorflow 2.10.1 https://github.com/tensorflow/java/#tensorflow-version-support
  val xgboostVersion = "2.0.3"
  val breezeVersion = "2.1.0"
  val hadoopVersion = "3.3.4" // matches spark version
  val platforms = "windows-x86_64,linux-x86_64,macosx-x86_64"

  object Compile {
    val `scala-collection-compat` = "org.scala-lang.modules" %% "scala-collection-compat" % scalaCollectionCompat
    val sparkMllibLocal = "org.apache.spark" %% "spark-mllib-local" % sparkVersion excludeAll (ExclusionRule(organization = "org.scalatest"))
    val spark: Seq[ModuleID] = Seq("org.apache.spark" %% "spark-core" % sparkVersion,
      "org.apache.spark" %% "spark-sql" % sparkVersion,
      "org.apache.spark" %% "spark-mllib" % sparkVersion,
      "org.apache.spark" %% "spark-mllib-local" % sparkVersion,
      "org.apache.spark" %% "spark-catalyst" % sparkVersion,
      "org.apache.spark" %% "spark-avro" % sparkVersion,

    )
    val avroDep = "org.apache.avro" % "avro" % "1.12.0"
    val sprayJson = "io.spray" %% "spray-json" % "1.3.6"
    val config = "com.typesafe" % "config" % "1.4.3"
    val scalaReflect = ScalaVersionDependentModuleID.versioned("org.scala-lang" % "scala-reflect" % _)
    val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion
    val jTransform = "com.github.rwl" % "jtransforms" % "2.4.0" exclude("junit", "junit")
    var tensorflowCoreApi = "org.tensorflow" % "tensorflow-core-api" % tensorflowJavaVersion
    val tensorflowProto = "org.tensorflow" % "proto" % "1.15.0"
    val tensorflowNative = "org.tensorflow" % "tensorflow-core-native" % tensorflowJavaVersion
    val tensorflowPlatform = "org.tensorflow" % "tensorflow-core-platform" % tensorflowJavaVersion
    val tensorflowFramework = "org.tensorflow" % "tensorflow-framework" % tensorflowJavaVersion

    val tensorflowDeps = Seq(tensorflowCoreApi, tensorflowProto, tensorflowNative, tensorflowPlatform, tensorflowFramework)
    val akkaTestKit = "com.typesafe.akka" %% "akka-testkit" % akkaVersion
    val akkaStreamTestKit = "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion

    val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaVersion
    val akkaHttp = "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
    val akkaHttpSprayJson = "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion
    // Scalameter 0.19 is the last version published for 2.12
    val scalameter: Seq[ModuleID] = Seq("scalameter", "scalameter-core").map(
      "com.storm-enroute" %% _ % "0.19" excludeAll(
        ExclusionRule("com.storm-enroute"),
        ExclusionRule("org.scala-lang.modules"),
      ))
    val scopt = "com.github.scopt" %% "scopt" % "4.1.0"

    val springBoot = "org.springframework.boot" % "spring-boot-starter-web" % springBootVersion
    val springBootActuator = "org.springframework.boot" % "spring-boot-starter-actuator" % springBootVersion

    val commonsLang = "org.apache.commons" % "commons-lang3" % "3.12.0"
    val scalaPb = Seq(
      "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
      "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion,
      "com.thesamet.scalapb" %% "scalapb-json4s" % scalaPbJson4sVersion
    )

    val awsS3 = "com.amazonaws" % "aws-java-sdk-s3" % awsSdkVersion

    val `logback-core-dep` = "ch.qos.logback" % "logback-core"
    val `logback-classic-dep` = "ch.qos.logback" % "logback-classic"
    lazy val logging = Seq(
      `logback-core-dep` % logbackVersion,
      `logback-classic-dep` % logbackVersion,
      "com.typesafe.scala-logging" %% "scala-logging" % loggingVersion
    )

    val breeze = "org.scalanlp" %% "breeze" % breezeVersion

    val xgboostDep = "ml.dmlc" %% "xgboost4j" % xgboostVersion exclude("org.scala-lang.modules", "scala-collection-compat_2.12")
    val xgboostSparkDep = "ml.dmlc" %% "xgboost4j-spark" % xgboostVersion exclude("org.scala-lang.modules", "scala-collection-compat_2.12") exclude("ml.dmlc", "xgboost4j_2.12")
    val xgboostPredictorDep = "ai.h2o" % "xgboost-predictor" % "0.3.20"

    val hadoop = "org.apache.hadoop" % "hadoop-client" % hadoopVersion

    val slf4jDep = "org.slf4j" % "slf4j-log4j12" % slf4jVersion
    val scalapbCompilerPlugin = "com.thesamet.scalapb" %% "compilerplugin" % "0.11.13"
  }

  object Test {
    val scalaTest = Compile.scalaTest % "test"
    val akkaHttpTestkit = "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % "test"
    val akkaTestKit = "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test"
    val springBootTest = "org.springframework.boot" % "spring-boot-starter-test" % springBootVersion % "test"
    val akkaStreamTestKit = "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % "test"
    val junit = "org.junit.jupiter" % "junit-jupiter" % junitVersion % "test"
    val spark = Compile.spark.map(_ % "test")
    val sparkTest = Compile.spark.map(_ % "test" classifier "tests")
  }

  object Provided {
    val spark = Compile.spark.map(_.excludeAll(ExclusionRule(organization = "org.scalatest"))).map(_ % "provided")
    val sparkTestLib = spark.map(_ classifier "tests")
    val hadoop = Compile.hadoop % "provided"
  }

  import Compile._

  val l = libraryDependencies

  val tensor = l ++= Seq(sprayJson, Test.scalaTest)

  val bundleMl = l ++= Seq(config, `scala-collection-compat`, sprayJson, Test.scalaTest)

  val bundleHdfs = l ++= Seq(Provided.hadoop, Test.scalaTest)

  val base = l ++= Seq(`scala-collection-compat`)

  val core = l ++= Seq(sparkMllibLocal, jTransform, breeze, Test.scalaTest) ++ Test.sparkTest

  def runtime(scalaVersion: SettingKey[String]) = l ++= (Seq(Test.scalaTest, Test.junit) ++ scalaReflect.modules(scalaVersion.value))

  val sparkBase = l ++= Provided.spark ++ Seq(scalaTest) ++ Provided.sparkTestLib

  val sparkTestkit = l ++= Provided.spark ++ Provided.sparkTestLib ++ Seq(scalaTest)

  val spark = l ++= Provided.spark ++ Test.sparkTest

  val sparkExtension = l ++= Provided.spark ++ Seq(Compile.slf4jDep) ++ Seq(Test.scalaTest) ++ Test.sparkTest

  val avro = l ++= Seq(avroDep, Test.scalaTest)

  val tensorflow = l ++= tensorflowDeps ++ Seq(Test.scalaTest)

  val xgboostRuntime = l ++= Seq(xgboostDep) ++ Seq(xgboostPredictorDep) ++ Test.spark ++ Test.sparkTest ++ Seq(Test.scalaTest)

  val xgboostSpark = l ++= Seq(xgboostDep) ++ Seq(xgboostSparkDep) ++ Provided.spark ++ Test.spark ++ Test.sparkTest

  val serving = l ++= Seq(akkaHttp, akkaHttpSprayJson, config, Test.scalaTest, Test.akkaHttpTestkit)

  val executor = l ++= Seq(akkaStream, config, Test.scalaTest, Test.akkaTestKit) ++ logging

  val executorTestKit = l ++= Seq(scalaTest, akkaTestKit, akkaStreamTestKit)

  val grpcServer = l ++= Seq(scopt) ++ Seq(Test.scalaTest, Test.akkaStreamTestKit)

  val repositoryS3 = l ++= Seq(awsS3)

  val grpc = l ++= Seq(
    "io.grpc" % "grpc-netty" % scalapb.compiler.Version.grpcJavaVersion) ++ scalaPb

  val springBootServing = l ++= Seq(springBoot, springBootActuator, commonsLang,
    `logback-classic-dep` % "1.2.3", // remove once we can support JDK 17 and spring boot 3.1.0
    Test.scalaTest, Test.springBootTest) ++ scalaPb

  val benchmark = l ++= Seq(scopt) ++ scalameter ++ Compile.spark

  val databricksRuntimeTestkit = l ++= Provided.spark

  object DependencyHelpers {
    case class ScalaVersionDependentModuleID(modules: String => Seq[ModuleID]) {
      def %(config: String): ScalaVersionDependentModuleID =
        ScalaVersionDependentModuleID(version => modules(version).map(_ % config))
    }

    object ScalaVersionDependentModuleID {
      implicit def liftConstantModule(mod: ModuleID): ScalaVersionDependentModuleID = versioned(_ => mod)

      def versioned(f: String => ModuleID): ScalaVersionDependentModuleID = ScalaVersionDependentModuleID(v => Seq(f(v)))

      def fromPF(f: PartialFunction[String, ModuleID]): ScalaVersionDependentModuleID =
        ScalaVersionDependentModuleID(version => if (f.isDefinedAt(version)) Seq(f(version)) else Nil)
    }

    /**
     * Use this as a dependency setting if the dependencies contain both static and Scala-version
     * dependent entries.
     */
    def versionDependentDeps(modules: ScalaVersionDependentModuleID*): Def.Setting[Seq[librarymanagement.ModuleID]] =
      libraryDependencies ++= scalaVersion(version => modules.flatMap(m => m.modules(version))).value

    val ScalaVersion = """\d\.\d+\.\d+(?:-(?:M|RC)\d+)?""".r
    val nominalScalaVersion: String => String = {
      // matches:
      // 2.12.0-M1
      // 2.12.0-RC1
      // 2.12.0
      case version@ScalaVersion() => version
      // transforms 2.12.0-custom-version to 2.12.0
      case version => version.takeWhile(_ != '-')
    }
  }
}
