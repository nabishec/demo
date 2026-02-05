package dev.nabishec.weatherapp.domain

import dev.nabishec.weatherapp.data.database.WeatherEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Основной ответ от сервера погоды.
 *
 * Используется для парсинга JSON‑ответа от OpenWeather
 * и дальнейшего преобразования в сущность базы данных.
 */
@Serializable
data class WeatherResponse(
    val name: String,
    val main: Main,
    val wind: Wind,
    val weather: List<Weather>,
)

/**
 * Описание погодного состояния (например, "Rain", "Clouds").
 */
@Serializable
data class Weather(
    val main: String,
    val description: String = ""
)

/**
 * Основные параметры погоды (температура, давление и т.д.).
 */
@Serializable
data class Main(
    @SerialName("temp")
    val temperature: Double,
    val pressure: Int,
    val humidity: Int,
    @SerialName("temp_min")
    val tempMin: Double,
    @SerialName("temp_max")
    val tempMax: Double,
)

/**
 * Информация о ветре.
 */
@Serializable
data class Wind(
    val speed: Double
)

/**
 * Преобразует сетевую модель `WeatherResponse` в сущность базы данных `WeatherEntity`,
 * чтобы сохранить последние данные о погоде локально через Room.
 */
fun WeatherResponse.toEntity() = WeatherEntity(
    name = name,
    temperature = main.temperature,
    pressure = main.pressure,
    humidity = main.humidity,
    tempMin = main.tempMin,
    tempMax = main.tempMax,
    speed = wind.speed,
    weather = weather[0].main,
    weatherDescription = weather[0].description
)