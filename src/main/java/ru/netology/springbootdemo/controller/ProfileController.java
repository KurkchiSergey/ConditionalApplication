package ru.netology.springbootdemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.springbootdemo.profile.SystemProfile;

import static net.logstash.logback.argument.StructuredArguments.value;

@RestController
@RequestMapping("/")
public class ProfileController {

    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);

    private SystemProfile profile;

    public ProfileController(SystemProfile profile) {
        this.profile = profile;
    }

    @GetMapping("profile")
    public String getProfile() {
        String result = profile.getProfile();
        // value("profile", result): в тексте сообщения останется само значение,
        // а в JSON-логе появится отдельное поле "profile" (его можно будет выбрать колонкой в Kibana).
        log.info("Запрос GET /profile, ответ: {}", value("profile", result));
        return result;
    }

    @GetMapping("error-test")
    public String errorTest() {
        try {
            throw new RuntimeException("Test error");
        } catch (Exception e) {
            log.error("Что-то пошло не так", e);

        }
        return "Ошибка записана в лог";
    }
}
