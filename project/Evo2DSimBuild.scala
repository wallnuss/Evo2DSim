import sbt._

object Evo2DSimBuild extends Build {
    lazy val root = project in file(".") aggregate(core, analyzer, macros)

    lazy val core = project in file("core") dependsOn macros
    lazy val analyzer = project in file("analyzer") dependsOn core
    lazy val macros = project in file("macros")
}