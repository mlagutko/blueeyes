package blueeyes.core.service

import akka.dispatch.Future
import akka.dispatch.Promise
import akka.dispatch.ExecutionContext

import blueeyes.core.data.ByteChunk
import blueeyes.core.http._
import blueeyes.core.service._
import engines.HttpClientXLightWeb
import HttpStatusCodes._

import scalaz.{Failure, Success}
import scalaz.Validation._
import blueeyes.Environment

trait ConfigurableHttpClient {
  implicit def executionContext: ExecutionContext

  lazy implicit val httpClient: HttpClientByteChunk = {
    val isMock = sys.props.getOrElse(Environment.MockSwitch, "false").toBoolean
    if (isMock) mockClient(mockServer) else realClient
  }

  protected def realClient: HttpClientByteChunk = new HttpClientXLightWeb()

  private def mockClient(h: AsyncHttpService[ByteChunk]): HttpClientByteChunk = new HttpClientByteChunk {
    val executor = executionContext
    def isDefinedAt(r: HttpRequest[ByteChunk]) = true
    def apply(r: HttpRequest[ByteChunk]): Future[HttpResponse[ByteChunk]] = h.service(r) match {
      case Success(rawFuture) => rawFuture recover { case throwable => convertErrorToResponse(throwable) }
      case Failure(DispatchError(throwable)) => Promise.successful(convertErrorToResponse(throwable))
      case failure => 
        val message = "MockClient received NotFound for request " + r
        Promise.successful(HttpResponse[ByteChunk](HttpStatus(NotFound, message)))
    }
  }

  private def convertErrorToResponse(th: Throwable): HttpResponse[ByteChunk] = th match {
    case e: HttpException => HttpResponse[ByteChunk](HttpStatus(e.failure, e.reason))
    case e => HttpResponse[ByteChunk](HttpStatus(InternalServerError, Option(e.getMessage).getOrElse("")))
  }

  protected def mockServer: AsyncHttpService[ByteChunk] = new CustomHttpService[ByteChunk, Future[HttpResponse[ByteChunk]]] {
    def service = (request: HttpRequest[ByteChunk]) => {
      success(Future(HttpResponse[ByteChunk](HttpStatus(NotFound, "Mock server handles no requests."))))
    }

    val metadata = None
  }
}
