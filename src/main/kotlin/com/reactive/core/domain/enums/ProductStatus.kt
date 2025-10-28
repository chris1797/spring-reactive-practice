package com.reactive.core.domain.enums

enum class ProductStatus(val value: Int) {
    INACTIVE(0),
    ACTIVE(1);

    companion object {
        fun fromValue(value: Int): ProductStatus {
            return entries.find { it.value == value }
                ?: throw IllegalArgumentException("Invalid ProductStatus value: $value")
        }
    }
}