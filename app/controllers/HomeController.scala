package controllers

import java.time.{LocalDate, LocalTime, Month}
import javax.inject._

import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.{Json, Writes}
import play.api.libs.ws.{WSClient, WSRequest, WSResponse}
import play.api.mvc._

import scala.concurrent.Future

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(ws: WSClient) extends Controller {

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index = Action {
    Ok(views.html.index("Your new application is ready. YAYY it is play with Akka and Slick"))
  }

  def show(id: Int) = Action {
    Ok(views.html.index("Your Id end point is awesome " + id))
  }

  val offerForm: Form[Offer] = Form {
    mapping(
      "code" -> text,
      "snap" -> text
    )(Offer.apply)(Offer.unapply)
  }

  def addOffer = Action { implicit request =>

    val offer = offerForm.bindFromRequest.get

    val url = "http://localhost:8085/booking/proposals/outward"

    val enovationUrl = "http://localhost:8080/administration/games"

    val wSRequest: WSRequest = ws.url(url)
    val snaprequest: WSRequest = ws.url(enovationUrl)

    val complexRequestBooking: WSRequest =
      wSRequest.withHeaders("Content-Type" -> "application/json", "cid" -> "1234567")
        .withQueryString("pos" -> "GBZVA")


    val complexRequestEnovation: WSRequest =
      snaprequest.withHeaders("Content-Type" -> "application/json", "cid" -> "12345")
    //.withQueryString("pos" -> "GBZVA")


    implicit val passengerWrites = new Writes[Passenger] {
      def writes(passenger: Passenger) = Json.obj(
        "passengerId" -> passenger.passengerId,
        "age" -> passenger.age,
        "passengerType" -> passenger.passengerType
      )
    }

    implicit val posWrites = new Writes[PointOfSales] {
      def writes(pointOfSales: PointOfSales) = Json.obj(
        "posCode" -> pointOfSales.posCode
      )
    }

    implicit val timeslotWrites = new Writes[TimeSlot] {
      def writes(timeSlot: TimeSlot) = Json.obj(
        "label" -> timeSlot.label,
        "group" -> timeSlot.group,
        "endExclusive" -> timeSlot.endExclusive,
        "startInclusive" -> timeSlot.startInclusive

      )
    }

    implicit val gameWrites = new Writes[Game] {
      def writes(game: Game) = Json.obj(
        "code" -> game.code,
        "saleStartDate" -> game.saleStartDate,
        "saleEndDate" -> game.saleEndDate,
        "travelStartDate" -> game.travelStartDate,
        "travelEndDate" -> game.travelEndDate,
        "refCodeExpiry" -> game.refCodeExpiry,
        "maxPassengers" -> game.maxPassengers,
        "minPassengers" -> game.minPassengers,
        "maxAge" -> game.maxAge,
        "minAge" -> game.minAge,
        "classOfService" -> game.classOfService,
        "tripType" -> game.tripType,
        "displayAvailability" -> game.displayAvailability,
        "availabilityThreshold" -> game.availabilityThreshold,
        "displayPrice" -> game.displayPrice,
        "referenceCodeType" -> game.referenceCodeType,
        "posList" -> game.posList,
        "timeSlotList" -> game.timeSlotList,
        "reminderEmailHourList" -> game.reminderEmailHourList
      )
    }


    implicit val gameListWrites = new Writes[GameList] {
      def writes(gameGroup: GameList) = Json.obj(
        "games" -> gameGroup.games
      )
    }

    val proposalDate: LocalDate = LocalDate.of(2016, Month.SEPTEMBER, 27)

    implicit val placeWrites = new Writes[Proposal] {
      def writes(place: Proposal) = Json.obj(
        "destinationCode" -> place.destinationCode,
        "originCode" -> place.originCode,
        "outboundDate" -> place.outboundDate,
        "passengers" -> place.passengers
      )
    }

    val saleStartDate: LocalDate = LocalDate.of(2016, Month.SEPTEMBER, 20)
    val saleEndDate: LocalDate = LocalDate.of(2016, Month.SEPTEMBER, 27)
    val travelStartDate: LocalDate = LocalDate.of(2016, Month.OCTOBER, 1)
    val travelEndDate: LocalDate = LocalDate.of(2016, Month.OCTOBER, 31)

    val startInclusive: LocalTime = LocalTime.of(1, 15)
    val endExclusive: LocalTime = LocalTime.of(11, 59)

    val game = Game(
      offer.code,
      saleStartDate,
      saleEndDate,
      travelStartDate,
      travelEndDate,
      7,
      4,
      1,
      81,
      16,
      "STANDARD",
      "FLEXIBLE",
      true,
      true,
      5,
      offer.snap,
      Seq(
        PointOfSales("GBZVA")
      ),
      Seq(
        TimeSlot("AM", "EN", "01:15", "11:59")
      ),
      Seq(
        "48", "72"
      )
    )

    val gameList = GameList(
      Seq(game)
    )
    val enovationJson = Json.toJson(gameList)
    val futureResponseSnap: Future[WSResponse] = complexRequestEnovation.post(enovationJson)

    val proposal = Proposal(
      "7015400",
      "8727100",
      proposalDate,
      Seq(
        Passenger("30", "ADULT", "1")
      )
    )
    val json = Json.toJson(proposal)
    val futureResponse: Future[WSResponse] = complexRequestBooking.post(json)
    Redirect(routes.HomeController.index())
  }

}
