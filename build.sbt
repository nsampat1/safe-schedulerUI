name := "safescheduler"

version := "0.1"

scalaVersion := "2.11.12"

autoCompilerPlugins := true

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.4.5",
  "org.apache.spark" %% "spark-sql" % "2.4.5",
  "mysql" % "mysql-connector-java" % "8.0.32",
  "org.scalafx" %% "scalafx" % "8.0.92-R10"
)
scalacOptions ++= Seq(
  "-P:flowframe:lang:purpose"
)

lazy val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux") => "linux"
  case n if n.startsWith("Mac") => "mac"
  case n if n.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
}

// Add JavaFX dependencies
lazy val javaFXModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
libraryDependencies ++= javaFXModules.map( m=>
  "org.openjfx" % s"javafx-$m" % "11" classifier osName
)

//libraryDependencies += "org.openjfx" % "javafx" % "12.0.2" pomOnly()

addCompilerPlugin("com.facebook" % "flowframe_2.11.12" % "0.1-SNAPSHOT")


fork := true
