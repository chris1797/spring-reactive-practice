package com.reactive.core.domain.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("board")
data class Board(
    @Id
    val id: Long? = null,

    val title: String,

    val content: String,

    @Column("user_id")
    val userId: Long?,

    @Column("created_at")
    val createdAt: String
)