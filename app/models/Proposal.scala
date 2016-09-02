package models

import java.time.LocalDate

/**
  * Created by Bala.
  */
case class Proposal(originCode : String, destinationCode:String, outboundDate: LocalDate, passengers: Seq[Passenger]) {

}
