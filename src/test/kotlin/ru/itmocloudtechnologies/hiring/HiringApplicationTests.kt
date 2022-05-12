package ru.itmocloudtechnologies.hiring

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.itmocloudtechnologies.hiring.model.*
import ru.itmocloudtechnologies.hiring.repository.WorkerRepository
import java.math.BigDecimal


@SpringBootTest
class HiringApplicationTests {

    @Autowired
    private lateinit var repository: WorkerRepository

    @Test
    fun contextLoads() {
        var worker = Worker(
            name = "John",
            coordinates = Coordinates(
                BigDecimal(333.3),
                113.0
            ),
            salary = 100_000.0f,
            position = Position.DEVELOPER,
            status = Status.REGULAR,
            organizationType = OrganizationType.COMMERCIAL
        )

        worker = repository.save(worker)

        Assertions.assertTrue(repository.findById(worker.id).isPresent)
    }

}
