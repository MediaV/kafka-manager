package controllers

import features.ApplicationFeatures
import models.navigation.Menus
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._

class Quota(val messagesApi: MessagesApi, val kafkaManagerContext: KafkaManagerContext)
            (implicit af: ApplicationFeatures, menus: Menus) extends Controller with I18nSupport {

  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  private[this] val dbManager = kafkaManagerContext.getDBManager

  def quota(c: String) = Action.async {
    dbManager.getQuotaListExtended(c).map {errorOrQuotaList=>
      Ok(views.html.quota.quotaList(c, errorOrQuotaList))
    }
  }
}

