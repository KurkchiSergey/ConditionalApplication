package ru.netology.springbootdemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

// Включает работу аннотации @Scheduled
@Configuration
@EnableScheduling
public class SchedulingConfig {
}
