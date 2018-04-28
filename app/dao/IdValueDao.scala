package dao

import javax.inject.Inject
import models.IdValue
import play.db.NamedDatabase
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Future

class IdValueDao @Inject()(
  @NamedDatabase("portfolio_db") protected val dbConfigProvider: DatabaseConfigProvider
) extends HasDatabaseConfigProvider [JdbcProfile]{

  private val IdValues = TableQuery[IdValueTable]

  def findIdValueById(id: Long): Future[Option[IdValue]] = {
    db.run(IdValues.filter(_.id === id).result.headOption)
  }

  def updateValueById(id: Long, value: Int):Future[Int] = {
    db.run(IdValues.filter(_.id === id).map(_.value).update(value))
  }

}

case class IdValueTable(tag: Tag) extends Table[IdValue](tag, "id_value") {
  def id = column[Option[Long]]("id", O.PrimaryKey)
  def value = column[Int]("value")
  override def * = (id, value) <> (IdValue.tupled, IdValue.unapply)
}
