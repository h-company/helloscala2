package models

/**
 * Created with IntelliJ IDEA.
 * User: tsutomu
 * Date: 2013/01/26
 * Time: 19:13
 * To change this template use File | Settings | File Templates.
 */
sealed trait Role

case object Administrator extends Role

case object NormalUser extends Role
