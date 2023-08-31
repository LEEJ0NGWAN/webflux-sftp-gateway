package com.example.gateway.h2.config;

import java.sql.SQLException;

import org.h2.tools.Server;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.gateway.h2.properties.H2Properties;

import lombok.RequiredArgsConstructor;

@Component
@ConditionalOnProperty(prefix = "spring.h2.console", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
public class H2ConsoleConfig {

    private Server server;
    private final H2Properties properties;

    @EventListener
    public void start(ContextRefreshedEvent event) throws SQLException {

        server = Server
        .createWebServer("-webPort", properties.getPort(), "-tcpAllowOthers")
        .start();
    }

    @EventListener
    public void stop(ContextClosedEvent event) {

        server.stop();
    }
}
