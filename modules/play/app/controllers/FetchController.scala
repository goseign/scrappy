package controllers

import javax.inject.{Inject, Singleton}

import com.airbnbData.repository.{AirbnbScrapRepository, PropertyRepository}
import com.airbnbData.service.AirbnbScrapService
import slick.jdbc.JdbcBackend._
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, Controller}
// In order to evaluate tasks, we'll need a Scheduler
import monix.execution.Scheduler.Implicits.global
// A Future type that is also Cancelable
import monix.execution.CancelableFuture


@Singleton
class FetchController @Inject() (airbnbScrapService: AirbnbScrapService, airbnbScrapRepository: AirbnbScrapRepository, propertyRepo: PropertyRepository, client: WSClient, db: DatabaseDef) extends Controller {

  def download = Action.async {

//    val e = airbnbScrapService
//      .scrap2(1)(
//        airbnbScrapRepository.scrap2
//      )
//      .run(client)
//      .map { result =>
//        println(result)
//        Ok(views.html.airbnb(result.foldLeft("") { case (acc, i) => acc + i.toString + "\n" }))
//      }
//          .subscribe({result =>})

    airbnbScrapService
      .scrap(
        propertyRepo.bulkCreate,
        airbnbScrapRepository.scrap,
        propertyRepo.close,
        propertyRepo.deleteAll
      )
      .run((client, db))
      .map { result =>
        println(result)
        Ok(views.html.airbnb(result))
      }
      .runAsync
  }
}
