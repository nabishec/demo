package dev.nabishec.weatherapp.data

/**
 * API‑ключ для доступа к OpenWeather.
 *
 * В реальном приложении такие ключи лучше хранить в
 * gradle‑конфигах или в `local.properties`, а не в коде.
 */
const val API_KEY = "c85ccc51d10d126f51b6d416b3f43379"

/**
 * Базовый URL для всех запросов к OpenWeather API.
 */
const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

/**
 * Город по умолчанию, который используется при первой загрузке приложения.
 */
const val DEFAULT_CITY = "London"