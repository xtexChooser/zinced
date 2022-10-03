package zinced.server.database

import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.Persistence
import kotlinx.coroutines.*
import org.hibernate.Transaction
import zinced.server.config.Config

object Database {

    val config get() = Config.database

    private val defaultProperties = mapOf(
        "hibernate.type.json_format_mapper" to "zinced.server.database.util.json.JsonMapper"
    )
    private val entityManagerFactory: EntityManagerFactory =
        Persistence.createEntityManagerFactory("main", config.properties + defaultProperties)
    val entityManager: EntityManager = entityManagerFactory.createEntityManager()

    val transaction get() = entityManager.transaction as Transaction

    suspend fun <T> transaction(block: suspend EntityManager.() -> T): T {
        return coroutineScope {
            async(Dispatchers.IO, start = CoroutineStart.UNDISPATCHED) {
                runBlocking {
                    val startThread = Thread.currentThread()
                    val result = if (transaction.isActive) {
                        entityManager.block()
                    } else {
                        try {
                            transaction.begin()
                            val result = entityManager.block()
                            if (transaction.isActive) {
                                transaction.commit()
                            }
                            result
                        } catch (e: Throwable) {
                            if (transaction.status.canRollback()) {
                                transaction.rollback()
                            }
                            throw e
                        }
                    }
                    if (startThread != Thread.currentThread())
                        error("Thread switching detected")
                    result
                }
            }.await()
        }
    }

}