package zinced.server.database.util

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import kotlinx.datetime.Instant
import java.sql.Timestamp

@Converter(autoApply = true)
object KtxInstantConverter : AttributeConverter<Instant, Timestamp> {

    override fun convertToDatabaseColumn(attribute: Instant) = Timestamp(attribute.toEpochMilliseconds())

    override fun convertToEntityAttribute(dbData: Timestamp) = Instant.fromEpochMilliseconds(dbData.time)

}