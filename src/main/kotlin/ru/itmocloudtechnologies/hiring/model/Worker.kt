package ru.itmocloudtechnologies.hiring.model

import java.time.LocalDate
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Entity
data class Worker(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @NotEmpty
    @Column(nullable = false)
    val name: String,

    @NotNull
    @Column(nullable = false)
    val coordinates: Coordinates,

    @NotNull
    @Column(nullable = false)
    val salary: Float,

    @NotNull
    @Column(nullable = false)
    val position: Position,

    @NotNull
    @Column(nullable = false)
    val status: Status,

    @NotNull
    @Column(nullable = false)
    val organizationType: OrganizationType,
) {
    @Column(nullable = false)
    val creationDate: LocalDate = LocalDate.now()
}