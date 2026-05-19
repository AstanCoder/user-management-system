package com.example.infrastructure.seed;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("seed")
public class DemoDataSeederRunner implements ApplicationRunner {

    private final DemoDataSeeder seeder;
    private final DemoSeedProperties properties;
    private final ApplicationContext applicationContext;

    public DemoDataSeederRunner(
            DemoDataSeeder seeder,
            DemoSeedProperties properties,
            ApplicationContext applicationContext) {
        this.seeder = seeder;
        this.properties = properties;
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!properties.isEnabled()) {
            return;
        }
        seeder.seed();
        if (properties.isExitAfterRun()) {
            int exitCode = SpringApplication.exit(applicationContext, () -> 0);
            System.exit(exitCode);
        }
    }
}
