package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def index() = Action { implicit request: Request[AnyContent] =>
    println(request.session.get("session_id"))
    Ok(views.html.index())
  }

  def json = Action { implicit request: Request[AnyContent] =>
    Ok("{\"originalValue\": 100}").as("application/json")
  }
}
