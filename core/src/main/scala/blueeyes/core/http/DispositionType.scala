package blueeyes.core.http

import blueeyes.util.ProductPrefixUnmangler

/* For use in the Content-Disposition HTTP Header */

sealed trait DispositionType extends ProductPrefixUnmangler{

  def dispType = unmangledName

  def fileName: Option[String]
  def creationDate: Option[HttpDateTime]
  def size: Option[Int]

  def value = (dispType :: fileName.map("fileame=" + _).toList ++
              creationDate.map("creation-date=" + _.toString).toList ++ size.map("size=" + _.toString).toList).mkString(";")
  override def toString = value
}

object DispositionTypes {

  def parseDispositionTypes(inString: String): DispositionType = {
    val splitStr = inString.toLowerCase.split(";")
    val disBuild = new DispositionTypeBuilder(splitStr(0))
    if (splitStr.length > 1) {
      disBuild.filename = {
        val params = splitStr(1).split("=")
        if (params.length > 1)
          Some(params(1))
        else None
      }
    }
    disBuild.buildType
  }

  case class inline (fileName: Option[String], creationDate: Option[HttpDateTime], size: Option[Int]) extends DispositionType
  object inline {
    def apply(): DispositionType = new inline(None, None, None)
    def apply(fileName: Option[String]) = new inline(fileName, None, None)
    def apply(fileName: Option[String], size: Option[Int]) = new inline(fileName, None, size)
  }

  case class attachment (fileName: Option[String], creationDate: Option[HttpDateTime], size: Option[Int]) extends DispositionType
  object attachment {
    def apply(): DispositionType = new attachment(None, None, None)
    def apply(fileName: Option[String]) = new attachment(fileName, None, None)
    def apply(fileName: Option[String], size: Option[Int]) = new attachment(fileName, None, size)
  }

  case class NullDispositionType(inDispType: String, fileName: Option[String], creationDate: Option[HttpDateTime], size: Option[Int]) extends DispositionType {
    override def dispType = inDispType
    override def value = ""
  }

  case class CustomDispositionType (inDispType: String, fileName: Option[String], creationDate: Option[HttpDateTime], size: Option[Int]) extends DispositionType {
    override def dispType = inDispType
  }

  case class DispositionTypeBuilder(inDispType: String) {

    def dispType = inDispType;

    var defFileName: Option[String] = None
    var defCreationDate: Option[HttpDateTime] = None
    var defSize: Option[Int] = None

    def filename: Option[String] = defFileName
    def filename_=(inName: Option[String]) { defFileName = inName }

    def creationDate: Option[HttpDateTime] = defCreationDate
    def creationDate_=(inDate: Option[HttpDateTime]) { defCreationDate = inDate }

    def size: Option[Int] = defSize
    def size_=(inSize: Option[Int]) {defSize = inSize }

    def buildType(): DispositionType = {
      dispType match {
        case "inline" => inline(filename, creationDate, size)
        case "attachment" => attachment(filename, creationDate, size)
        case default => NullDispositionType(dispType, None, None, None)
      }
    }
  }

}
