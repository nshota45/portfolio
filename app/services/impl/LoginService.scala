package services.impl

import javax.inject._

import config.GoogleOauthConfig
import dao.UserDao
import models.User

import org.apache.commons.codec.binary.Base64

import play.api.libs.ws.WSClient
import play.api.libs.json.Json
import play.api.mvc.{AnyContent, Request}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

class LoginService @Inject()(
  ws: WSClient,
  config: GoogleOauthConfig,
  dao: UserDao
) extends services.LoginService {

  override def authorizationUrl: String = {
    s"${config.authorizationEndpoint}?" +
      s"client_id=${config.clientId}" +
      s"&response_type=${config.responseType}" +
      s"&scope=${config.scope}" +
      s"&redirect_uri=${config.redirectUri}"
  }

  override def auth(request: Request[AnyContent]): Future[User] = {
    for {
      _     <- confirmState(request)
      code  <- getCode(request)
      token <- exchangeCodeForToken(code)
      email <- getEmail(token)
      user  <- getUser(email)
    } yield user
  }


  private def confirmState(request: Request[AnyContent]): Future[Unit] = {
    (for {
      paramState <- request.getQueryString("state")
      sessionState <- request.session.get("state")
    } yield paramState == sessionState) match {
      case Some(bool) if bool => Future(Unit)
      case None               => Future(throw new Exception("Invalid state."))
    }
  }

  private def getCode(request: Request[AnyContent]): Future[String] = {
    request.getQueryString("code") match {
      case Some(code) => Future(code)
      case None       => Future(throw new Exception("Not found code."))
    }
  }

  private def exchangeCodeForToken(code: String): Future[String] = {
    val request =
      ws
      .url(config.tokenEndpoint)
      .withHttpHeaders("Accept" -> "application/json")
      .withRequestTimeout(config.requestTimeOut.millis)

    val body = Map(
      "code" -> Seq(code),
      "client_id" -> Seq(config.clientId),
      "client_secret" -> Seq(config.clientSecret),
      "redirect_uri" -> Seq(config.redirectUri),
      "grant_type" -> Seq(config.grantType)
    )

    request.post(body).map{ response => (response.json \ "id_token").as[String] }
  }

  private def getEmail(idToken: String): Future[String] = {
    Future {
      val tokenSegment = idToken.split("\\.")
      val base64EncodedClaims = tokenSegment(1)
      val claims = new String(Base64.decodeBase64(base64EncodedClaims))
      val json = Json.parse(claims)
      (json \ "email").as[String]
    }
  }

  private def getUser(email: String): Future[User] = {
    dao.findUserByEmail(email).map {
      case Some(user) => user
      case None       => throw new Exception("User not found.")
    }
  }
}
