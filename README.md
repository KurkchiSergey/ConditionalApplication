# ConditionalApplication + ELK

Приложение пишет JSON-логи в `logs/app.log`. Filebeat читает файл и отправляет строки в Logstash,
Logstash разбирает JSON и кладёт события в Elasticsearch (индексы `app-logs-ГГГГ.ММ.ДД`), Kibana их показывает.

```
приложение -> logs/app.log -> Filebeat -> Logstash -> Elasticsearch -> Kibana
```
## Запуск

1. Запустить `SpringBootDemoApplication` в IDE (рабочая папка — корень проекта, чтобы появилась папка `logs/`).
2. Открыть http://localhost:8080/profile несколько раз (каждый запрос пишет строку в лог).
3. Проверить, что файл `logs/app.log` появился и в нём есть JSON-строки.
4. `docker compose up -d`,ждем несколько секунд.
5. Kibana: http://localhost:5601
   - Stack Management → Data Views → Create data view: name `app-logs`, index pattern `app-logs-*`, timestamp field `@timestamp`.
   - Discover → выбрать data view `app-logs`, сортировка по `@timestamp` (по убыванию),
     добавить колонки `level`, `logger_name`, `message`, `profile`.




