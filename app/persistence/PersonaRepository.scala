package persistence

import javax.inject.{Inject, Singleton}
import models.Persona
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class PersonaRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) (implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile]{

  import profile.api._

  private val Personas = TableQuery[PersonaTable]

  def all() : Future[Seq[Persona]] = db.run(Personas.result)

  def add(persona: Persona): Future[Unit] = db.run(Personas += persona).map(_ => ())

  def delete(persona: Persona) : Future[Unit] = {
    val q = Personas.filter(_.name === persona.nombre)
    val action = q.delete
    db.run(action).map(_ => ())
  }

  def buscarPorNombre(nombre:String) : Future[Seq[Persona]] = {
    val q = Personas.filter(_.name === nombre)
    val action = q.result
    db.run(action)
  }

  private class PersonaTable(tag: Tag) extends Table[Persona] (tag, "PERSONA"){
    def name = column[String]("NAME", O.PrimaryKey)
    def apellido = column[String]("APELLIDO")
    def edad = column[Int]("EDAD")

    def * = (name, apellido, edad) <> ( (Persona.apply _).tupled, Persona.unapply)
  }
}
