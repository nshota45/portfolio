package services

import models.User
import play.api.mvc.{AnyContent, Request}

import scala.concurrent.Future

trait LoginService {
  def authorizationUrl: String
  def auth(request: Request[AnyContent]): Future[Option[User]]
}
