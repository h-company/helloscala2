package models

import scalikejdbc._
import scalikejdbc.SQLInterpolation._

/**
 * Created with IntelliJ IDEA.
 * User: tsutomu
 * Date: 2013/01/26
 * Time: 17:31
 * To change this template use File | Settings | File Templates.
 */
case class User (id: Long, email: String, password: String, fullname: String, isAdmin: Boolean)

object User {

  private val * = (rs: WrappedResultSet) => User(
    id = rs.long("id"),
    email = rs.string("email"),
    password = rs.string("password"),
    fullname = rs.string("fullname"),
    isAdmin = rs.boolean("isAdmin")
  )

  def find(id: Long)(implicit session: DBSession = AutoSession): Option[User] = {
    SQL("select * from user where id = {id}").bindByName('id -> id).map(*).single.apply()
  }
  def findByEmail(email: String)(implicit session: DBSession = AutoSession): Option[User] = {
    sql"SELECT * FROM user WHERE email = ${email}".map(*).single.apply()
  }
  def findById(id: Long)(implicit session: DBSession = AutoSession): Option[User] = {
    sql"SELECT * FROM user WHERE id = ${id}".map(*).single.apply()
  }
  def authenticate(email: String, password: String)(implicit session: DBSession = AutoSession): Option[User] = {
    findByEmail(email).filter(_.password == password)
  }

}
