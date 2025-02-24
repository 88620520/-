package com.example.exam.config;

import org.springframework.boot.web.server.Compression;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

@Configuration
public class GzipConfig {

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerFactoryCustomizer() {
        return factory -> {
            Compression compression = new Compression();
            compression.setEnabled(true);
            compression.setMinResponseSize(DataSize.ofKilobytes(2)); // 2KB以上的响应进行压缩
            compression.setMimeTypes(new String[]{
                "text/html", 
                "text/xml",
                "text/plain",
                "text/css",
                "text/javascript",
                "application/javascript",
                "application/json",
                "application/xml"
            });
            factory.setCompression(compression);
        };
    }
} 