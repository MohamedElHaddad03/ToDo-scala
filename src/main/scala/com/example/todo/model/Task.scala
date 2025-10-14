package com.example.todo.model
import java.util.UUID
import java.time.Instant


case class Task(
                 id: UUID,
                 title: String,
                 description: Option[String],
                 status: TaskStatus,
                 createdAt: Instant,
                 updatedAt: Option[Instant]
               )
