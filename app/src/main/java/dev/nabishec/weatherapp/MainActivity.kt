package dev.nabishec.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import dev.nabishec.weatherapp.presentation.theme.WeatherAppTheme
import dev.nabishec.weatherapp.presentation.MainScreen

/**
 * Главная `Activity` приложения.
 *
 * Отвечает за инициализацию Compose‑UI и подключение Hilt
 * для внедрения зависимостей в навигационный граф и ViewModel.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Включаем режим "edge to edge", чтобы контент мог
        // занимать весь экран, включая системные полосы.
        enableEdgeToEdge()

        // Запускаем Compose и отображаем основной экран приложения.
        setContent {
            WeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Передаём отступы от `Scaffold` во `MainScreen`,
                    // чтобы контент не перекрывался системными элементами.
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}