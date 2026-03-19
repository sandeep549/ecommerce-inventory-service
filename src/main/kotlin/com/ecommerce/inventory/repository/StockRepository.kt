package com.ecommerce.inventory.repository

import com.ecommerce.inventory.entity.Stock
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface StockRepository : JpaRepository<Stock, Long> {
    fun findByProductId(productId: Long): Optional<Stock>
}
