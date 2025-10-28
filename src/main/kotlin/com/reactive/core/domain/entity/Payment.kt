package com.reactive.core.domain.entity

import com.reactive.core.domain.enums.PaymentMethod
import com.reactive.core.domain.enums.PaymentStatus
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Table("payments")
data class Payment(
    @Id
    val id: Long? = null,

    @Column("user_id")
    val userId: Long,

    @Column("order_id")
    val orderId: Long,

    val amount: BigDecimal,

    val method: PaymentMethod,

    val status: PaymentStatus,

    @Column("tx_id")
    val txId: String,

    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column("updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
)