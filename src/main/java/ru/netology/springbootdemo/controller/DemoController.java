package ru.netology.springbootdemo.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Методы, возвращающие разные коды ответа (200, 404, 500), и метод с кастомной метрикой.
 * Каждый вызов увеличивает счётчик controller.calls с меткой method (в Prometheus: controller_calls_total).
 */
@RestController
@RequestMapping("/api")
public class DemoController {

    private final MeterRegistry registry;

    public DemoController(MeterRegistry registry) {
        this.registry = registry;
    }

    @GetMapping("/ok")
    public ResponseEntity<String> ok() {
        count("ok");
        return ResponseEntity.ok("200: всё хорошо");
    }

    @GetMapping("/not-found")
    public ResponseEntity<String> notFound() {
        count("not-found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("404: ничего не найдено");
    }

    @GetMapping("/error")
    public ResponseEntity<String> error() {
        count("error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("500: внутренняя ошибка сервера");
    }

    // Метод с кастомной метрикой: считает вызов и возвращает общее число вызовов всех методов контроллера
    @GetMapping("/stats")
    public Map<String, Object> stats() {
        count("stats");
        double total = registry.find("controller.calls").counters().stream()
                .mapToDouble(Counter::count)
                .sum();
        return Map.of("totalControllerCalls", (long) total);
    }

    private void count(String method) {
        registry.counter("controller.calls", "method", method).increment();
    }
}
