package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class AdminController @Inject()(
  cc: ControllerComponents
) extends AbstractController(cc) {

  /** 管理者用ページの表示
    *
    * @return
    */
  def admin = Action {
    Ok
  }
}
