package zinced.server.database

import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.Persistence
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

    inline fun <T> transaction(crossinline block: EntityManager.() -> T): T {
        return if (transaction.isActive) {
            entityManager.block()
        } else {
            try {
                transaction.begin()
                val result = entityManager.block()
                transaction.commit()
                result
            } finally {
                if (transaction.status.canRollback()) {
                    transaction.rollback()
                }
            }
        }
    }

}