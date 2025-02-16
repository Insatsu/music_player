# Мобильный аудиоплеер скачанной музыки и из api
Мобильное приложение для поиска и воспроизведения музыки как скачанной на само устройство, так и полученной из внешнего api (Deezer).

### Запуск приложения
1. Клонировать репозиторий (с помощью git командой git clone <ссылка данного репозитория> или непосредственно скачав репозиторий с сайта)
2. Запустить проект в Android Studio или другой среде программирования, поддерживающей android приложения
3. После запуска приложения на мобильном устройстве/эмуляторе предоставить необходимые разрешения (чтение файлов на устройстве и показ уведомлений), согласившись с появившимися запросами
4. Пользоваться

### Технологии
Kotlin, Kotlin coroutines, Dagger2, Retrofit2, Coil


## Функционал
1. Просмотр локальной музыки, расположенной в папке Download
2. Просмотр музыки из api (Deezer)
3. Поиск по каждому из списков музыки (по отдельности)
4. Воспроизведение музыки, её управление (следующий/предыдущий трек, пауза/стоп, перемотка)
5. Управление воспроизведением через уведомление


## Немного об изюминках
- Как уточнил ранее, в качестве источника для скачанных песен используется папка Download
- В силу реализации, при переходе на экран воспроизведения с экрана онлайн музыки, данные последней берутся не из дополнительного запроса к api, а из ранее подгруженного списка
- В качестве обложки по умолчанию для песен, которые не имеют таковой, использовал блестяшки-звездочки 

1. Есть разные недочёты, исправить которые не успел, однако весь поставленный функционал приложение выполняет 
2. На экране с музыкой из api отсутствует индикатор загрузки, так что пока музыка не подгрузилась - пустой список
3. Интерфейс может показывать прошлое состояние приложения (список по поиску из api) или актуализироваться после первого взаимодействия (полоса перемотки) 
4. Покуда на просторах интернета увидел использование mediaSession только с андроид 10, также добавил это условие себе, однако доказательств этого не нашел, а сам проверить не могу, ибо нет смартфона с этой версией, так что перестраховался 
5. Много чего использовал для себя нового, поэтому не везде использован оптимальный подход

## Итого
Довольно интересно получилось, многое испробовал для достижения цели, что мне нравится.  