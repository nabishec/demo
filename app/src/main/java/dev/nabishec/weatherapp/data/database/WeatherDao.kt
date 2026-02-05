package dev.nabishec.weatherapp.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

/**
 * DAO (Data Access Object) для работы с таблицей `weather`.
 *
 * Содержит запросы к базе данных Room.
 */
@Dao
interface WeatherDao {

    /**
     * Возвращает последнюю (самую новую) запись о погоде из базы.
     */
    @Query(
        """
        SELECT * FROM weather
        ORDER BY id DESC
        LIMIT 1
    """
    )
    suspend fun getLastWeather(): WeatherEntity?

    /**
     * Добавляет новую запись о погоде или обновляет существующую,
     * если запись с тем же первичным ключом уже есть.
     */
    @Upsert
    suspend fun addWeather(weather: WeatherEntity)
}