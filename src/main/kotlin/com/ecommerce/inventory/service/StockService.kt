package com.ecommerce.inventory.service

import com.ecommerce.common.dto.StockDTO
import com.ecommerce.common.exception.InsufficientStockException
import com.ecommerce.common.exception.ResourceNotFoundException
import com.ecommerce.inventory.entity.Stock
import com.ecommerce.inventory.repository.StockRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StockService(private val stockRepository: StockRepository) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun getStock(productId: Long): StockDTO {
        logger.debug("getStock - productId={}", productId)
        return stockRepository.findByProductId(productId)
            .orElseThrow { ResourceNotFoundException("Stock not found for product: $productId") }
            .toDTO()
    }

    @Transactional
    fun setStock(productId: Long, quantity: Int): StockDTO {
        val stock = stockRepository.findByProductId(productId)
            .orElse(Stock(productId = productId, quantity = 0))
        val previous = stock.quantity
        stock.quantity = quantity
        val saved = stockRepository.save(stock).toDTO()
        logger.info("Stock set - productId={}, previous={}, new={}", productId, previous, quantity)
        return saved
    }

    @Transactional
    fun decrementStock(productId: Long, quantity: Int): StockDTO {
        val stock = stockRepository.findByProductId(productId)
            .orElseThrow { ResourceNotFoundException("Stock not found for product: $productId") }
        if (stock.quantity < quantity) {
            logger.warn("Insufficient stock - productId={}, available={}, requested={}", productId, stock.quantity, quantity)
            throw InsufficientStockException("Insufficient stock for product $productId. Available: ${stock.quantity}, Requested: $quantity")
        }
        stock.quantity -= quantity
        val saved = stockRepository.save(stock).toDTO()
        logger.info("Stock decremented - productId={}, decrementedBy={}, remaining={}", productId, quantity, saved.quantity)
        return saved
    }

    fun isAvailable(productId: Long, quantity: Int): Boolean {
        val available = stockRepository.findByProductId(productId)
            .map { it.quantity >= quantity }
            .orElse(false)
        logger.debug("isAvailable - productId={}, requested={}, available={}", productId, quantity, available)
        return available
    }

    private fun Stock.toDTO() = StockDTO(productId = productId, quantity = quantity)
}
