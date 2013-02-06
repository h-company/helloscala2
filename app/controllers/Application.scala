package controllers

import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import jp.t2v.lab.play20.auth._
import views._

object Application extends Controller with LoginLogout with AuthConfigImpl with Auth {

  type LoginForm = Form[(String, String)]

  val loginForm = Form {
    tuple("email" -> email, "password" -> nonEmptyText)
  }

  def index = authorizedAction(models.NormalUser) { user => request =>
    Ok(views.html.index(user.fullname))
  }

  def login = Action {
    Ok(views.html.login(loginForm))
  }

  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
    formWithErrors => BadRequest(html.login(formWithErrors)),
    {
      case (email, password) =>
        models.User.authenticate(email, password)
          .map(_.id)
          .map(gotoLoginSucceeded)
          .getOrElse(
          BadRequest(html.login(loginForm.fill((email, "")).withGlobalError("emailもしくはパスワードが間違っています。"))))
    }
    )
  }
  def logout = Action { implicit request =>
  // do something...
    gotoLogoutSucceeded
  }



//  def authenticate = Action { implicit request =>
//    loginForm.bindFromRequest.fold(
//      formWithErrors => BadRequest(html.login(formWithErrors)),
//      {
//         case (email, password) =>
//               models.User.authenticate(email, password).map(_.id).map(gotoLoginSucceeded).getOrElse(BadRequest(html.login(loginForm.fill((email, "")))))
//           }
//       )
//     }
}