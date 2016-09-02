package models

import play.api.libs.json.Json

/**
  * Created by Bala.
  */
case class Offer(code : String, snap : String)

object Offer {
  implicit val offerFormat = Json.format[Offer]
}
