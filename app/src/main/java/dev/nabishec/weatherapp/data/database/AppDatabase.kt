package dev.nabishec.weatherapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Главная база данных приложения.
 *
 * Здесь регистрируются все сущности Room и версии схемы.
 */
@Database(
    version = 4,
    entities = [
        WeatherEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Возвращает DAO для работы с таблицей погоды.
     */
    abstract fun getWeatherDao(): WeatherDao
}