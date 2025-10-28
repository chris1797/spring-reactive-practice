package com.reactive.core.domain.entity

import com.reactive.core.domain.enums.OrderStatus
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Table("orders")
data class Order(
    @Id
    val id: Long? = null,

    @Column("user_id")
    val userId: Long?,

    @Column("product_name")
    val productName: String,

    val quantity: Int,

    @Column("total_amount")
    val totalAmount: BigDecimal,

    val address: String,

    val status: OrderStatus,

    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column("updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
)