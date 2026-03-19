package com.ecommerce.inventory.entity

import jakarta.persistence.*

@Entity
@Table(name = "stock")
data class Stock(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val productId: Long,

    @Column(nullable = false)
    var quantity: Int
)
