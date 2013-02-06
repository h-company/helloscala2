package controllers

import jp.t2v.lab.play20.auth._
import play.api.mvc._
import play.api.mvc.Results._


/**
 * Created with IntelliJ IDEA.
 * User: tsutomu
 * Date: 2013/01/26
 * Time: 19:09
 * To change this template use File | Settings | File Templates.
 */
trait AuthConfigImpl extends AuthConfig {
  /**
   * ユーザを識別するIDの型です。String や Int や Long などが使われるでしょう。
   */
  type Id = Long
  /**
   * あなたのアプリケーションで認証するユーザを表す型です。
   * User型やAccount型など、アプリケーションに応じて設定してください。
   */
  type User = models.User

  /**
   * 認可(権限チェック)を行う際に、アクション毎に設定するオブジェクトの型です。
   * このサンプルでは例として以下のような trait を使用しています。
   *
   * sealed trait Permission
   * case object Administrator extends Permission
   * case object NormalUser extends Permission
   */
  type Authority = models.Role

  /**
   * CacheからユーザIDを取り出すための ClassManifest です。
   * 基本的にはこの例と同じ記述をして下さい。
   */
  val idManifest: ClassManifest[Id] = classManifest[Id]

  /**
   * セッションタイムアウトの時間(秒)です。
   */
  val sessionTimeoutInSeconds: Int = 3600

  /**
   * ユーザIDからUserブジェクトを取得するアルゴリズムを指定します。
   * 任意の処理を記述してください。
   */
  def resolveUser(id: Id): Option[User] = models.User.findById(id)

  /**
   * ログインが成功した際に遷移する先を指定します。
   */
  def loginSucceeded(request: RequestHeader): Result = Redirect(routes.Application.index())

  /**
   * ログアウトが成功した際に遷移する先を指定します。
   */
  def logoutSucceeded(request: RequestHeader): Result = Redirect(routes.Application.login())

  /**
   * 認証が失敗した場合に遷移する先を指定します。
   */
  def authenticationFailed(request: RequestHeader): Result = Redirect(routes.Application.login())

  /**
   * 認可(権限チェック)が失敗した場合に遷移する先を指定します。
   */
  def authorizationFailed(request: RequestHeader): Result = Forbidden("no permission")

  /**
   * 権限チェックのアルゴリズムを指定します。
   * 任意の処理を記述してください。
   */
  def authorize(user: User, authority: Authority): Boolean =
    (user.isAdmin, authority) match {
      case (true, _) => true
      case (false, models.NormalUser) => true
      case _ => false
    }

//  /**
//   * SessionID Cookieにsecureオプションを指定するか否かの設定です。
//   * デフォルトでは利便性のために false になっていますが、
//   * 実際のアプリケーションでは true にすることを強く推奨します。
//   */
//  override lazy val cookieSecureOption: Boolean = play.api.Play.current.configuration.getBoolean("auth.cookie.secure").getOrElse("true")

}
