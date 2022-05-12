package ru.itmocloudtechnologies.hiring.model

import java.math.BigDecimal
import javax.persistence.Embeddable
import javax.validation.constraints.DecimalMax

@Embeddable
data class Coordinates(
    @DecimalMax(value = "508.0")
    private val x: BigDecimal,
    private val y: Double
) {
}