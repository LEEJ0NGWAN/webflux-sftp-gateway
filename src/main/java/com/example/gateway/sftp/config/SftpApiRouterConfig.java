package com.example.gateway.sftp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.gateway.sftp.handler.DownloadHandler;
import com.example.gateway.sftp.handler.UploadHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SftpApiRouterConfig {

    @Bean
    public RouterFunction<ServerResponse> upload(
        @Value("${api.path.upload}") String path, UploadHandler handler) {

        return route(POST(path), handler::handle);
    }

    @Bean
    public RouterFunction<ServerResponse> download(
        @Value("${api.path.download}") String path, DownloadHandler handler) {

        return route(POST(path), handler::handle);
    }
}
