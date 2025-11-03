package com.reactive.core.repository

import com.reactive.core.domain.entity.Order
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import org.springframework.data.r2dbc.repository.Query

@Repository
interface OrderRepository : ReactiveCrudRepository<Order, Long> {
    fun findByUserId(userId: Long): Flux<Order>

    @Query("SELECT * FROM orders WHERE user_id = :userId ORDER BY created_at DESC LIMIT :limit OFFSET :offset")
    fun findByUserIdPaged(userId: Long, limit: Int, offset: Long): Flux<Order>
}
