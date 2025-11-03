package com.reactive.core.service

import com.reactive.core.domain.entity.User
import com.reactive.core.domain.entity.Order
import com.reactive.core.repository.UserRepository
import com.reactive.core.repository.OrderRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class UserService(
    private val userRepository: UserRepository,
    private val orderRepository: OrderRepository
) {
    fun getUserById(id: Long): Mono<User> {
        return userRepository.findById(id)
    }

    fun getUserOrders(userId: Long): Flux<Order> {
        return orderRepository.findByUserId(userId)
    }

    // 페이징 지원 메서드: page는 0 기반, size는 페이지 크기
    fun getUserOrders(userId: Long, page: Int, size: Int): Flux<Order> {
        val pageSafe = if (page < 0) 0 else page
        val sizeSafe = if (size <= 0) 20 else size
        val offset = pageSafe.toLong() * sizeSafe.toLong()
        val limit = sizeSafe
        return orderRepository.findByUserIdPaged(userId, limit, offset)
    }
}