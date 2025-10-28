package com.reactive.core.controller

import com.reactive.core.domain.entity.User
import com.reactive.core.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
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
}