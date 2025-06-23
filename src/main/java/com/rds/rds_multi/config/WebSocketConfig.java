package com.rds.rds_multi.config;

import com.rds.rds_multi.controller.ScreenSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ScreenSocketHandler screenSocketHandler;

    public WebSocketConfig(ScreenSocketHandler screenSocketHandler) {
        this.screenSocketHandler = screenSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(screenSocketHandler, "/ws/screen")
                .setAllowedOrigins("*");
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(5 * 1024 * 1024);  // 1 MB * 5 
        container.setMaxBinaryMessageBufferSize(5 * 1024 * 1024);
        return container;
    }
}
