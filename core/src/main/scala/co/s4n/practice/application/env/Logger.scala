package co.s4n.practice.application.env

import org.slf4j.LoggerFactory

trait Logger {

  def loggerInformation(message: String): Unit = {
    logger.info(s"$message")
  }

  def loggerInfoError(message: String): Unit = {
    logger.error(message)
  }
  /*@volatile*/
  private lazy val logger: org.slf4j.Logger =
    LoggerFactory.getLogger(getClass.getName.stripSuffix("$"))
}
