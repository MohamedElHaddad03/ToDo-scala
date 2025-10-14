package com.example.todo.model

import doobie.util.Put
import doobie.util.meta.Meta
import io.circe.Json
import io.circe.syntax.EncoderOps

sealed trait TaskStatus
object TaskStatus {
  case object Todo extends TaskStatus
  case object InProgress extends TaskStatus
  case object Done extends TaskStatus

  import io.circe.{Decoder, Encoder}
  import io.circe.generic.extras.Configuration
  import io.circe.generic.extras.semiauto._

  implicit val customConfig: Configuration = Configuration.default.withSnakeCaseConstructorNames

  implicit val encodeTaskStatus: Encoder[TaskStatus] = Encoder.encodeString.contramap {
    case Todo => "todo"
    case InProgress => "in_progress"
    case Done => "done"
  }

  implicit val decodeTaskStatus: Decoder[TaskStatus] = Decoder.decodeString.emap {
    case "todo" => Right(Todo)
    case "in_progress" => Right(InProgress)
    case "done" => Right(Done)
    case other => Left(s"Invalid task status: $other")
  }
  implicit val taskStatusMeta: Meta[TaskStatus] = Meta[String].imap {
    case "todo"        => TaskStatus.Todo: TaskStatus
    case "in_progress" => TaskStatus.InProgress: TaskStatus
    case "done"        => TaskStatus.Done: TaskStatus
    case other         => throw new IllegalArgumentException(s"Unknown TaskStatus: $other")
  } {
    case TaskStatus.Todo       => "todo"
    case TaskStatus.InProgress => "in_progress"
    case TaskStatus.Done       => "done"
  }


}
