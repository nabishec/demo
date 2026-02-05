package dev.nabishec.weatherapp.data.service

import dev.nabishec.weatherapp.data.API_KEY
import dev.nabishec.weatherapp.domain.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit‑интерфейс для обращения к API погоды.
 *
 * Описывает HTTP‑запросы и их параметры.
 */
interface WeatherApiService {
    @GET("weather")
    /**
     * Запрашивает текущую погоду для указанного города.
     *
     * @param cityName название города.
     * @param apiKey   API‑ключ OpenWeather (по умолчанию берётся из `API_KEY`).
     * @param language язык локализации ответа (по умолчанию русский "ru").
     *
     * @return объект `WeatherResponse` с данными о погоде.
     */
    suspend fun getWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String = API_KEY,
        @Query("lang") language: String = "ru",
    ): WeatherResponse
}