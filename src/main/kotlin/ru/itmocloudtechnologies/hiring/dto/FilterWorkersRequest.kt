package ru.itmocloudtechnologies.hiring.dto

import ru.itmocloudtechnologies.hiring.model.OrganizationType
import ru.itmocloudtechnologies.hiring.model.Position
import ru.itmocloudtechnologies.hiring.model.Status

data class FilterWorkersRequest(
    val name: String?,
    val coordinatesX: Double?,
    val coordinatesY: Double?,
    val salary: Float?,
    val position: Position?,
    val status: Status?,
    val organizationType: OrganizationType?,
    val sortedDirection: String = "ASC",
    val sortedColumn: String = "id",
    var pageNum: Int = 0,
    var pageSize: Int = 5
)