# Мониторинг Spring Boot: Actuator + Prometheus + Grafana

## Запуск (одна команда)

```
docker compose up -d --build
```


| Описание               | Адрес |
|------------------------|---|
| Приложение             | http://localhost:8080/api/ok |
| Метрики для Prometheus | http://localhost:8080/actuator/prometheus |
| Prometheus             | http://localhost:9090 (алерты: /alerts) |
| Grafana                | http://localhost:3000 (admin / admin) |

## Что делает приложение
- `MemoryConsumer` каждые 2 секунды занимает ещё 10 МБ кучи до 300 МБ (JVM запущена с `-Xmx512m`).
- `DemoController`: `/api/ok` (200), `/api/not-found` (404), `/api/error` (500), `/api/stats` (кастомная метрика).
- Кастомные метрики: `controller_calls_total{method=...}`, `app_memory_allocated_bytes`.
- Сервис `load-generator` сам вызывает методы, поэтому графики заполняются без ручных действий.

## Алерт
`HighHeapMemoryUsage` (prometheus/alert.rules.yml): куча больше 250 МБ дольше 15 секунд.
Срабатывает примерно через минуту после старта; смотреть: http://localhost:9090/alerts.

## Остановка
`docker compose down`
