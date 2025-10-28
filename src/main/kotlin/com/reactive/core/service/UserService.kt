package com.reactive.core.service

import com.reactive.core.domain.entity.User
import com.reactive.core.repository.UserRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun getUserById(id: Long): Mono<User> {
        return userRepository.findById(id)
    }
}