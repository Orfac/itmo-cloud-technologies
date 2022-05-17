package ru.itmocloudtechnologies.hiring.model

import ru.itmocloudtechnologies.hiring.validation.group.CreateGroup
import java.math.BigDecimal
import javax.persistence.Embeddable
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.NotNull

@Embeddable
data class Coordinates(
    @field:NotNull(groups = [CreateGroup::class])
    @field:DecimalMax("408.0")
    var x: BigDecimal?,

    @field:NotNull(groups = [])
    var y: Double?
)