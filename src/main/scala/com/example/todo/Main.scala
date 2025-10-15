package com.example.todo

import doobie.implicits._
import cats.effect.{ExitCode, IO, IOApp}
import com.example.todo.db.Database
import com.example.todo.repository.TaskRepository
import com.example.todo.route.TaskRoutes
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze._

import scala.concurrent.ExecutionContext.Implicits.global

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    implicit val ec = scala.concurrent.ExecutionContext.global

    Database.transactor[IO]("tasks.db").use { xa =>
      val repo = new TaskRepository(xa)

      // Run DB setup in IO by lifting it with `.transact(xa)`
      for {
        _ <- repo.init.transact(xa)

        httpApp = Router(
          "/api" -> TaskRoutes.routes(repo)
        ).orNotFound

        // ✅ IO context: start the server
        _ <- BlazeServerBuilder[IO]
          .bindHttp(8080, "0.0.0.0")
          .withHttpApp(httpApp)
          .serve
          .compile
          .drain
      } yield ExitCode.Success
    }
  }
}
