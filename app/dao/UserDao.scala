package dao

import javax.inject.Inject
import models.User
import play.db.NamedDatabase
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Future

class UserDao @Inject()(
  @NamedDatabase("portfolio_db") protected val dbConfigProvider: DatabaseConfigProvider
) extends HasDatabaseConfigProvider[JdbcProfile]{

  private val Users = TableQuery[UserTable]

  def findUserByEmail(email: String): Future[Option[User]] = {
    db.run(Users.filter(_.email === email).result.headOption)
  }

}

case class UserTable(tag: Tag) extends Table[User](tag, "user") {
  def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def email = column[String]("email")
  override def * = (id, name, email) <> (User.tupled, User.unapply)
}
