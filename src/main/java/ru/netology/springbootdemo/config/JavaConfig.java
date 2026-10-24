package ru.netology.springbootdemo.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.netology.springbootdemo.profile.DevProfile;
import ru.netology.springbootdemo.profile.ProductionProfile;
import ru.netology.springbootdemo.profile.SystemProfile;

@Configuration
public class JavaConfig {

    // Возвращается, если в application.properties: netology.profile.dev=true
    @Bean
    @ConditionalOnProperty(prefix = "netology.profile", name = "dev", havingValue = "true")
    public SystemProfile devProfile() {
        return new DevProfile();
    }

    // Возвращается , если netology.profile.dev=false
    @Bean
    @ConditionalOnProperty(prefix = "netology.profile", name = "dev", havingValue = "false")
    public SystemProfile prodProfile() {
        return new ProductionProfile();
    }
}
