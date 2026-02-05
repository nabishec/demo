## WeatherApp

Небольшое Android‑приложение на Kotlin и Jetpack Compose, которое показывает текущую погоду по городу, используя OpenWeather API.  
В проекте используются Hilt (DI), Room (локальная база), Retrofit + kotlinx.serialization (сеть).

### Стек технологий

- **Язык**: Kotlin  
- **UI**: Jetpack Compose, Material 3  
- **DI**: Hilt  
- **Сеть**: Retrofit, OkHttp, kotlinx.serialization  
- **БД**: Room  

### Требования

- Android Studio **Hedgehog** / **Iguana** или новее  
- Установленный **Android SDK 21+**  
- Подключённый интернет для получения данных о погоде  

### Как открыть проект в Android Studio

1. **Запустите Android Studio**.  
2. На стартовом экране выберите пункт **“Open” / “Открыть”**.  
3. Укажите папку проекта:
   - `WeatherApp` (корневая директория, где лежит `build.gradle.kts`, `settings.gradle.kts` и папка `app`).  
4. Дождитесь, пока проект синхронизируется с Gradle (внизу IDE появится прогресс‑бар).  

### Настройка API‑ключа

Сейчас API‑ключ прописан в файле:

- `app/src/main/java/dev/nabishec/weatherapp/data/Constants.kt` — константа `API_KEY`.

Чтобы использовать свой ключ:

1. Получите ключ на сайте OpenWeather.  
2. Замените значение константы:

```kotlin
const val API_KEY = "ВАШ_КЛЮЧ_СЮДА"
```

Пересоберите приложение.

> Для продакшена лучше выносить ключ в `local.properties` или Gradle‑конфиги, а не держать его в коде.

### Как запустить приложение

1. Подключите **эмулятор** или **реальное устройство**:
   - Включите режим разработчика и USB‑отладку на телефоне, либо
   - Создайте AVD (Android Virtual Device) через **Device Manager** в Android Studio.
2. В верхней панели выберите конфигурацию запуска:
   - Убедитесь, что выбрано приложение `app`.  
3. Нажмите кнопку **Run ▶** (или `Shift + F10`).  
4. Дождитесь сборки и установки, приложение откроется на устройстве/эмуляторе.

### Как пользоваться приложением

1. При первом запуске приложение пытается загрузить погоду для города по умолчанию (`DEFAULT_CITY`, сейчас это **London**).  
2. Введите название города в текстовое поле и нажмите **Enter** или иконку поиска на клавиатуре:
   - Приложение сделает запрос в API;
   - Сохранит результат в локальную базу;
   - Обновит экран свежими данными.  
3. При плохом соединении:
   - Если в базе **нет** данных, появится Toast с сообщением о пустом кэше;
   - Если база **уже содержит** данные, будет показано сообщение, что отображаются закэшированные значения.

### Структура проекта (кратко)

- `app/src/main/java/dev/nabishec/weatherapp/app/Application.kt` — класс `Application` с Hilt.  
- `data/` — слой данных:
  - `service/WeatherApiService.kt` — интерфейс Retrofit для OpenWeather.
  - `database/` — Room: `AppDatabase`, `WeatherDao`, `WeatherEntity`.
  - `repository/Repository.kt` — репозиторий, объединяющий сеть и БД.
  - `di/Module.kt` — Hilt‑модуль, создающий все зависимости.
- `domain/WeatherResponse.kt` — доменные модели и мапперы `toEntity()/toResponse()`.  
- `presentation/` — слой UI:
  - `MainViewModel.kt` — бизнес‑логика и состояние экрана.
  - `MainScreen.kt` — основной экран на Compose.
  - `theme/` — файлы темы (цвета, типографика, MaterialTheme).  
- `MainActivity.kt` — точка входа, где подключается Compose и `MainScreen`.

