package ru.itmocloudtechnologies.hiring.model

import javax.persistence.Column
import javax.persistence.Embeddable
import javax.validation.constraints.Positive

@Embeddable
data class Organization(
    @field:Positive
    @Column(nullable = false)
    private val employeesCount: Int?,

    @Column(nullable = true)
    private val type: OrganizationType?
)