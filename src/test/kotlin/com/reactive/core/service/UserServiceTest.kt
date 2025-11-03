package com.reactive.core.service

import com.reactive.core.domain.entity.Order
import com.reactive.core.domain.enums.OrderStatus
import com.reactive.core.repository.OrderRepository
import com.reactive.core.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.math.BigDecimal
import java.time.LocalDateTime

class UserServiceTest {

    private fun makeOrder(id: Long, userId: Long, name: String): Order {
        return Order(
            id = id,
            userId = userId,
            productName = name,
            quantity = 1,
            totalAmount = BigDecimal("10.00"),
            address = "addr",
            status = OrderStatus.COMPLETED,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }

    @Test
    fun `given user has orders when getUserOrders page 1 size 2 then returns correct page slice`() {
        // given
        val userId = 1L
        val order3 = makeOrder(3, userId, "p3")
        val order4 = makeOrder(4, userId, "p4")

        val repo = Mockito.mock(OrderRepository::class.java)
        // For page=1, size=2 => limit=2, offset=2 => expect order3, order4
        given(repo.findByUserIdPaged(userId, 2, 2L)).willReturn(Flux.just(order3, order4))

        val userRepo = Mockito.mock(UserRepository::class.java)
        val svc = UserService(userRepo, repo)

        // when
        val result = svc.getUserOrders(userId, 1, 2)

        // then
        StepVerifier.create(result)
            .expectNext(order3)
            .expectNext(order4)
            .verifyComplete()
    }

    @Test
    fun `given invalid page and size when getUserOrders then normalizes to defaults`() {
        // given
        val userId = 1L
        val repo = Mockito.mock(OrderRepository::class.java)

        val captured = arrayOfNulls<Any>(2)
        given(repo.findByUserIdPaged(Mockito.eq(userId), Mockito.anyInt(), Mockito.anyLong())).willAnswer { invocation ->
            captured[0] = invocation.getArgument<Int>(1)
            captured[1] = invocation.getArgument<Long>(2)
            Flux.empty<Order>()
        }

        val userRepo = Mockito.mock(UserRepository::class.java)
        val svc = UserService(userRepo, repo)

        // when & then (service should normalize invalid inputs)
        StepVerifier.create(svc.getUserOrders(userId, -5, 0)).verifyComplete()

        // then: default size is 20, default page becomes 0 -> offset 0
        assertEquals(20, captured[0] as Int)
        assertEquals(0L, captured[1] as Long)
    }
}
