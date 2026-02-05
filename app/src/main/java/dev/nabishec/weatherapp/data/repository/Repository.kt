package dev.nabishec.weatherapp.data.repository

import dev.nabishec.weatherapp.data.database.WeatherDao
import dev.nabishec.weatherapp.data.database.WeatherEntity
import dev.nabishec.weatherapp.data.service.WeatherApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Репозиторий инкапсулирует логику получения и сохранения данных о погоде.
 *
 * - Берёт данные из сетевого слоя (`WeatherApiService`);
 * - Сохраняет и читает данные через `WeatherDao`;
 * - Перекладывает работу в `Dispatchers.IO`, чтобы не блокировать главный поток.
 */
class Repository @Inject constructor(
    private val service: WeatherApiService,
    private val dao: WeatherDao,
) {
    /**
     * Запрашивает погоду из сети по названию города.
     *
     * @param cityName название города.
     */
    suspend fun getWeatherFromApi(cityName: String) = withContext(Dispatchers.IO) {
        service.getWeather(cityName = cityName)
    }

    /**
     * Получает последнюю сохранённую запись о погоде из локальной базы.
     */
    suspend fun getWeatherFromCache() = withContext(Dispatchers.IO) {
        dao.getLastWeather()
    }

    /**
     * Сохраняет запись о погоде в локальную базу.
     *
     * @param weather сущность `WeatherEntity`, которая будет вставлена или обновлена.
     */
    suspend fun addWeather(weather: WeatherEntity) = withContext(Dispatchers.IO) {
        dao.addWeather(weather)
    }
}