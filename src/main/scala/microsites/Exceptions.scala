package microsites

object Exceptions {

  sealed abstract class MicrositesException(val message: String, val maybeCause: Option[Throwable])
      extends RuntimeException(message)
      with Product
      with Serializable {

    maybeCause foreach initCause

    override def toString: String = message
  }

  case class IOException(msg: String, cause: Option[Throwable] = None)
      extends MicrositesException(msg, cause)

  case class ValidationException(msg: String, cause: Option[Throwable] = None)
      extends MicrositesException(msg, cause)

  case class GitHub4sException(msg: String, cause: Option[Throwable] = None)
      extends MicrositesException(msg, cause)

  case class YamlException(msg: String, cause: Option[Throwable] = None)
      extends MicrositesException(msg, cause)

  case class UnexpectedException(msg: String, cause: Option[Throwable] = None)
      extends MicrositesException(msg, cause)
}
