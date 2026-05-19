package com.example.infrastructure.seed;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.context.ApplicationContext;

@ExtendWith(MockitoExtension.class)
class DemoDataSeederRunnerTest {

    @Mock
    private DemoDataSeeder seeder;

    @Mock
    private DemoSeedProperties properties;

    @Mock
    private ApplicationContext applicationContext;

    @InjectMocks
    private DemoDataSeederRunner runner;

    @Test
    void run_skipsWhenDisabled() throws Exception {
        when(properties.isEnabled()).thenReturn(false);

        runner.run(new DefaultApplicationArguments(new String[0]));

        verify(seeder, never()).seed();
    }

    @Test
    void run_executesSeederWhenEnabled() throws Exception {
        when(properties.isEnabled()).thenReturn(true);
        when(properties.isExitAfterRun()).thenReturn(false);

        runner.run(new DefaultApplicationArguments(new String[0]));

        verify(seeder).seed();
    }
}
