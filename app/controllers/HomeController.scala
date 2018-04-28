package controllers

import dao.IdValueDao
import javax.inject._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class HomeController @Inject()(
  cc: ControllerComponents,
  dao: IdValueDao
) extends AbstractController(cc) {

  def index() = Action { implicit request: Request[AnyContent] =>
    println(request.session.get("session_id"))
    Ok(views.html.index())
  }

  def json(id: Long) = Action.async { implicit request: Request[AnyContent] =>
    dao.findIdValueById(id).map {
      case Some(iv) =>
        val value = iv.value
        val jsonValue = "{\"originalValue\":" + value + "}"
        Ok(jsonValue).as("application/json")
      case None => Ok("{\"error\": \"該当するidが見つかりません\"}")
    }
  }

  def updateValue = Action { implicit request: Request[AnyContent] =>
    Redirect("/").flashing("infoMsg" -> "正常に更新されました")
  }

}
