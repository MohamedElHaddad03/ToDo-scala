/*package com.example.todo.route

import cats.effect.IO
import com.example.todo.repository.TaskRepository
import org.http4s.Method._
import org.http4s._
import org.http4s.implicits._

class TaskRoutesSpec extends CatsEffectSuite {

  private val repo = new TaskRepository
  private val routes = TaskRoutes.routes(repo).orNotFound

  test("POST /tasks creates a new task") {
    val json =
      """{"title": "Test task", "description": "A test"}"""

    val request = Request[IO](method = POST, uri = uri"/tasks")
      .withEntity(json)
      .withHeaders(Headers(Header("Content-Type", "application/json")))

    for {
      resp <- routes.run(request)
      body <- resp.as[String]
    } yield {
      assertEquals(resp.status, Status.Created)
      assert(body.contains("Test task"))
    }
  }

  test("GET /tasks returns list of tasks") {
    for {
      resp <- routes.run(Request[IO](GET, uri"/tasks"))
      body <- resp.as[String]
    } yield {
      assertEquals(resp.status, Status.Ok)
      assert(body.contains("Test task"))
    }
  }
}
*/