package dev.nabishec.weatherapp.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.nabishec.weatherapp.data.BASE_URL
import dev.nabishec.weatherapp.data.database.AppDatabase
import dev.nabishec.weatherapp.data.service.WeatherApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

/**
 * Hilt‑модуль, который описывает, как создавать и предоставлять зависимости.
 *
 * Здесь настраиваются:
 * - база данных Room;
 * - DAO;
 * - JSON‑парсер;
 * - HTTP‑клиент OkHttp с логированием;
 * - Retrofit и сервис для работы с API погоды.
 */
@Module
@InstallIn(SingletonComponent::class)
class Module {

    /**
     * Создаёт и предоставляет экземпляр базы данных Room.
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext application: Context) = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "database.db"
    )
        .fallbackToDestructiveMigration()
        .build()

    /**
     * Предоставляет DAO для работы с таблицей `weather`.
     */
    @Provides
    fun provideWeatherDao(db: AppDatabase) = db.getWeatherDao()

    /**
     * Настраивает JSON‑парсер `kotlinx.serialization`.
     *
     * `ignoreUnknownKeys = true` позволяет игнорировать лишние поля в ответе сервера.
     */
    @Provides
    fun provideJson() = Json {
        ignoreUnknownKeys = true
    }

    /**
     * Создаёт HTTP‑перехватчик, логирующий запросы и ответы.
     */
    @Provides
    fun provideInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /**
     * Создаёт `OkHttpClient` и подключает к нему перехватчик логирования.
     */
    @Provides
    @Singleton
    fun provideClient(interceptor: HttpLoggingInterceptor) = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    /**
     * Создаёт и настраивает экземпляр `Retrofit` для работы с API.
     */
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, json: Json): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    /**
     * Создаёт реализацию интерфейса `WeatherApiService` через `Retrofit`.
     */
    @Provides
    @Singleton
    fun provideService(retrofit: Retrofit) = retrofit.create(WeatherApiService::class.java)

}