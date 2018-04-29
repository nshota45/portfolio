package controllers

import dao.IdValueDao
import javax.inject._
import play.api.mvc._

import play.api.data._
import play.api.data.Forms._

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class HomeController @Inject()(
  cc: ControllerComponents,
  dao: IdValueDao
) extends AbstractController(cc) {

  case class FormData(id: Long, after: Int)
  case class FormsData(forms: List[FormData])

  private val formData = Form(
    mapping(
      "forms" -> list(mapping(
        "id" -> longNumber,
        "after" -> number
      )(FormData.apply)(FormData.unapply))
    )(FormsData.apply)(FormsData.unapply)
  )

  def index() = Action { implicit request: Request[AnyContent] =>
    println(request.session.get("session_id"))
    Ok(views.html.index())
  }

  def form = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.form())
  }

  def postForm = Action { implicit request: Request[AnyContent] =>
    formData.bindFromRequest.fold(
      e => {
        Redirect("/form").flashing("errMsg" -> e.errors.mkString(","))
      },
      f => {
        f.forms.foreach { r =>
          println(r)
          dao.updateValueById(id = r.id, value = r.after)
        }
        Redirect("/form").flashing("infoMsg" -> "登録に成功しました")
      }
    )
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
