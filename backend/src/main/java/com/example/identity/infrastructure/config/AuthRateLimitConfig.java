package com.example.identity.infrastructure.config;

import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.jdbc.PrimaryKeyMapper;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.postgresql.Bucket4jPostgreSQL;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthRateLimitConfig {

    @Bean
    public ProxyManager<String> authRateLimitProxyManager(DataSource dataSource) {
        return Bucket4jPostgreSQL.selectForUpdateBasedBuilder(dataSource)
                .table("auth_rate_limit_bucket")
                .stateColumn("state")
                .expiresAtColumn("expires_at")
                .primaryKeyMapper(PrimaryKeyMapper.STRING)
                .expirationAfterWrite(ExpirationAfterWriteStrategy.fixedTimeToLive(java.time.Duration.ofHours(24)))
                .build();
    }
}
