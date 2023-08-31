package com.example.gateway.sftp.properties;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Component
@ConfigurationProperties("sftp")
public class SftpProperties {

    private String serverAddress;
    private String serverUsername;
    private String defaultPath;
    private String privateKeyId;
    private int uploadChmod;
    private Map<String, String> config;

    public void setUploadChmod(String chmod) {

        uploadChmod = Integer.parseInt(chmod, 8);
    }
}
