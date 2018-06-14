package controllers

import javax.inject._
import models.{ Persona}
import persistence.{ PersonaRepository}
import play.api.libs.json.{Json}
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class HomeController @Inject()(cc: ControllerComponents, personaRepository: PersonaRepository)(implicit executionContext: ExecutionContext)
  extends AbstractController(cc) {

  populateDate()

  def listarPersonas() = Action.async{ implicit request: Request[AnyContent] =>
    val fPersonas: Future[Seq[Persona]] = personaRepository.all()

    fPersonas.map(s => Ok( Json.toJson(s)))
  }

  def addPersona() = Action.async(parse.json[Persona]) { request =>
    insertPersona(request.body)
  }

  def eliminarPersona() = Action.async(parse.json[Persona]) {request =>
    deletePersona(request.body)
  }

  def buscar(nombre: String)= Action.async{ implicit request: Request[AnyContent] =>
    val resultado: Future[Seq[Persona]] = personaRepository.buscarPorNombre(nombre)
    resultado
      .map(s => Ok(Json.toJson(s.headOption.get)))
      .recoverWith{
        case error: Exception => {
          error.printStackTrace(System.err)
          Future.successful( InternalServerError(s"No se encontro ninguna persona con el nombre: $nombre"))
        }
      }
  }

  private def deletePersona(persona: Persona): Future[Result] = {
    personaRepository.delete(persona)
      .map(_ => Ok("Persona Elimino Con Exito"))
      .recoverWith{
        case error: Exception => {
          error.printStackTrace(System.err)
          Future.successful( InternalServerError("no se pudo eliminar a la persona "))
        }
      }
  }

  private def insertPersona(persona: Persona): Future[Result] = {
    personaRepository.add(persona)
      .map(_ => Ok("Persona Agregada Con Exito"))
      .recoverWith{
        case error: Exception => {
          error.printStackTrace(System.err)
          Future.successful( InternalServerError(s"no se pudo agragr a la persona: $persona"))
        }
      }
  }

  private def populateDate() {
    insertPersona(new Persona("Edgar", "Jimenez", 24))
    insertPersona(new Persona("Andres", "Mangones", 24))
    insertPersona(new Persona("Juna", "Castillo", 18))
  }

}
