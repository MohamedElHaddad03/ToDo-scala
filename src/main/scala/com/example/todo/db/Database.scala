package com.example.todo.db

import cats.effect._
import doobie.hikari.HikariTransactor
import scala.concurrent.ExecutionContext

object Database {

  def transactor[F[_]: Async](dbPath: String)(implicit ec: ExecutionContext): Resource[F, HikariTransactor[F]] = {
    val url = s"jdbc:sqlite:$dbPath"

    HikariTransactor.newHikariTransactor[F](
      driverClassName = "org.sqlite.JDBC",
      url = url,
      user = "",
      pass = "",
      connectEC = ec
    )
  }
}
