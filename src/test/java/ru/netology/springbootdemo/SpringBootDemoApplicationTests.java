package ru.netology.springbootdemo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Интеграционный тест: запускает два готовых Docker-образа и проверяет, что они отвечают по-разному.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringBootDemoApplicationTests {

    // dev-образ: внутри контейнера приложение слушает порт 8080
    private static final GenericContainer<?> devApp = new GenericContainer<>("devapp")
            .withExposedPorts(8080)
            .waitingFor(Wait.forHttp("/profile").forStatusCode(200));

    // prod-образ: внутри контейнера приложение слушает порт 8081
    private static final GenericContainer<?> prodApp = new GenericContainer<>("prodapp")
            .withExposedPorts(8081)
            .waitingFor(Wait.forHttp("/profile").forStatusCode(200));

    @Autowired
    TestRestTemplate restTemplate;

    @BeforeAll
    public static void setUp() {
        devApp.start();
        prodApp.start();
    }

    @AfterAll
    public static void tearDown() {
        devApp.stop();
        prodApp.stop();
    }

    @Test
    void devAppReturnsDevProfile() {
        // getMappedPort: на каком порту ПК Docker открыл порт 8080 контейнера (он выбирается случайно)
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://" + devApp.getHost() + ":" + devApp.getMappedPort(8080) + "/profile", String.class);
        System.out.println(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Current profile is dev", response.getBody());
    }

    @Test
    void prodAppReturnsProductionProfile() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://" + prodApp.getHost() + ":" + prodApp.getMappedPort(8081) + "/profile", String.class);
        System.out.println(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Current profile is production", response.getBody());
    }
}
