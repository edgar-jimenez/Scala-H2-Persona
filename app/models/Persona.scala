package models

import play.api.libs.json.Json

case class Persona (nombre: String, apelido: String, edad: Int)

object Persona{
  implicit val personaFormat = Json.format[Persona]
}
