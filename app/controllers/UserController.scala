package controllers

import javax.inject._
import models.{User, UserForm}
import play.api.i18n.{Messages, I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}
import services.UserService

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
/**
  * Created by Bala.
  */
class UserController @Inject()(userService: UserService, val messagesApi: MessagesApi) extends Controller with I18nSupport{

  def home = Action.async { implicit  request =>
    userService.listAllUsers map { users =>
      Ok(views.html.user(UserForm.form,users))
    }
  }

  def addUser() = Action.async { implicit request =>
    UserForm.form.bindFromRequest.fold(
      // if any error in submitted data
      errorForm => Future.successful(Ok(views.html.user(errorForm,Seq.empty[User]))),
      data => {
        val newUser = User(0,data.firstName,data.lastName,data.mobile,data.email)
        userService.addUser(newUser).map(res =>
          Redirect(routes.UserController.home()).flashing(Messages("flash.success") -> res)
        )
      })
  }

  def deleteUser(id : Long) = Action.async { implicit request =>
    userService.deleteUser(id) map { res =>
      Redirect(routes.UserController.home())
    }
  }


}
