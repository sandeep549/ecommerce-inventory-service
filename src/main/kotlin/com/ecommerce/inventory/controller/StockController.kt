package com.ecommerce.inventory.controller

import com.ecommerce.common.dto.StockDTO
import com.ecommerce.inventory.service.StockService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

data class StockUpdateRequest(val quantity: Int)
data class StockDecrementRequest(val quantity: Int)
data class AvailabilityResponse(val productId: Long, val available: Boolean)

@RestController
@RequestMapping("/stock")
class StockController(private val stockService: StockService) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("/{productId}")
    fun getStock(@PathVariable productId: Long): StockDTO {
        logger.info("GET /stock/{}", productId)
        return stockService.getStock(productId)
    }

    @PutMapping("/{productId}")
    fun setStock(@PathVariable productId: Long, @RequestBody req: StockUpdateRequest): StockDTO {
        logger.info("PUT /stock/{} - quantity={}", productId, req.quantity)
        return stockService.setStock(productId, req.quantity)
    }

    @PostMapping("/{productId}/decrement")
    fun decrementStock(@PathVariable productId: Long, @RequestBody req: StockDecrementRequest): StockDTO {
        logger.info("POST /stock/{}/decrement - quantity={}", productId, req.quantity)
        return stockService.decrementStock(productId, req.quantity)
    }

    @GetMapping("/{productId}/available")
    fun checkAvailability(@PathVariable productId: Long,
                          @RequestParam quantity: Int): ResponseEntity<AvailabilityResponse> {
        logger.debug("GET /stock/{}/available - quantity={}", productId, quantity)
        return ResponseEntity.ok(AvailabilityResponse(productId, stockService.isAvailable(productId, quantity)))
    }
}
