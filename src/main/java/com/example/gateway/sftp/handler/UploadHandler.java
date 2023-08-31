package com.example.gateway.sftp.handler;

import com.example.gateway.sftp.dto.upload.UploadRequest;
import com.example.gateway.sftp.dto.upload.UploadResponse;
import com.example.gateway.sftp.exception.InvalidRequestData;
import com.example.gateway.sftp.properties.SftpProperties;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import static com.example.gateway.sftp.util.StringUtils.isBlank;

import java.io.ByteArrayInputStream;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class UploadHandler {

    private static final Base64.Decoder DECODER = Base64.getDecoder();

    private final Session session;
    private final SftpProperties sftpProperties;

    public Mono<ServerResponse> handle(ServerRequest serverRequest) {

        return serverRequest
        .bodyToMono(UploadRequest.class)
        .filter(request -> !isBlank(request.getName())&&!isBlank(request.getPayload()))
        .switchIfEmpty(Mono.defer(()->Mono.error(new InvalidRequestData(UploadRequest.class))))
        .doOnNext(request -> {

            final String path = isBlank(request.getPath())?
            sftpProperties.getDefaultPath(): request.getPath();

            final String destination  = path.endsWith("/")?
            path + request.getName(): path + "/" + request.getName();

            request.setDestination(destination);
            request.setData(DECODER.decode(request.getPayload()));
        })
        .flatMap(
            request -> Mono.using(
                () -> (ChannelSftp) session.openChannel("sftp"),
                channel -> Mono.fromSupplier(() -> {

                    boolean result = false;
                    try (ByteArrayInputStream inputStream = new ByteArrayInputStream(request.getData())) {

                        channel.connect();
                        channel.put(inputStream, request.getDestination());
                        channel.chmod(sftpProperties.getUploadChmod(), request.getDestination());

                        result = true;

                    } catch (Exception e) { log.error(e.getMessage()); }

                    return UploadResponse
                    .builder()
                    .result(result)
                    .location(request.getDestination())
                    .build();
                }),
                channel -> channel.disconnect()))
        .flatMap(ServerResponse.ok()::bodyValue);
    }
}
