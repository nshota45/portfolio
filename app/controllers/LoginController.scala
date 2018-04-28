package controllers

import java.math.BigInteger
import java.security.SecureRandom
import javax.inject._
import play.api.Logger
import play.api.cache.SyncCacheApi
import play.api.mvc._
import services.LoginService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

@Singleton
class LoginController @Inject()(
  cc: ControllerComponents,
  cache: SyncCacheApi,
  service: LoginService
) extends AbstractController(cc) {


  def login = Action { implicit request: Request[AnyContent] =>
    val state = new BigInteger(130, new SecureRandom()).toString(32)
    val url = service.authorizationUrl + s"&state=$state"
    Ok(views.html.login(url)).withSession("state" -> state)
  }

  def auth = Action.async { implicit request: Request[AnyContent] =>
    service.auth(request).map {
      case Some(user) =>
        val sessionId = generateSessionId
        cache.set(sessionId, user, 1800.seconds)
        Redirect("/admin").withSession("session_id" -> sessionId)
      case None       => Redirect("/login").flashing("errMsg" -> "Not found user").withNewSession
    }.recover {
      case e: Exception =>
        Logger.error(e.getMessage, e)
        Redirect("/login").flashing("errMsg" -> e.getMessage).withNewSession
    }
  }

  def logout = Action { implicit request: Request[AnyContent] =>
    Redirect("/login").withNewSession
  }

  private def generateSessionId: String = new BigInteger(130, new SecureRandom()).toString(64)
}
