import sbt.{Def, *}
import sbt.Keys.*

object CopySources {
  /** Copies source files from one module to another, and applies transformations */
  def copySources(
    module: Project,
    transformSource: String => String,
    transformResource: String => String
  ): Seq[Def.Setting[? >: Seq[Task[Seq[File]]] & Task[Seq[(File, String)]] <: Equals]] = {
    def transformWith(
      fromSetting: SettingKey[File],
      toSetting: SettingKey[File],
      transform: String => String
    ): Def.Initialize[Task[Seq[File]]] =
      Def.task {
        val from  = fromSetting.value
        val to    = toSetting.value
        val files = (from ** "*").get.filterNot(_.isDirectory)
        println(s"Copying and transforming the following files for ${moduleName.value} scalaVersion ${scalaVersion.value}:" +
          s" files:\n${files.map("  " + _).mkString("\n")}")
        files.map { file =>
          val targetFile = new java.io.File(file.getParent.replace(from.getPath, to.getPath)) / file.getName
          IO.write(targetFile, transform(IO.read(file)))
          targetFile
        }
      }

    def include(location: File): Seq[(File, String)] =
      (location ** "*")
        .get
        .filterNot(_.isDirectory)
        .map(
          file =>
            file -> file.getPath.stripPrefix(location.getPath + "/")
        )

    Seq(
      Compile / sourceGenerators   +=
        transformWith(module / Compile / scalaSource      , Compile / sourceManaged  , transformSource  ).taskValue,
      Compile / resourceGenerators +=
        transformWith(module / Compile / resourceDirectory, Compile / resourceManaged, transformResource).taskValue,
      Test    / sourceGenerators   +=
        transformWith(module / Test    / scalaSource      , Test    / sourceManaged  , transformSource  ).taskValue,
      Test    / resourceGenerators +=
        transformWith(module / Test    / resourceDirectory, Test    / resourceManaged, transformResource).taskValue,
      // generated sources are not included in source.jar by default
      Compile / packageSrc / mappings ++=
        include((Compile / sourceManaged).value) ++ include((Compile / resourceManaged).value)
    )
  }
}
