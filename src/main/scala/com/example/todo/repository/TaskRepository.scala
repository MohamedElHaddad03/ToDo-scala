package com.example.todo.repository

import cats.effect.IO
import doobie.implicits._
import cats.implicits.toFunctorOps
import com.example.todo.model.{Task, TaskStatus}
import com.example.todo.model.TaskStatus.taskStatusMeta
import doobie._
import doobie.util.meta.Meta
import doobie.util.transactor.Transactor

import java.time.Instant
import java.util.UUID

class TaskRepository(xa: Transactor[IO]) {

  implicit val uuidMeta: Meta[UUID] =
    Meta[String].imap(UUID.fromString)(_.toString)

  implicit val instantMeta: Meta[Instant] =
    Meta[String].imap(Instant.parse)(_.toString)


  def init: ConnectionIO[Unit] =
    sql"""
      CREATE TABLE IF NOT EXISTS tasks (
        id TEXT PRIMARY KEY,
        title TEXT NOT NULL,
        description TEXT,
        status TEXT NOT NULL,
        created_at TEXT NOT NULL,
        updated_at TEXT
      )
    """.update.run.void

  def getAll: IO[List[Task]] =
    sql"SELECT id, title, description, status, created_at, updated_at FROM tasks"
      .query[Task]
      .to[List]
      .transact(xa)

  def get(id: UUID): IO[Option[Task]] =
    sql"SELECT * FROM tasks WHERE id = $id"
      .query[Task]
      .option
      .transact(xa)

  def create(title: String, description: Option[String]): IO[Int] ={
    val createdAt = Instant.now()
    val id = UUID.randomUUID()
    sql"""
      INSERT INTO tasks ( id,title, description ,status, created_at)
      VALUES (${id}, ${title}, ${description},'todo',  ${createdAt})
    """.update.run.transact(xa)
    }

  def update(id: UUID, title: String, desc: Option[String], status: TaskStatus): IO[Option[Task]] = {
    val updatedAt = Instant.now()
    val updateQuery =
      sql"""
        UPDATE tasks
        SET title = $title,
            description = $desc,
            status = $status,
            updated_at = $updatedAt
        WHERE id = $id
      """.update.run.transact(xa)

    for {
      rowsAffected <- updateQuery
      updatedTask <- if (rowsAffected > 0) get(id) else IO.pure(None)
    } yield updatedTask
  }

  def delete(id: UUID): IO[Boolean] =
    sql"DELETE FROM tasks WHERE id = $id"
      .update.run
      .map(_ > 0)
      .transact(xa)
}
