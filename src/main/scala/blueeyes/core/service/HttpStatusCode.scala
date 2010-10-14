package blueeyes.core.service

sealed trait HttpStatusCode {
  def name = productPrefix
      
  def productPrefix: String
  
  def value: Int
  
  def defaultMessage: String
  
  def apply() = value
}

sealed trait HttpSuccess extends HttpStatusCode
sealed trait HttpFailure extends HttpStatusCode

sealed abstract class Informational(val value: Int, val defaultMessage: String) extends HttpSuccess
sealed abstract class Success(val value: Int, val defaultMessage: String) extends HttpSuccess
sealed abstract class Redirection(val value: Int, val defaultMessage: String) extends HttpSuccess

sealed abstract class ClientError(val value: Int, val defaultMessage: String) extends HttpFailure
sealed abstract class ServerError(val value: Int, val defaultMessage: String) extends HttpFailure

object HttpStatusCodes {  
  case object Continue            extends Informational(100, "The server has received the request headers, and the client should proceed to send the request body.")
  case object SwitchingProtocols  extends Informational(101, "The server is switching protocols, because the client requested the switch.")
  case object Processing          extends Informational(102, "The server is processing the request, but no response is available yet.")
  
  case object OK              extends Success(200, "The request was successful.")
  case object Created         extends Success(201, "The request has been fulfilled and resulted in a new resource being created.")
  case object Accepted        extends Success(202, "The request has been accepted for processing, but the processing has not been completed.")
  case object Non             extends Success(203, "The server successfully processed the request, but is returning information that may be from another source.")
  case object NoContent       extends Success(204, "The server successfully processed the request, but is not returning any content.")
  case object ResetContent    extends Success(205, "The server successfully processed the request, but is not returning any content.")
  case object PartialContent  extends Success(206, "The server is delivering only part of the resource due to a range header sent by the client.")
  case object Multi           extends Success(207, "The message body that follows is an XML message and can contain a number of separate response codes, depending on how many sub-requests were made.")
  
  case object MultipleChoices   extends Redirection(300, "Indicates multiple options for the resource that the client may follow.")
  case object MovedPermanently  extends Redirection(301, "This and all future requests should be directed to the given URI.")
  case object Found             extends Redirection(302, "The resource was found, but at a different URI.")
  case object SeeOther          extends Redirection(303, "The response to the request can be found under another URI using a GET method.")
  case object NotModified       extends Redirection(304, "Indicates the resource has not been modified since last requested.")
  case object UseProxy          extends Redirection(305, "Many HTTP clients (such as Mozilla[7] and Internet Explorer) do not correctly handle responses with this status code, primarily for security reasons.")
  case object TemporaryRedirect extends Redirection(307, "In this occasion, the request should be repeated with another URI, but future requests can still use the original URI.")
  
  case object BadRequest                          extends ClientError(400, "The request contains bad syntax or cannot be fulfilled.")
  case object Unauthorized                        extends ClientError(401, "Authentication is possible but has failed or not yet been provided.")
  case object PaymentRequired                     extends ClientError(402, "Reserved for future use.")
  case object Forbidden                           extends ClientError(403, "The request was a legal request, but the server is refusing to respond to it.")
  case object NotFound                            extends ClientError(404, "The requested resource could not be found but may be available again in the future.")
  case object MethodNotAllowed                    extends ClientError(405, "A request was made of a resource using a request method not supported by that resource;")
  case object NotAcceptable                       extends ClientError(406, "The requested resource is only capable of generating content not acceptable according to the Accept headers sent in the request.")
  case object ProxyAuthenticationRequired         extends ClientError(407, "Proxy authentication is required to access the requested resource.")
  case object RequestTimeout                      extends ClientError(408, "The server timed out waiting for the request.")
  case object Conflict                            extends ClientError(409, "Indicates that the request could not be processed because of conflict in the request, such as an edit conflict.")
  case object Gone                                extends ClientError(410, "Indicates that the resource requested is no longer available and will not be available again.")
  case object LengthRequired                      extends ClientError(411, "The request did not specify the length of its content, which is required by the requested resource.")
  case object PreconditionFailed                  extends ClientError(412, "The server does not meet one of the preconditions that the requester put on the request.")
  case object RequestEntityTooLarge               extends ClientError(413, "The request is larger than the server is willing or able to process.")
  case object Request                             extends ClientError(414, "The URI provided was too long for the server to process.")
  case object UnsupportedMediaType                extends ClientError(415, "The request entity has a media type which the server or resource does not support.")
  case object RequestedRangeNotSatisfiable        extends ClientError(416, "The client has asked for a portion of the file, but the server cannot supply that portion.")
  case object ExpectationFailed                   extends ClientError(417, "The server cannot meet the requirements of the Expect request-header field.")
  case object TooManyConnections                  extends ClientError(421, "There are too many connections from your internet address.")
  case object UnprocessableEntity                 extends ClientError(422, "The request was well-formed but was unable to be followed due to semantic errors.")
  case object Locked                              extends ClientError(423, "The resource that is being accessed is locked.")
  case object FailedDependency                    extends ClientError(424, "The request failed due to failure of a previous request.")
  case object UnorderedCollection                 extends ClientError(425, "The collection is unordered.")
  case object UpgradeRequired                     extends ClientError(426, "The client should switch to a different protocol such as TLS/1.0.")
  case object RetryWith                           extends ClientError(449, "The request should be retried after doing the appropriate action.")
  
  case object InternalServerError       extends ServerError(500, "There was an internal server error.")
  case object NotImplemented            extends ServerError(501, "The server either does not recognize the request method, or it lacks the ability to fulfill the request.")
  case object BadGateway                extends ServerError(502, "The server was acting as a gateway or proxy and received an invalid response from the upstream server.")
  case object ServiceUnavailable        extends ServerError(503, "The server is currently unavailable (because it is overloaded or down for maintenance).")
  case object GatewayTimeout            extends ServerError(504, "The server was acting as a gateway or proxy and did not receive a timely request from the upstream server.")
  case object HTTPVersionNotSupported   extends ServerError(505, "The server does not support the HTTP protocol version used in the request.")
  case object VariantAlsoNegotiates     extends ServerError(506, "Transparent content negotiation for the request, results in a circular reference.")
  case object InsufficientStorage       extends ServerError(507, "Insufficient storage to complete the request.")
  case object BandwidthLimitExceeded    extends ServerError(509, "Bandwidth limit has been exceeded.")
  case object NotExtended               extends ServerError(510, "Further extensions to the request are required for the server to fulfill it.")
  case object UserAccessDenied          extends ServerError(530, "User access is denied to the specified resource.")
}