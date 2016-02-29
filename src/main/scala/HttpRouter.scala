
import akka.actor.ActorSystem
import akka.event.{ LoggingAdapter, Logging }
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.{ HttpResponse, HttpRequest }
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.{ ActorMaterializer, Materializer }
import akka.stream.scaladsl.{ Flow, Sink, Source }
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import java.io.IOException
import scala.concurrent.{ ExecutionContextExecutor, Future }
import spray.json.DefaultJsonProtocol

import Members._

case class MemberInfo(id: String, name: Option[String], first_name: Option[String], street: Option[String], city: Option[String])

trait Protocols extends DefaultJsonProtocol {
  implicit val memberInfoFormat = jsonFormat5(MemberInfo.apply)
}

trait Service extends Protocols {
  implicit val system: ActorSystem
  implicit def executor: ExecutionContextExecutor
  implicit val materializer: Materializer

  def config: Config
  val logger: LoggingAdapter

  def getMemberInfo(id: String): Future[Either[String, MemberInfo]] = {
    Members.members.find(m => m.id == id) match {
      case Some(m) => Future { Right(m) };
      case _       => Future { Left(s"Member with code $id not found.") };
    }
  }

  def getMembersInfo(): Future[String] = {
    Future { "Dit zou de lijst met members moeten zijn" };
  }

  def initMemberInfo(): Future[String] = {
    Members.init()
    Future { "Members initialised" };
  }

  def postMemberInfo(memberInfo: MemberInfo): Future[Either[String, String]] = {
    Members.members = memberInfo +: Members.members;
    Future { Right("Member added") };
  }

  val routes = {
    logRequestResult("akka-samen-fietsen") {
      pathPrefix("member") {
        (get & path(Segment)) { id =>
          complete {
            getMemberInfo(id).map[ToResponseMarshallable] {
              case Right(memberInfo)  => memberInfo;
              case Left(errorMessage) => errorMessage;
            }
          }
        } ~
          (post & entity(as[MemberInfo])) { memberInfo =>
            complete {
              postMemberInfo(memberInfo).map[ToResponseMarshallable] {
                case Right(confirmation) => confirmation;
                case Left(errorMessage)  => errorMessage;

              }
            }
          }
      } ~
        pathPrefix("members" / "init") {
          post {
            complete {
              initMemberInfo().map[ToResponseMarshallable] {
                case (confirmation) => confirmation;
              } 
              }
            }
          } ~
        path("members") {
          get {
            complete {
              getMembersInfo().map[ToResponseMarshallable] {
                case (confirmation) => confirmation
              }
            }
          }
        }
    }
  }
}

object HttpRouter extends App with Service {
  override implicit val system = ActorSystem()
  override implicit val executor = system.dispatcher
  override implicit val materializer = ActorMaterializer()

  override val config = ConfigFactory.load()
  override val logger = Logging(system, getClass)
  
  Members.init()

  Http().bindAndHandle(routes, config.getString("http.interface"), config.getInt("http.port"))
}
