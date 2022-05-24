package ru.itmocloudtechnologies.hiring.model

import ru.itmocloudtechnologies.hiring.validation.group.CreateGroup
import java.time.LocalDate
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
data class Worker(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @field:NotNull(groups = [CreateGroup::class])
    @field:Size(min = 1)
    @Column(nullable = false)
    var name: String?,

    @field:NotNull(groups = [CreateGroup::class])
    @field:Valid
    @Column(nullable = false)
    var coordinates: Coordinates?,

    @field:NotNull(groups = [CreateGroup::class])
    @Column(nullable = false)
    var salary: Float?,

    @field:NotNull(groups = [CreateGroup::class])
    @Column(nullable = false)
    var position: Position?,

    @field:NotNull(groups = [CreateGroup::class])
    @Column(nullable = false)
    // TODO добавить валидатор нормальный
    var status: Status?,

    @field:NotNull(groups = [CreateGroup::class])
    @Column(nullable = false)
    var organizationType: OrganizationType?,
) {
    @Column(nullable = false)
    val creationDate: LocalDate = LocalDate.now()
}