package com.easy.gateway.filter;

import com.easy.common.ApiResponse;
import com.easy.common.ErrorCode;
import com.easy.gateway.config.GatewayAuthProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 网关鉴权过滤器：校验 JWT 并调用权限服务进行接口级校验。
 */
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(AuthGlobalFilter.class);
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private final SecretKey secretKey;
    private final WebClient.Builder webClientBuilder;
    private final GatewayAuthProperties properties;
    private final ReactiveStringRedisTemplate redisTemplate;

    public AuthGlobalFilter(WebClient.Builder webClientBuilder,
                            GatewayAuthProperties properties,
                            ReactiveStringRedisTemplate redisTemplate) {
        this.webClientBuilder = webClientBuilder;
        this.properties = properties;
        this.redisTemplate = redisTemplate;
        this.secretKey = Keys.hmacShaKeyFor(properties.getJwtSecret().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return writeError(exchange, ErrorCode.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);
        Claims claims;
        try {
            claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (ExpiredJwtException ex) {
            return writeError(exchange, ErrorCode.TOKEN_EXPIRED);
        } catch (JwtException ex) {
            return writeError(exchange, ErrorCode.TOKEN_INVALID);
        }

        if (!"access".equals(claims.get("type"))) {
            return writeError(exchange, ErrorCode.TOKEN_INVALID);
        }

        String jti = claims.getId();
        Long userId = Long.parseLong(claims.getSubject());
        String service = resolveService(exchange);
        String method = exchange.getRequest().getMethod() != null
            ? exchange.getRequest().getMethod().name()
            : "UNKNOWN";

        return redisTemplate.hasKey(accessBlacklistKey(jti))
            .flatMap(blacklisted -> {
                if (Boolean.TRUE.equals(blacklisted)) {
                    return writeError(exchange, ErrorCode.TOKEN_INVALID);
                }
                return webClientBuilder.baseUrl(properties.getPermissionServiceUrl())
                    .build()
                    .post()
                    .uri("/internal/permissions/check")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                        "userId", userId,
                        "service", service,
                        "method", method,
                        "path", path
                    ))
                    .retrieve()
                    .bodyToMono(PermissionResponse.class)
                    .flatMap(response -> {
                        if (response != null && Boolean.TRUE.equals(response.getData())) {
                            return chain.filter(exchange);
                        }
                        return writeError(exchange, ErrorCode.FORBIDDEN, HttpStatus.FORBIDDEN);
                    });
            })
            .onErrorResume(ex -> {
                log.warn("权限服务调用失败: {}", ex.getMessage());
                return writeError(exchange, ErrorCode.INTERNAL_ERROR, HttpStatus.SERVICE_UNAVAILABLE);
            });
    }

    private boolean isPublicPath(String path) {
        for (String pattern : properties.getWhitelistPaths()) {
            if (matches(pattern, path)) {
                return true;
            }
        }
        return false;
    }

    private boolean matches(String pattern, String path) {
        // 使用 Ant 风格匹配，支持 /**/api/auth/** 这类通配
        return PATH_MATCHER.match(pattern, path);
    }

    private String resolveService(ServerWebExchange exchange) {
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        if (route == null) {
            return "unknown-service";
        }
        if (route.getUri() != null && route.getUri().getHost() != null && !route.getUri().getHost().isBlank()) {
            return route.getUri().getHost();
        }
        String routeId = route.getId();
        if (routeId == null || routeId.isBlank()) {
            return "unknown-service";
        }
        // 兼容 discovery locator 生成的路由 ID，例如 ReactiveCompositeDiscoveryClient_permission-service
        int splitIndex = routeId.lastIndexOf('_');
        if (splitIndex >= 0 && splitIndex < routeId.length() - 1) {
            return routeId.substring(splitIndex + 1);
        }
        return routeId;
    }

    private Mono<Void> writeError(ServerWebExchange exchange, ErrorCode errorCode) {
        return writeError(exchange, errorCode, HttpStatus.UNAUTHORIZED);
    }

    private Mono<Void> writeError(ServerWebExchange exchange, ErrorCode errorCode, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        ApiResponse<Void> body = ApiResponse.fail(errorCode);
        byte[] bytes = ("{\"code\":" + body.getCode() + ",\"message\":\"" + body.getMessage() + "\",\"data\":null}")
            .getBytes(StandardCharsets.UTF_8);
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
    }

    @Override
    public int getOrder() {
        return -10;
    }

    private String accessBlacklistKey(String jti) {
        return "auth:access:blacklist:" + jti;
    }

    private static class PermissionResponse {
        private Integer code;
        private String message;
        private Boolean data;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Boolean getData() {
            return data;
        }

        public void setData(Boolean data) {
            this.data = data;
        }
    }
}
