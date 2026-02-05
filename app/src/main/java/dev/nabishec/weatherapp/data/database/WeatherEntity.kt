package dev.nabishec.weatherapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.nabishec.weatherapp.domain.WeatherResponse
import dev.nabishec.weatherapp.domain.Main
import dev.nabishec.weatherapp.domain.Weather
import dev.nabishec.weatherapp.domain.Wind

/**
 * Сущность Room, описывающая строку в таблице `weather`.
 *
 * Хранит последнюю полученную информацию о погоде.
 */
@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val temperature: Double,
    val pressure: Int,
    val humidity: Int,
    val tempMin: Double,
    val tempMax: Double,
    val speed: Double,
    val weather: String,
    val weatherDescription: String
)

/**
 * Преобразует сущность `WeatherEntity` обратно в доменную модель `WeatherResponse`,
 * чтобы отобразить кэшированные данные в UI так же, как и данные из сети.
 */
fun WeatherEntity.toResponse() = WeatherResponse(
    name = name,
    main = Main(
        temperature = temperature,
        pressure = pressure,
        humidity = humidity,
        tempMin = tempMin,
        tempMax = tempMax
    ),
    wind = Wind(
        speed = speed
    ),
    weather = listOf(Weather(
        main = weather,
        description = weatherDescription
    ))
)