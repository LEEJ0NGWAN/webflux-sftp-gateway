package com.example.gateway.sftp.handler;

import static com.example.gateway.sftp.util.StringUtils.isBlank;

import java.util.Base64;

import com.example.gateway.sftp.dto.download.DownloadRequest;
import com.example.gateway.sftp.dto.download.DownloadResponse;
import com.example.gateway.sftp.exception.InvalidRequestData;
import com.example.gateway.sftp.properties.SftpProperties;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;

import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class DownloadHandler {

    private static final Base64.Encoder ENCODER = Base64.getEncoder();

    private final Session session;
    private final SftpProperties sftpProperties;

    public Mono<ServerResponse> handle(ServerRequest serverRequest) {

        return serverRequest
        .bodyToMono(DownloadRequest.class)
        .filter(request -> !isBlank(request.getName()))
        .switchIfEmpty(Mono.defer(()->Mono.error(new InvalidRequestData(DownloadRequest.class))))
        .flatMap(
            request -> Mono
            .justOrEmpty(request.getPath())
            .defaultIfEmpty(sftpProperties.getDefaultPath())
            .map(
                path -> path.endsWith("/")?
                path+request.getName(): path+"/"+request.getName()))
        .flatMap(
            destination -> Mono.using(
                () -> (ChannelSftp) session.openChannel("sftp"),
                channel -> Mono.fromSupplier(() -> {

                    String payload = null;
                    boolean result = false;

                    try (FastByteArrayOutputStream outputStream = new FastByteArrayOutputStream()) {

                        channel.connect();
                        channel.get(destination, outputStream);

                        payload = ENCODER.encodeToString(outputStream.toByteArray());
                        result = true;

                    } catch (Exception e) { log.error(e.getMessage()); }

                    return DownloadResponse
                    .builder()
                    .result(result)
                    .payload(payload)
                    .build();
                }),
                channel -> channel.disconnect()))
        .flatMap(ServerResponse.ok()::bodyValue);
    }
}
