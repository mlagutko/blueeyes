name := "blueeyes-json"

publishArtifact in Test := true

libraryDependencies ++= Seq(
  "org.scalaz"                  % "scalaz-core_2.9.2"        % "7.0.0-M8",
  "joda-time"                   %  "joda-time"          % "1.6.2"          % "optional",
  "org.specs2"         %% "specs2"        % "1.12.2"         % "test",
  "org.scalacheck"     %% "scalacheck"    % "1.10.0"         % "test"
)
