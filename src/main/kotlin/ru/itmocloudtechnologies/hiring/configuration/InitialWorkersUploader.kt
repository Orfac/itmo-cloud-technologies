package ru.itmocloudtechnologies.hiring.configuration

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import ru.itmocloudtechnologies.hiring.model.*
import ru.itmocloudtechnologies.hiring.service.WorkerService
import kotlin.random.Random

@Configuration
class InitialWorkersUploader(workerService: WorkerService) {
    companion object {
        private const val SEED = 12345
        val random = Random(SEED)
        val logger: Logger = LoggerFactory.getLogger(InitialWorkersUploader::class.java)
    }

    init {
        logger.info("Clearing existing data")
        workerService.deleteAll()
        logger.info("Clearing of existing data is finished")


        logger.info("Random data is started to uploading")
        for (i in 0..10){
            workerService.save(getRandomWorker())
        }
        logger.info("Random data is uploaded")
    }

    private fun getRandomWorker(): Worker {
        return Worker(
            name = randomName(),
            status = Status.HIRED,
            coordinates = Coordinates(random.nextDouble().toBigDecimal(), random.nextDouble()),
            organizationType = OrganizationType.COMMERCIAL,
            position = Position.DIRECTOR,
            salary = random.nextFloat()
        )
    }


    private fun randomName() = arrayOf(
        "Sergey", "Mark",
        "Bobby", "Jeff",
        "Pavel", "Donald",
        "Roberto", "Mihail",
        "Vladimir", "Vitaliy")
        .random()
}