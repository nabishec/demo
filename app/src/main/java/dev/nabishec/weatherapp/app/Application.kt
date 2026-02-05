package dev.nabishec.weatherapp.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Класс `Application` приложения.
 *
 * Аннотация `@HiltAndroidApp` запускает генерацию кода Hilt
 * и делает возможной DI (dependency injection) во всём приложении.
 */
@HiltAndroidApp
class Application: Application() {

}