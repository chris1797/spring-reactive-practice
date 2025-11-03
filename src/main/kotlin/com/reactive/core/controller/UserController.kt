package com.reactive.core.controller

import com.reactive.core.domain.entity.Order
import com.reactive.core.domain.entity.User
import com.reactive.core.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {
    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): Mono<User> {
        return userService.getUserById(id)
    }

    @GetMapping("/orders/{userId}")
    fun getUserOrders(
        @PathVariable userId: Long,
        @RequestParam(name = "page", defaultValue = "0") page: Int,
        @RequestParam(name = "size", defaultValue = "20") size: Int
    ): Flux<Order> {
        return userService.getUserOrders(userId, page, size)
    }
}