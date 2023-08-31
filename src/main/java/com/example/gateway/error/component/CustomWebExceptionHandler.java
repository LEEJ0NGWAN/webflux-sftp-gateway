package com.example.gateway.error.component;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.gateway.error.constant.ErrorProperty;

import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.all;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.util.Map;

@Component
@Order(-2)
public class CustomWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    public CustomWebExceptionHandler(
        WebProperties webProperties,
        ErrorAttributes errorAttributes,
        ApplicationContext applicationContext,
        ServerCodecConfigurer serverCodecConfigurer) {

        super(errorAttributes, webProperties.getResources(), applicationContext);
        this.setMessageWriters(serverCodecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {

        return route(all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest serverRequest) {

        final Map<String, Object> errorAttributes =
        getErrorAttributes(serverRequest, ErrorAttributeOptions.of(Include.MESSAGE));

        final int status = (int) errorAttributes.get(ErrorProperty.STATUS.value());
        final String error = errorAttributes.get(ErrorProperty.ERROR.value()).toString();
        final String message = errorAttributes.get(ErrorProperty.MESSAGE.value()).toString();

        errorAttributes.clear();
        errorAttributes.put(ErrorProperty.ERROR.value(), error);
        errorAttributes.put(ErrorProperty.MESSAGE.value(), message);

        return ServerResponse.status(status).bodyValue(errorAttributes);
    }
}
