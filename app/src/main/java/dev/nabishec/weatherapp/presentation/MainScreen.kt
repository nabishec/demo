package dev.nabishec.weatherapp.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.nabishec.weatherapp.R

/**
 * Основной экран приложения.
 *
 * Содержит:
 * - текстовое поле для ввода города;
 * - иконку погоды в зависимости от состояния;
 * - текстовые поля с подробной информацией о погоде;
 * - обработку событий `MainViewModel` (показ Toast при ошибках).
 *
 * @param modifier внешний модификатор, который позволяет передавать отступы и размер от родительского контейнера.
 */
@Composable
fun MainScreen(modifier: Modifier) {

    // Получаем ViewModel через Hilt и подписываемся на состояние.
    val viewModel = hiltViewModel<MainViewModel>()
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val context = LocalContext.current
    val badConnectionEmptyCache = stringResource(R.string.bad_connection_empty_cache)
    val badConnectionFilledCache = stringResource(R.string.bad_connection_filled_cache)
    val errorSearchString = stringResource(R.string.error_search)
    val clearIcon = painterResource(R.drawable.clear_day_24dp_e3e3e3_fill0_wght400_grad0_opsz24)
    val rainIcon = painterResource(R.drawable.rainy_24dp_e3e3e3_fill0_wght400_grad0_opsz24)
    val cloudIcon = painterResource(R.drawable.cloud_24dp_e3e3e3_fill0_wght400_grad0_opsz24)
    val snowIcon = painterResource(R.drawable.weather_snowy_24dp_e3e3e3_fill0_wght400_grad0_opsz24)

    // Отслеживаем поток одноразовых событий и показываем Toast‑ы.
    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is MainViewModel.Event.ShowToastOnEmptyDatabase -> Toast.makeText(
                    context,
                    badConnectionEmptyCache,
                    Toast.LENGTH_SHORT
                ).show()

                is MainViewModel.Event.ShowToastOnFilledDatabase -> Toast.makeText(
                    context,
                    badConnectionFilledCache,
                    Toast.LENGTH_SHORT
                ).show()

                is MainViewModel.Event.ShowToastOnErrorSearch -> Toast.makeText(
                    context,
                    errorSearchString,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize(),
    ) {
        // Поле ввода названия города.
        OutlinedTextField(
            value = state.searchString,
            onValueChange = { viewModel.onQueryChanged(it) },
            placeholder = { Text(stringResource(R.string.search_weather_by_city)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { viewModel.search() })
        )

        // Иконка погоды в зависимости от текущего состояния.
        Icon(
            painter = when (state.weather?.weather[0]?.main) {
                "Snow" -> snowIcon
                "Rain" -> rainIcon
                "Clouds" -> cloudIcon
                else -> clearIcon
            },
            contentDescription = null
        )

        Text(text = "Город - ${state.weather?.name}")
        Text(text = "Температура - ${state.weather?.main?.temperature}")
        Text(text = "Влажность - ${state.weather?.main?.humidity}")
        Text(text = "Давление - ${state.weather?.main?.pressure}")
        Text(text = "Максимальная температура - ${state.weather?.main?.tempMax}")
        Text(text = "Минимальная температура - ${state.weather?.main?.tempMin}")
        Text(text = "Скорость ветра - ${state.weather?.wind?.speed}")
        Text(text = "Погода - ${state.weather?.weather[0]?.description}")
    }
}