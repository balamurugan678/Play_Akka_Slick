package models

import java.time.LocalDate

/**
  * Created by Bala.
  */
case class Game(code: String, saleStartDate: LocalDate, saleEndDate: LocalDate, travelStartDate: LocalDate, travelEndDate: LocalDate, refCodeExpiry: Int, maxPassengers: Int, minPassengers: Int, maxAge: Int, minAge: Int, classOfService: String, tripType: String, displayAvailability: Boolean, displayPrice: Boolean, availabilityThreshold: Int, referenceCodeType: String, posList: Seq[PointOfSales], timeSlotList: Seq[TimeSlot], reminderEmailHourList: Seq[String]) {

}
