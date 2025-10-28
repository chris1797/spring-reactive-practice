package com.reactive.core.domain.entity

import com.reactive.core.domain.enums.ProductStatus
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Table("products")
data class Product(
    @Id
    val id: Long? = null,

    val name: String?,

    val price: BigDecimal?,

    val status: ProductStatus?,

    @Column("created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column("updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
)