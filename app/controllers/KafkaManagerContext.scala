/**
 * Copyright 2015 Yahoo Inc. Licensed under the Apache License, Version 2.0
 * See accompanying LICENSE file.
 */

package controllers

import kafka.manager.{DBManager, KafkaManager}
import play.api.Configuration
import play.api.inject.{ApplicationLifecycle}

import scala.concurrent.Future

/**
 * @author hiral
 */
class KafkaManagerContext (lifecycle: ApplicationLifecycle, configuration: Configuration) {
  private[this] val kafkaManager : KafkaManager = new KafkaManager(configuration.underlying)
  private[this] val dbManager : DBManager = new DBManager()

  lifecycle.addStopHook { () =>
    Future.successful(kafkaManager.shutdown())
  }

  def getKafkaManager : KafkaManager = kafkaManager
  def getDBManager : DBManager = dbManager
}
