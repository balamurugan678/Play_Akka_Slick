package services

import javax.inject.{Inject, Singleton}

import dao.UserDAO
import models.User

import scala.concurrent.Future

/**
  * Created by Bala.
  */
@Singleton
class UserServiceImpl @Inject()(userDAO: UserDAO) extends UserService {
  override def addUser(user: User): Future[String] = {
    userDAO.add(user)
  }

  override def deleteUser(id: Long): Future[Int] = {
    userDAO.delete(id)
  }

  override def getUser(id: Long): Future[Option[User]] = {
    userDAO.get(id)
  }

  override def listAllUsers: Future[Seq[User]] = {
    userDAO.listAll
  }
}
