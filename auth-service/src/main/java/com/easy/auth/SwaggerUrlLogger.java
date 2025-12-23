package com.easy.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 启动后输出 Swagger 地址，便于开发调试。
 */
@Component
public class SwaggerUrlLogger {
    private static final Logger log = LoggerFactory.getLogger(SwaggerUrlLogger.class);
    private final Environment environment;

    public SwaggerUrlLogger(Environment environment) {
        this.environment = environment;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void logSwaggerUrl() {
        String port = environment.getProperty("local.server.port", "8081");
        String contextPath = environment.getProperty("server.servlet.context-path", "");
        String basePath = contextPath == null ? "" : contextPath;
        String host = environment.getProperty("SWAGGER_HOST", "localhost");
        log.info("Swagger 地址: http://{}:{}{}/swagger-ui/index.html", host, port, basePath);
    }
}
