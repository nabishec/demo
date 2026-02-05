package dev.nabishec.weatherapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nabishec.weatherapp.data.DEFAULT_CITY
import dev.nabishec.weatherapp.data.repository.Repository
import dev.nabishec.weatherapp.data.database.toResponse
import dev.nabishec.weatherapp.domain.WeatherResponse
import dev.nabishec.weatherapp.domain.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Главная `ViewModel` приложения.
 *
 * Хранит состояние экрана, обращается к репозиторию за данными
 * и отправляет одноразовые события (например, показ Toast).
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    /**
        * Текущее состояние экрана.
        *
        * - `searchString` — введённый пользователем текст города;
        * - `weather` — текущие данные о погоде (могут быть `null`, пока загрузка не завершена).
        */
    data class State(
        var searchString: String = "",
        val weather: WeatherResponse? = null
    )

    /**
        * Одноразовые события для UI.
        *
        * Используются для отображения Toast‑ов при разных ситуациях.
        */
    sealed interface Event {
        data object ShowToastOnEmptyDatabase : Event
        data object ShowToastOnFilledDatabase : Event
        data object ShowToastOnErrorSearch : Event
    }

    // Внутренний изменяемый StateFlow с состоянием экрана.
    private val _state = MutableStateFlow(State())
    // Публичный неизменяемый StateFlow, к которому подписывается UI (Compose).
    val state: StateFlow<State> = _state.asStateFlow()

    // Внутренний SharedFlow для одноразовых событий.
    private val _event = MutableSharedFlow<Event>()
    // Публичный поток событий, на который подписывается UI.
    val event: SharedFlow<Event> = _event.asSharedFlow()

    init {
        // При создании ViewModel сразу загружаем погоду
        // для города по умолчанию или из кэша.
        loadWeather()
    }


    /**
     * Обновляет текст поискового запроса города при вводе пользователем.
     *
     * @param query новый текст из текстового поля.
     */
    fun onQueryChanged(query: String) {
        _state.update { it.copy(searchString = query) }
    }

    /**
     * Выполняет поиск погоды по введённому городу.
     *
     * - Делает запрос в API через репозиторий;
     * - Обновляет состояние экрана новыми данными;
     * - Сохраняет результат в локальную базу;
     * - В случае ошибки отправляет событие для показа Toast.
     */
    fun search() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val weather = repository.getWeatherFromApi(_state.value.searchString)
                _state.update { it.copy(weather = weather) }
                repository.addWeather(weather.toEntity())
            } catch (e: Exception){
                _event.emit(Event.ShowToastOnErrorSearch)
            }
        }
    }

    /**
     * Загружает стартовые данные о погоде.
     *
     * Сначала пытается получить погоду по городу по умолчанию из сети.
     * При неудаче берёт последние сохранённые данные из базы
     * и показывает соответствующий Toast (пустая или заполненная база).
     */
    private fun loadWeather() = viewModelScope.launch {
        try {
            val weather = repository.getWeatherFromApi(DEFAULT_CITY)
            repository.addWeather(weather.toEntity())

            _state.update { it.copy(weather = weather) }
        } catch (e: Exception) {
            val weatherFromCache = repository.getWeatherFromCache()

            if (weatherFromCache == null) _event.emit(Event.ShowToastOnEmptyDatabase)
            else _event.emit(Event.ShowToastOnFilledDatabase)

            _state.update { it.copy(weather = weatherFromCache?.toResponse()) }
        }
    }

}