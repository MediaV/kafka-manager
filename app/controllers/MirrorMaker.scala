package controllers

import play.api.Play.current
import features.ApplicationFeatures
import models.navigation.Menus
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import com.mchange.v2.c3p0.ComboPooledDataSource
import play.api.Play

class MirrorMaker(val messagesApi: MessagesApi, val kafkaManagerContext: KafkaManagerContext)
            (implicit af: ApplicationFeatures, menus: Menus) extends Controller with I18nSupport {

  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  private[this] val dbManager = kafkaManagerContext.getDBManager

  def mirrormakers(c: String) = Action.async {
    dbManager.getMirrorMakerListExtended(c).map {errorOrMirrorMakerList =>
      Ok(views.html.mirrormaker.mirrormakerList(c, errorOrMirrorMakerList))
    }
  }
}

object DataSourceManager {
  val driver = Play.application.configuration.getString("db.default.driver")
  val url = Play.application.configuration.getString("db.default.url")
  val username = Play.application.configuration.getString("db.default.username")
  val password = Play.application.configuration.getString("db.default.password")
  val dataSource: ComboPooledDataSource = new ComboPooledDataSource()
  dataSource.setDriverClass(driver.get)
  dataSource.setJdbcUrl(url.get)
  dataSource.setUser(username.get)
  dataSource.setPassword(password.get)
  dataSource.setMaxPoolSize(5)
  dataSource.setMinPoolSize(1)
  dataSource.setAcquireIncrement(1)
  dataSource.setCheckoutTimeout(5000)
  dataSource.setIdleConnectionTestPeriod(120)
  dataSource.setMaxIdleTime(3600)
  dataSource.setMaxStatements(0)
  dataSource.setMaxStatementsPerConnection(0)
//  connectionPool.create(database, new DatabaseConfig(Some(driver), Some(url), Some(username), Some(password), None()), Play.application().configuration())
}
