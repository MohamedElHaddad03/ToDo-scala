package com.example.todo.route

import com.example.todo.repository.TaskRepository
import com.example.todo.model._
import cats.effect._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.circe._
import io.circe.generic.auto._
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder

object TaskRoutes {

  case class CreateTaskRequest(title: String, description: Option[String])
  case class UpdateTaskRequest(title: String, description: Option[String], status: TaskStatus)

  def routes(repo: TaskRepository): HttpRoutes[IO] = {
    implicit val decoderCreate = jsonOf[IO, CreateTaskRequest]
    implicit val decoderUpdate = jsonOf[IO, UpdateTaskRequest]

    HttpRoutes.of[IO] {
      case GET -> Root / "tasks" =>
        for {
          allTasks <- repo.getAll
          resp <- Ok(allTasks)
        } yield resp

      case GET -> Root / "tasks" / UUIDVar(id) =>
        repo.get(id).flatMap {
          case Some(task) => Ok(task)
          case None       => NotFound()
        }

      case req @ POST -> Root / "tasks" =>
        for {
          data <- req.as[CreateTaskRequest]
          task <- repo.create(data.title, data.description)
          resp <- Created(task)
        } yield resp

      case req @ PUT -> Root / "tasks" / UUIDVar(id) =>
        for {
          data <- req.as[UpdateTaskRequest]
          updated <- repo.update(id, data.title, data.description, data.status)
          resp <- updated match {
            case Some(t) => Ok(t)
            case None    => NotFound()
          }
        } yield resp

      case DELETE -> Root / "tasks" / UUIDVar(id) =>
        repo.delete(id).flatMap {
          case true  => NoContent()
          case false => NotFound()
        }
    }
  }
}
