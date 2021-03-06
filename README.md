# Описание задачи
Реализовать систему ввода и отображения информации о рецептах поликлиники, включающую следующие сущности и их атрибуты:
* Пациент 
	* Имя
	* Фамилия 
	* Отчество 
	* Телефон
* Врач
	* Имя
	* Фамилия 
	* Отчество 
	* Специализация
* Рецепт
	* Описание
	* Пациент
	* Врач
	* Дата создания 
	* Срок действия 
	* Приоритет

Рецепт может иметь один из приоритетов: Нормальный, Cito (Срочный), Statim (Немедленный).

# Используемые технологии:
- [x] Java SE 8
- [x] Пользовательский интерфейс на [Vaadin 8](https://vaadin.com)
- [x] Доступ к данным через JDBC
- [x] Сервер баз данных: HSQLDB в [in-process режиме](http://hsqldb.org/doc/2.0/guide/running-chapt.html#rgc_inprocess)

Видео обзор функциональных возможностей страницы пациентов:
-------------
[![Watch the video](https://user-images.githubusercontent.com/16539012/69039826-9b3ed080-09e4-11ea-8c5f-c65631b75149.png)](https://youtu.be/sCOaNgsMaI0)

Описание возможностей:
-------------
- [x] Отображение списка пациентов.
- [x] Добавление/редактирование (посредством вызова модального окна), удаление пациента.
- [x] Валидация полей (проверка на непустоту + регулярное выражение для проверки корректности введенного телефонного номера).

Видео обзор функциональных возможностей страницы докторов:
-------------
[![Watch the video](https://user-images.githubusercontent.com/16539012/69040556-6e4fe500-0a07-11ea-96fc-bf18f18c617c.png)](https://youtu.be/TuNHoa5P4b4)

Описание возможностей:
-------------
- [x] Отображение списка докторов.
- [x] Добавление/редактирование (посредством вызова модального окна), удаление доктора.
- [x] Просмотр статистики докторов (посредством вызова модального окна).
- [x] Валидация полей (проверка на непустоту).

Видео обзор функциональных возможностей страницы рецептов:
-------------
[![Watch the video](https://user-images.githubusercontent.com/16539012/69040777-cf77b880-0a07-11ea-8ad7-a94c81a66365.png)](https://youtu.be/bqQ_1LzSGZY)

Описание возможностей:
-------------
- [x] Отображение списка рецептов.
- [x] Добавление/редактирование (посредством вызова модального окна), удаление рецепта.
- [x] Фильтрация по трем параметрам: пациент, описание, приоритет. Результат отображается сразу после ввода символа в любое из полей.
- [x] Валидация полей (проверка на непустоту).

Требования
-------------

* [Java Development Kit (JDK) 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Maven 3](https://maven.apache.org/download.cgi)

Сборка и запуск
-------------

1. Запустите следующие команды:
	```
	mvn package
	mvn jetty:run
	```

2. Перейдите по ссылке: `http://localhost:8080`в браузере.
